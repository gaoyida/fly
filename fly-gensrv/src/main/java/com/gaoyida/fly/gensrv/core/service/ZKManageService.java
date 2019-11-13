package com.gaoyida.fly.gensrv.core.service;

import com.gaoyida.fly.common.Constant;
import com.gaoyida.fly.common.dt.ServerNode;
import com.gaoyida.fly.common.exception.RegisteServiceException;
import com.gaoyida.fly.common.log.Logger;
import com.gaoyida.fly.common.log.LoggerFactory;
import com.gaoyida.fly.common.util.ServerUtil;
import com.gaoyida.fly.gensrv.listen.ZKEvent;
import com.gaoyida.fly.gensrv.listen.ZKEventListener;
import com.gaoyida.fly.gensrv.zk.ZKClient;
import com.gaoyida.fly.gensrv.zk.ZKClientFactory;
import com.gaoyida.fly.gensrv.zk.ZKConfig;
import com.google.gson.Gson;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gaoyida
 * @date 2019/10/17 下午7:56
 * <p>
 * /fly
 * <p>
 * ..../server
 * .........../1
 * .........../2
 * <p>
 * ..../record
 * .........../id
 * ............../123500(sn, timestamp)
 * .........../current
 * .................../master
 * <p>
 * ..../workerId
 */
@Service
public class ZKManageService extends AbstractService {

    private static final String ROOT = "fly";
    private static final String SERVERNODE = "/server";
    private static final String RECORD = "/record";
    private static final String WORKERID = "/workerId";
    private static final String CURRENTPATH = RECORD + "/current";
    private static final String IDPATH = RECORD + "/id";
    private static final String MASTERPATH = CURRENTPATH + "/master";
    private static final List<String> CHECKPATHS = Arrays.asList(SERVERNODE, RECORD, WORKERID, CURRENTPATH, IDPATH);
    public ZKClient zkClient;
    private Logger logger = LoggerFactory.getLogger(ZKManageService.class);
    private String serverInfo = null;
    private AtomicBoolean isMaster = new AtomicBoolean(false);
    private AtomicBoolean isAvaliable = new AtomicBoolean(false);
    private volatile ServerNode masterNode;
    private volatile ServerNode serverNode;
    private Lock serverLock = new ReentrantLock();

    private ZKConfig zkConfig;

    private volatile int workerId;

    @Resource
    private List<ZKEventListener> listenerList;

    private Gson gson = new Gson();

    public void fireListenEvent(ZKEvent ZKEvent, Object attachment) {
        if (CollectionUtils.isEmpty(listenerList)) {
            return;
        }
        try {
            for (ZKEventListener ZKEventListener : listenerList) {
                ZKEventListener.listen(ZKEvent, attachment);
            }
        } catch (Exception e) {
            logger.error("fire listener occur error:" + e.getMessage(), e);
        }
    }

    public void startService() {
        try {
            zkConfig.setRoot(ROOT);

            zkClient = ZKClientFactory.initZKClient(zkConfig, new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    try {
                        if (ConnectionState.RECONNECTED == newState) {
                            init();
                        }
                        notifyReconnection();
                    } catch (Throwable e) {
                        logger.error("handle ConnectionState " + newState + " change occurred error " + e.getMessage(), e);
                    }
                }
            });
            logger.info("established connection to " + zkConfig.getConnectUrl() + " success.");
            init();
        } catch (Throwable e) {
            logger.error("StateManager start error " + e.getMessage(), e);
        }
    }

    protected void notifyReconnection() {
        fireListenEvent(ZKEvent.RECONNECT, workerId);
        logger.warn("zk connected happened!");
    }

    private synchronized void init() throws Exception {
        checkZKPath();
        registerService();
    }

    private void checkZKPath() throws Exception {
        for (String checkPath : CHECKPATHS) {
            try {
                if (!zkClient.checkExists(checkPath)) {
                    zkClient.createPersistent(checkPath, "", true);
                }
            } catch (Exception e) {
                logger.error("create path " + checkPath + "occur error: " + e.getMessage(), e);
                throw e;
            }
        }
    }

    private void registerService() throws Exception {
        String sequential = zkClient.createSequential(WORKERID + "/ID", "", false);
        workerId = Integer.valueOf(sequential.substring(12, sequential.length()));

        serverNode = ServerUtil.toServerNode(workerId);
        String serverPath = SERVERNODE + "/" + workerId;
        serverInfo = gson.toJson(serverNode);

        if (!zkClient.checkExists(serverPath)) {
            zkClient.createEphemeral(serverPath, serverInfo, false);
            serverLock.lock();
            try {
                acquireMasterPrivilege();
                if (isMaster.get()) { //抢占
                    masterNode = serverNode;
                } else {
                    String masterInfo = zkClient.getData(MASTERPATH);
                    if (!StringUtils.isEmpty(masterInfo)) {
                        masterNode = gson.fromJson(masterInfo, ServerNode.class);
                    } else {
                        throw new KeeperException.NoNodeException("get node data occur error:" + MASTERPATH);
                    }
                    zkClient.addTargetChildListener(CURRENTPATH, this::checkMasterChanged);
                }
                fireListenEvent(ZKEvent.MASTER_CHANGE, masterNode);
            } finally {
                serverLock.unlock();
            }
        } else {
            logger.error("registe service occur concurrency error.");
            throw new RegisteServiceException("registe service occur concurrency error.");
        }
    }

    private void acquireMasterPrivilege() {
        try {
            zkClient.createEphemeral(MASTERPATH, serverInfo, false);
            isMaster.compareAndSet(false, true);
        } catch (KeeperException.NodeExistsException ne) {
            logger.info("acquire master failed...");
        } catch (Throwable e) {
            logger.error("create node:" + MASTERPATH + "occur error: " + e.getMessage(), e);
        }
    }

    private void checkMasterChanged(String path, List<String> children) throws Exception {
        serverLock.lock();
        try {
            boolean hasMaster = zkClient.checkExists(MASTERPATH);
            if (!hasMaster) {
                acquireMasterPrivilege();
            }
            if (isMaster.get()) {
                masterNode = serverNode;
            } else {
                String data = zkClient.getData(MASTERPATH);
                masterNode = new Gson().fromJson(data, ServerNode.class);
            }
            fireListenEvent(ZKEvent.MASTER_CHANGE, masterNode);
        } finally {
            serverLock.unlock();
        }
    }

    public long getIdEnd() throws Exception {
        List<String> children = zkClient.getChildren(IDPATH);
        if (!CollectionUtils.isEmpty(children)) {
            String lastId = children.stream().max(Comparator.comparingLong(Long::parseLong)).get();
            return Long.parseLong(lastId);
        }
        return 0;
    }

    public AtomicBoolean getIsMaster() {
        return isMaster;
    }

    public String getMasterAddress() {
        return masterNode == null ? null : masterNode.getIp();
    }

    public void setZc(ZKConfig zkConfig) {
        this.zkConfig = zkConfig;
    }

    public void shutdownService() throws Exception {
        //TODO
    }

    public AtomicBoolean getIsAvaliable() {
        return isAvaliable;
    }

    public ServerNode getServerNode() {
        return serverNode;
    }

    public long expandAndGet(long id) {
        serverLock.lock();
        try {
            id += Constant.GAP;
            zkClient.createPersistent(IDPATH + "/" + (id), new Gson().toJson(new Object()), false);
            return id;
        } catch (Exception e) {
            logger.error("expand id error " + e.getMessage(), e);
        } finally {
            serverLock.unlock();
        }
        return 0;
    }
}
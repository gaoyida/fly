package com.gaoyida.fly.gensrv.zk;

import com.gaoyida.fly.common.exception.ZKClientInitException;
import com.gaoyida.fly.gensrv.listen.ChildListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author gaoyida
 * @date 2019/10/17 下午5:42
 */
public class ZKClient {

    private static final String UTF8 = "UTF-8";

    private final Set<ConnectionStateListener> stateListeners = new CopyOnWriteArraySet<ConnectionStateListener>();

    private CuratorFramework curatorFramework;

    public ZKClient(CuratorFramework curatorFramework, ConnectionStateListener stateListener) {
        this.curatorFramework = curatorFramework;
        if (null != stateListener)
            stateListeners.add(stateListener);
    }

    public void stateChanged(ConnectionState state) {
        for (ConnectionStateListener stateListener : stateListeners) {
            stateListener.stateChanged(curatorFramework, state);
        }
    }

    private void check() throws ZKClientInitException {
        if (null == curatorFramework)
            throw new ZKClientInitException("ZkClientSupport not init success.");
    }

    /**
     * 检查指定的path
     *
     * @param path
     * @return
     * @throws Exception
     */
    public boolean checkExists(final String path) throws Exception {
        check();
        return null != curatorFramework.checkExists().forPath(path);
    }

    public String createSequential(String path, String data, boolean ignoreNodeExists) throws Exception {
        return create(path, CreateMode.PERSISTENT_SEQUENTIAL, data, ignoreNodeExists);
    }

    public void createEphemeral(String path, String data, boolean ignoreNodeExists) throws Exception {
        create(path, CreateMode.EPHEMERAL, data, ignoreNodeExists);
    }

    public void createPersistent(String path, String data, boolean ignoreNodeExists) throws Exception {
        create(path, CreateMode.PERSISTENT, data, ignoreNodeExists);
    }

    public String creatingWithParent(String path, CreateMode createmode, String data) throws Exception {
        check();
        return curatorFramework.create().creatingParentsIfNeeded().withMode(createmode).forPath(path,
                data.getBytes(Charset.forName(UTF8)));
    }

    public String getData(String path) throws Exception {
        check();
        if (checkExists(path)) {
            curatorFramework.sync().forPath(path);
            return new String(curatorFramework.getData().forPath(path), Charset.forName(UTF8));
        }
        throw new KeeperException.NoNodeException("no node at " + path);
    }

    public List<String> getChildren(String path) throws Exception {
        check();
        if (!checkExists(path)) {
            throw new KeeperException.NoNodeException("no node at " + path);
        }
        return curatorFramework.getChildren().forPath(path);
    }

    public List<String> addTargetChildListener(String path, ChildListener listener) throws Exception {
        check();
        return curatorFramework.getChildren().usingWatcher(new CuratorChildrenWatcherImpl(listener)).forPath(path);

    }

    private String create(String path, CreateMode createmode, String data, boolean ignoreNodeExists) throws Exception {
        check();
        try {
            return curatorFramework.create().withMode(createmode).forPath(path, data.getBytes(Charset.forName(UTF8)));
        } catch (KeeperException.NodeExistsException ex) {
            if (ignoreNodeExists) {
                return ex.getPath();
            }
            throw ex;
        }
    }

    private class CuratorChildrenWatcherImpl implements CuratorWatcher {
        private volatile ChildListener listener;

        public CuratorChildrenWatcherImpl(ChildListener listener) {
            this.listener = listener;
        }

        @Override
        public void process(WatchedEvent event) throws Exception {
            String path = null;
            if (listener != null && null != (path = event.getPath())) {
                listener.childChanged(path,
                        StringUtils.isEmpty(path) ? curatorFramework.getChildren().usingWatcher(this).forPath(path)
                                : Collections.<String>emptyList());
            }
        }
    }
}

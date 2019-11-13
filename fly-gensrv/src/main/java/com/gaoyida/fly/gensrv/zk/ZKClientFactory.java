package com.gaoyida.fly.gensrv.zk;

import com.gaoyida.fly.common.exception.ZKClientInitException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * @author gaoyida
 * @date 2019/10/17 下午6:44
 */
public class ZKClientFactory {

    public static ZKClient initZKClient(ZKConfig zkConfig, ConnectionStateListener listener) throws InterruptedException, ZKClientInitException {
        zkConfig.checkConfig();
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(zkConfig.getConnectUrl())
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(zkConfig.getTimeout())
                .sessionTimeoutMs(zkConfig.getSessionTimeout())
                .namespace(zkConfig.getRoot()).build();


        ZKClient zkClient = new ZKClient(curatorFramework, listener);
        curatorFramework.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState state) {
                zkClient.stateChanged(state);
            }
        });

        curatorFramework.start();

        if (!curatorFramework.blockUntilConnected(10, TimeUnit.SECONDS)) {
            throw new ZKClientInitException("can not established to " + zkConfig.getConnectUrl());
        }
        return zkClient;
    }


}

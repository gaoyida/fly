package com.gaoyida.fly.gensrv.core.service;

import com.gaoyida.fly.common.log.Logger;
import com.gaoyida.fly.common.log.LoggerFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author gaoyida
 * @date 2019/10/24 上午11:32
 */
public abstract class AbstractFlyService extends AbstractService {

    protected ChannelPipelineFactory channelPipelineFactory;
    private Logger logger = LoggerFactory.getLogger(AbstractFlyService.class);
    private ServerBootstrap bootstrap;
    private NioServerSocketChannelFactory channelFactory;
    private int bossThreadCount = Runtime.getRuntime().availableProcessors();
    private int workerThreadCount = Runtime.getRuntime().availableProcessors();
    private ThreadPoolExecutor bossExecutor;
    private ThreadPoolExecutor workerExecutor;

    public void startService() throws Exception {

        bossExecutor = new ThreadPoolExecutor(1, bossThreadCount, 120L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        workerExecutor = new ThreadPoolExecutor(workerThreadCount, workerThreadCount, 120L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        channelFactory = new NioServerSocketChannelFactory(bossExecutor, workerExecutor, workerThreadCount);

        bootstrap = new ServerBootstrap(channelFactory);

        bootstrap.setPipelineFactory(channelPipelineFactory);

        List<SocketAddress> bindAddresses = new ArrayList<>();

        bindAddresses.add(new InetSocketAddress(getPort()));

        for (SocketAddress address : bindAddresses) {
            bootstrap.bind(address);
            logger.info("start server, listening to " + address);
        }
    }

    public void shutdownService() {
        bootstrap.releaseExternalResources();
        bossExecutor.shutdown();
        workerExecutor.shutdown();
    }

    protected abstract int getPort();

}

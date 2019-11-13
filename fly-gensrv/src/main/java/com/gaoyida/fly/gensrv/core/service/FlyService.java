package com.gaoyida.fly.gensrv.core.service;

import com.gaoyida.fly.common.Constant;
import com.gaoyida.fly.gensrv.net.handler.biz.DispatcherHandler;
import com.gaoyida.fly.gensrv.net.handler.channel.BizUpstreamHandler;
import com.gaoyida.fly.gensrv.net.handler.channel.TLVMessageDecoder;
import com.gaoyida.fly.gensrv.net.handler.channel.TLVMessageEncoder;
import com.gaoyida.fly.gensrv.zk.ZKConfig;
import org.jboss.netty.channel.Channels;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author gaoyida
 * @date 2019/10/18 下午9:17
 */
@Service
public class FlyService extends AbstractFlyService implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${zookeeper.connectUrl}")
    private String zkConnectionUrl;

    @Resource
    private ZKManageService zkManagerService;
    //    @Resource
//    private ForwardService forwardService;
    @Resource
    private DispatcherHandler dispatcherHandler;

    private AtomicBoolean inited = new AtomicBoolean();

    public void startService() throws Exception {

        //先启动zk manager
        ZKConfig zc = new ZKConfig();
        zc.setConnectUrl(zkConnectionUrl);
        zkManagerService.setZc(zc);
        zkManagerService.startService();

//        forwardService.setDispatcherHandler(dispatcherHandler);
//        forwardService.startService();

        //开启请求监听
        channelPipelineFactory = () -> Channels.pipeline(
                new TLVMessageEncoder(),
                new TLVMessageDecoder(dispatcherHandler),
                new BizUpstreamHandler(dispatcherHandler));
        super.startService();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    FlyService.super.shutdownService();
//                    forwardService.shutdownService();
                    zkManagerService.shutdownService();
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    protected int getPort() {
        return Constant.LISTEN_PORT;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if (inited.compareAndSet(false, true)) {
                startService();
            }
        } catch (Exception ex) {
            throw new ApplicationContextException("init occur error " + ex.getMessage(), ex);
        }
    }
}

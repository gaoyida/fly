package com.gaoyida.fly.gensrv.net.handler.channel;

import com.gaoyida.fly.common.protocol.TLVDownStreamMessage;
import com.gaoyida.fly.common.protocol.TLVUpStreamMessage;
import com.gaoyida.fly.gensrv.net.handler.biz.DispatcherHandler;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * @author gaoyida
 * @date 2019/10/23 下午5:20
 */
public class BizUpstreamHandler extends SimpleChannelUpstreamHandler {

    private DispatcherHandler dispatcherHandler;

    public BizUpstreamHandler(DispatcherHandler dispatcherHandler) {
        this.dispatcherHandler = dispatcherHandler;
    }

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Channel channel = ctx.getChannel();
        Object msg = e.getMessage();

        if (msg instanceof TLVUpStreamMessage) {
            TLVUpStreamMessage request = (TLVUpStreamMessage) msg;
            TLVDownStreamMessage response = dispatcherHandler.handle(request, channel);
            if (response.isFeedBack()) {
                channel.write(response);
            }
        } else {
            throw new RuntimeException("handle message type occur error:" + msg.getClass().getName());
        }
        ctx.sendUpstream(e);
    }

}

package com.gaoyida.fly.gensrv.net.handler;

import com.gaoyida.fly.common.protocol.TLVDownStreamMessage;
import com.gaoyida.fly.common.protocol.TLVUpStreamMessage;
import org.jboss.netty.channel.Channel;

/**
 * @author gaoyida
 * @date 2019/10/24 下午7:22
 */
public abstract class AbstractCallHandler<REQ extends TLVUpStreamMessage, RES extends TLVDownStreamMessage> extends AbstractMessageHandler<REQ, RES> {
    @Override
    public void handleMessage(REQ request, RES response, Channel channel) {

    }
}

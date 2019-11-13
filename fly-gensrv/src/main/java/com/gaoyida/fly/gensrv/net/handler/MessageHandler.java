package com.gaoyida.fly.gensrv.net.handler;

import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.TLVDownStreamMessage;
import com.gaoyida.fly.common.protocol.TLVUpStreamMessage;
import org.jboss.netty.channel.Channel;

/**
 * @author gaoyida
 * @date 2019/10/19 下午1:09
 */
public interface MessageHandler<REQ extends TLVUpStreamMessage, RES extends TLVDownStreamMessage> {

    String getName();

    MessageType getMessageType() throws Exception;

    Class<REQ> getRequestMessageClass();

    Class<RES> getResponseMessageClass();

    TLVUpStreamMessage decode(byte[] raw) throws Exception;

    void handle(REQ request, RES response, Channel channel);

}

package com.gaoyida.fly.gensrv.net.handler;

import com.gaoyida.fly.common.Constant;
import com.gaoyida.fly.common.exception.MessageHandleException;
import com.gaoyida.fly.common.log.Logger;
import com.gaoyida.fly.common.log.LoggerFactory;
import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.TLVDownStreamMessage;
import com.gaoyida.fly.common.protocol.TLVUpStreamMessage;
import com.google.gson.Gson;
import org.jboss.netty.channel.Channel;

import java.io.UnsupportedEncodingException;

/**
 * @author gaoyida
 * @date 2019/10/22 下午8:15
 */
public abstract class AbstractMessageHandler<REQ extends TLVUpStreamMessage, RES extends TLVDownStreamMessage> implements MessageHandler<REQ, RES> {
    private static Logger logger = LoggerFactory.getLogger(AbstractMessageHandler.class);
    private static Gson gson = new Gson();

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public void handle(REQ request, RES response, Channel channel) {
        try {
            response.setMsgId(request.getMsgId());
            handleMessage(request, response, channel);
        } catch (Exception e) {
            response.setErrorCode(Constant.CODE_ERR);
            logger.error("handle message occur error:" + e.getMessage(), e);
        }
    }

    @Override
    public MessageType getMessageType() throws Exception {
        try {
            Class<? extends TLVUpStreamMessage> c = getRequestMessageClass();
            return c.newInstance().getType();
        } catch (Exception e) {
            throw new MessageHandleException("get message" + getRequestMessageClass().getName() + "occur error", e);
        }
    }

    @Override
    public TLVUpStreamMessage decode(byte[] bytes) throws Exception {
        try {
            String str = null;
            try {
                str = new String(bytes, "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("UnsupportedEncodingException: " + e.getMessage(), e);
            }
            return gson.fromJson(str, getRequestMessageClass());
        } catch (Exception e) {
            logger.error("occur error :" + e.getMessage(), e);
            throw e;
        }
    }

    public abstract void handleMessage(REQ request, RES response, Channel channel);
}

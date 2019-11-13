package com.gaoyida.fly.gensrv.net.handler.biz;

import com.gaoyida.fly.common.Constant;
import com.gaoyida.fly.common.exception.NoMsgHandlerFoundException;
import com.gaoyida.fly.common.log.Logger;
import com.gaoyida.fly.common.log.LoggerFactory;
import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.TLVDownStreamMessage;
import com.gaoyida.fly.common.protocol.TLVUpStreamMessage;
import com.gaoyida.fly.common.protocol.seq.GetSequentialIDResponse;
import com.gaoyida.fly.gensrv.core.service.ZKManageService;
import com.gaoyida.fly.gensrv.net.handler.MessageHandler;
import org.jboss.netty.channel.Channel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaoyida
 * @date 2019/10/25 下午6:36
 */
@Component
public class DispatcherHandler implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);

    @Resource
    private List<MessageHandler> messageHandlerList;
    @Resource
    private ZKManageService zkManageService;

    private Map<Short, MessageHandler> messageHandlerMap;

    public TLVDownStreamMessage handle(TLVUpStreamMessage req, Channel channel) throws Exception {
        TLVDownStreamMessage response = null;
        MessageHandler messageHandler = messageHandlerMap.get(req.getType().getType());
        if (messageHandler == null) {
            logger.error("not implemented type:" + req.getType());
            throw new NoMsgHandlerFoundException("no handler found for message " + req.getType());
        }
        response = (TLVDownStreamMessage) messageHandler.getResponseMessageClass().newInstance();
        if (!zkManageService.getIsMaster().get() && MessageType.GET_SEQUENTIAL_ID_REQUEST.equals(req.getType())) {
            if (response instanceof GetSequentialIDResponse) {
                response.setErrorCode(Constant.CODE_FORWARD);
                ((GetSequentialIDResponse) response).setMasterIp(zkManageService.getMasterAddress());
            }
        } else {
            messageHandler.handle(req, response, channel);
        }
        return response;
    }

    public TLVUpStreamMessage decode(short type, byte[] dst) throws Exception {
        MessageHandler messageHandler = messageHandlerMap.get(type);
        if (messageHandler == null) {
            logger.error("handle type occur error " + type + "no implementation.");
            throw new NoMsgHandlerFoundException("handle type occur error " + type + "no implementation.");
        }
        return messageHandler.decode(dst);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        messageHandlerMap = new HashMap<>();
        for (MessageHandler messageHandler : messageHandlerList) {
            messageHandlerMap.put(messageHandler.getMessageType().getType(), messageHandler);
        }
    }
}

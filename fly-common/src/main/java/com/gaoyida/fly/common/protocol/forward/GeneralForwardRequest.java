package com.gaoyida.fly.common.protocol.forward;

import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.TLVUpStreamMessage;

/**
 * @author gaoyida
 * @date 2019/10/25 下午7:01
 * 转发出去的消息体
 */
public class GeneralForwardRequest extends TLVUpStreamMessage {

    private MessageType forwardType;

    public MessageType getType() {
        return MessageType.FORWARD_GET_ID_REQUEST;
    }

    public MessageType getForwardType() {
        return forwardType;
    }

    public void setForwardType(MessageType forwardType) {
        this.forwardType = forwardType;
    }
}

package com.gaoyida.fly.common.protocol.forward;

import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.TLVDownStreamMessage;

/**
 * @author gaoyida
 * @date 2019/10/25 下午7:38
 */
public class GeneralForwardMasterRequest extends TLVDownStreamMessage {

    private MessageType forwardType;

    public MessageType getForwardType() {
        return forwardType;
    }

    public void setForwardType(MessageType forwardType) {
        this.forwardType = forwardType;
    }

    @Override
    public MessageType getType() {
        return MessageType.FORWARD_GET_ID_REQUEST;
    }
}

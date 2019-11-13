package com.gaoyida.fly.common.protocol.forward;

import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.TLVUpStreamMessage;

/**
 * @author gaoyida
 * @date 2019/10/25 下午7:53
 */
public class GeneralForwardMasterResponse extends TLVUpStreamMessage {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.FORWARD_GET_ID_RESPONSE;
    }
}

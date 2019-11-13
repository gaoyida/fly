package com.gaoyida.fly.common.protocol.unqiue;

import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.TLVDownStreamMessage;

/**
 * @author gaoyida
 * @date 2019/10/22 下午8:34
 */
public class GetUniqueIDResponse extends TLVDownStreamMessage {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.GET_UNIQUE_ID_RESPONSE;
    }
}

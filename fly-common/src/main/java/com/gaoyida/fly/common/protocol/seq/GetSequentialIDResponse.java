package com.gaoyida.fly.common.protocol.seq;

import com.gaoyida.fly.common.protocol.MessageType;
import com.gaoyida.fly.common.protocol.TLVDownStreamMessage;

/**
 * @author gaoyida
 * @date 2019/10/22 下午9:02
 */
public class GetSequentialIDResponse extends TLVDownStreamMessage {

    private long id;

    private String masterIp;

    public String getMasterIp() {
        return masterIp;
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.GET_SEQUENTIAL_ID_RESPONSE;
    }
}

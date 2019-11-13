package com.gaoyida.fly.common.protocol;

/**
 * @author gaoyida
 * @date 2019/10/22 下午9:02
 */
public class GetMasterResponse extends TLVDownStreamMessage {

    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public MessageType getType() {
        return MessageType.GET_MASTER_RESPONSE;
    }
}

package com.gaoyida.fly.common.dt;

/**
 * @author gaoyida
 * @date 2019/10/18 上午10:58
 */
public class ServerNode {
    private int id;

    private String ip;

    private long timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

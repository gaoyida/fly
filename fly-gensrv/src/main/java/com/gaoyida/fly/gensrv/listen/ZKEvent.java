package com.gaoyida.fly.gensrv.listen;

/**
 * @author gaoyida
 * @date 2019/10/24 下午11:15
 */
public enum ZKEvent {

    MASTER_CHANGE("master状态变更"),

    RECONNECT("重连");

    private String descp;

    ZKEvent(String descp) {
        this.descp = descp;
    }
}

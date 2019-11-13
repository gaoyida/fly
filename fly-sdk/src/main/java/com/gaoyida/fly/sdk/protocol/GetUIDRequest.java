package com.gaoyida.fly.sdk.protocol;

import com.gaoyida.fly.common.protocol.MessageType;

/**
 * @author gaoyida
 * @date 2019/10/22 下午5:03
 */
public class GetUIDRequest implements RequestBase {

    private String appName;

    private String instance;

    @Override
    public MessageType getMessageType() {
        return MessageType.GET_UNIQUE_ID_REQUEST;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
}

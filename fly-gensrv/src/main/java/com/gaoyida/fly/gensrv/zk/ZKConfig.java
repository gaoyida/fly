package com.gaoyida.fly.gensrv.zk;

import org.springframework.util.StringUtils;

/**
 * @author gaoyida
 * @date 2019/10/17 下午7:09
 */
public class ZKConfig {
    private String root;
    private String connectUrl;
    private int timeout = 3000;
    private int sessionTimeout = 10000;

    public ZKConfig() {
    }

    public ZKConfig(String connectUrl, String root) {
        this.connectUrl = connectUrl;
        this.root = root;
    }

    public void checkConfig() {

        if (StringUtils.isEmpty(this.root)) {
            throw new IllegalArgumentException("ZKConfig root attribute not support blank.");
        }
        if (this.root.startsWith("/")) {
            throw new IllegalArgumentException("ZKConfig root attribute start with /");
        }
        if (this.root.endsWith("/")) {
            throw new IllegalArgumentException("ZKConfig root attribute end with /");
        }
        if (StringUtils.isEmpty(this.connectUrl)) {
            throw new IllegalArgumentException("ZKConfig connectUrl attribute not support blank.");
        }
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getConnectUrl() {
        return connectUrl;
    }

    public void setConnectUrl(String connectUrl) {
        this.connectUrl = connectUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        if (timeout <= 0) return;
        this.timeout = timeout;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        if (sessionTimeout <= 0) return;
        this.sessionTimeout = sessionTimeout;
    }
}

package com.gaoyida.fly.sdk.exception;

/**
 * @author gaoyida
 * @date 2019/10/22 下午2:58
 */
public class ConnectionException extends RuntimeException {
    public ConnectionException(String msg) {
        super(msg);
    }

    public ConnectionException(String s, Exception e) {
        super(s, e);
    }
}

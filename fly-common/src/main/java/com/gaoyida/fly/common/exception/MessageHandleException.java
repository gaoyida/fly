package com.gaoyida.fly.common.exception;

/**
 * @author gaoyida
 * @date 2019/10/22 下午8:24
 */
public class MessageHandleException extends RuntimeException {

    public MessageHandleException(String s, Exception e) {
        super(s, e);
    }
}

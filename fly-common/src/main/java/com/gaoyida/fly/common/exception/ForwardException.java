package com.gaoyida.fly.common.exception;

/**
 * @author gaoyida
 * @date 2019/10/30 上午12:06
 */
public class ForwardException extends RuntimeException {
    public ForwardException(String message) {
        super(message);
    }

    public ForwardException(String message, Throwable cause) {
        super(message, cause);
    }
}

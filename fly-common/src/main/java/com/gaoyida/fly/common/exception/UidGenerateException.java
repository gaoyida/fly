package com.gaoyida.fly.common.exception;

/**
 * @author gaoyida
 * @date 2019/10/26 下午1:28
 */
public class UidGenerateException extends RuntimeException {
    public UidGenerateException(String message) {
        super(message);
    }

    public UidGenerateException(String message, Throwable cause) {
        super(message, cause);
    }
}

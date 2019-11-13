package com.gaoyida.fly.common.exception;

/**
 * @author gaoyida
 * @date 2019/10/28 下午2:01
 */
public class InitExeception extends RuntimeException {
    public InitExeception(String message) {
        super(message);
    }

    public InitExeception(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.gaoyida.fly.common.log;

/**
 * @author gaoyida
 * @date 2019/10/17 下午8:10
 */
public class Logger {
    protected org.apache.logging.log4j.Logger log;

    public Logger(org.apache.logging.log4j.Logger log) {
        this.log = log;
    }

    public void error(String message, Throwable e) {
        log.error(message, e);
    }

    public void error(String message) {
        log.error(message);
    }

    public void error(Throwable e) {
        log.error(e);
    }

    public void warn(String message) {
        log.warn(message);
    }

    public void warn(String message, Throwable e) {
        log.warn(message, e);
    }

    public void warn(Throwable e) {
        log.warn(e);
    }

    public void info(Throwable e) {
        log.info(e);
    }

    public void info(String message) {
        log.info(message);
    }
}

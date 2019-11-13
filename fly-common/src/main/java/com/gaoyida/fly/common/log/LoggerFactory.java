package com.gaoyida.fly.common.log;

import org.apache.logging.log4j.LogManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaoyida
 * @date 2019/10/17 下午8:14
 */
public class LoggerFactory {

    private static Map<String, Logger> loggerMap = new ConcurrentHashMap<>();

    public static Logger getLogger(String name) {
        if (name == null || "".equals(name))
            throw new IllegalArgumentException("logger name is null.");
        Logger log = new Logger(LogManager.getLogger(name));
        Logger preLog = loggerMap.putIfAbsent(name, log);
        if (null != preLog) return preLog;
        return log;
    }

    public static Logger getLogger(Class<?> clazz) {
        if (null == clazz)
            throw new IllegalArgumentException("not support Class is null argument.");
        return getLogger(clazz.getName());
    }

}

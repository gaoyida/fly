package com.gaoyida.fly.common.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamingThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOLNUM = new AtomicInteger(1);

    private final AtomicInteger threadNum = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemo;

    private final ThreadGroup group;

    public NamingThreadFactory() {
        this("pool-" + POOLNUM.getAndIncrement(), false);
    }

    public NamingThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamingThreadFactory(String prefix, boolean daemo) {
        this.prefix = prefix + "-thread-";
        this.daemo = daemo;
        SecurityManager s = System.getSecurityManager();
        this.group = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    public Thread newThread(Runnable runnable) {
        String name = prefix + threadNum.getAndIncrement();
        Thread ret = new Thread(group, runnable, name, 0);
        ret.setDaemon(daemo);
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return group;
    }

}

package com.gaoyida.fly.gensrv.core.generate;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author gaoyida
 * @date 2019/10/28 上午11:05
 */
public class PaddedAtomicLong extends AtomicLong {
    public volatile long p1, p2, p3, p4, p5, p6;

    public PaddedAtomicLong() {
        super();
    }

    public PaddedAtomicLong(long initialValue) {
        super(initialValue);
    }

    public long sumPaddingToPreventOptimization() {
        return p1 + p2 + p3 + p4 + p5 + p6;
    }
}

package com.gaoyida.fly.gensrv.core.generate;

import com.gaoyida.fly.common.exception.UidGenerateException;
import com.gaoyida.fly.common.log.Logger;
import com.gaoyida.fly.common.log.LoggerFactory;
import com.gaoyida.fly.gensrv.listen.ZKEvent;
import com.gaoyida.fly.gensrv.listen.ZKEventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author gaoyida
 * @date 2019/10/25 上午11:32
 * 基于Twitter的SnowFlake算法
 * 该实现参考https://github.com/baidu/uid-generator/blob/master/README.zh_cn.md
 * TODO 这里未实现Ringbuffer的cache，待优化！！！
 * 注意服务器重连ZK会导致workerId的重新获取，workerId要更新（其实不更新也没问题，但最好还是更新），
 * TODO 这里监听了listener，状态部分有耦合，看是否应该把耦合的状态提取出来
 */
@Component
public class UniqueGeneratorService implements ZKEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueGeneratorService.class);
    private static int timeBits = 28;//相对时间(s)，从"2015-08-25 00:00:00"开始计算
    private static final long maxDeltaSeconds = ~(-1L << timeBits);
    private static int workerBits = 22;//work占据的位
    private static final long maxWorkerId = ~(-1L << workerBits);
    private static int seqBits = 13;//同一时刻(s)的并发序列
    private static final long maxSequence = ~(-1L << seqBits);
    private static final int timestampShift = workerBits + seqBits;
    private static final int workerIdShift = seqBits;
    private int singBits = 1;//标志符，UID为正数
    private long sequence = 0L;
    private long lastSecond = -1L;
    private String epochStr = "2019-10-01";
    private long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1569859200000L);
    private volatile int workerId;

    private long getCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epochSeconds > maxDeltaSeconds) {
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }

        return currentSecond;
    }

    public synchronized long getNextId() {
        long currentSecond = getCurrentSecond();

        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            throw new UidGenerateException("Clock moved backwards. Refusing for " + refusedSeconds + " seconds.");
        }

        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
            }
        } else {
            sequence = 0L;
        }

        lastSecond = currentSecond;
        return ((currentSecond - epochSeconds) << timestampShift) | (workerId << workerIdShift) | sequence;

    }

    private long getNextSecond(long lastSecond) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastSecond) {
            timestamp = getCurrentSecond();
        }

        return timestamp;
    }

    public int getWorkerId() {
        return workerId;
    }

    public synchronized void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    @Override
    public synchronized void listen(ZKEvent event, Object attachement) {
        if (ZKEvent.RECONNECT == event) {
            if (attachement instanceof Integer) {
                setWorkerId((Integer) attachement);
            }
        }
    }
}

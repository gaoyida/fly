package com.gaoyida.fly.gensrv.core.generate;

import com.gaoyida.fly.common.dt.ServerNode;
import com.gaoyida.fly.common.exception.IdGenerateException;
import com.gaoyida.fly.common.exception.InitExeception;
import com.gaoyida.fly.common.log.Logger;
import com.gaoyida.fly.common.log.LoggerFactory;
import com.gaoyida.fly.common.util.NamingThreadFactory;
import com.gaoyida.fly.gensrv.core.service.ZKManageService;
import com.gaoyida.fly.gensrv.listen.ZKEvent;
import com.gaoyida.fly.gensrv.listen.ZKEventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author gaoyida
 * @date 2019/10/25 上午11:32
 */
@Component
public class SequentialGeneratorService implements ZKEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SequentialGeneratorService.class);
    private static final long paddingThreshold = 500;
    private final Object lock = new Object();
    @Resource
    private ZKManageService zkManageService;
    private ScheduledExecutorService expandService;
    private AtomicLong idStart = new AtomicLong(0);
    private AtomicLong idEnd = new AtomicLong(0);
    private AtomicBoolean state = new AtomicBoolean();

    public long getNextId() {

        if (!state.get()) {
            throw new IdGenerateException("service unavailable, try later.");

        }

        long nextId = idStart.getAndIncrement();
        if (idEnd.get() <= nextId) {
            synchronized (lock) {
                if (idEnd.get() <= nextId) {
                    asyncExpand();
                }
            }
        }
        if (idEnd.get() <= nextId) {
            logger.error("update id range occur error! ");
            state.compareAndSet(true, false);
            throw new IdGenerateException("update id range occur error, try later.");
        }
        return nextId;
    }

    private void asyncExpand() {
        synchronized (lock) {
            if (idEnd.get() - idStart.get() < paddingThreshold && zkManageService.getIsMaster().get()) {
                logger.warn("Reach the padding threshold, current at " + idStart.get() + ", current end " + idEnd.get());
                try {
                    long expandEndId = zkManageService.expandAndGet(idEnd.get());
                    idEnd.getAndSet(expandEndId);
                } catch (Exception ex) {
                    throw new InitExeception("init seq id occur error: " + ex.getMessage());
                }
            }
        }
    }

    @Override
    public void listen(ZKEvent event, Object attachement) throws Exception {
        if (ZKEvent.MASTER_CHANGE == event && attachement instanceof ServerNode) {
            ServerNode serverNode = (ServerNode) attachement;
            if (serverNode.getId() != zkManageService.getServerNode().getId()) {
                state.getAndSet(false);
            } else {
                if (expandService == null) {
                    expandService = Executors.newSingleThreadScheduledExecutor(
                            new NamingThreadFactory("asyncExpand", true));
                    expandService.scheduleWithFixedDelay(this::asyncExpand,
                            10, 10, TimeUnit.SECONDS);
                }
                if (!state.get()) {
                    long latestId = zkManageService.getIdEnd();
                    try {
                        long expandEndId = zkManageService.expandAndGet(latestId);
                        idStart = new AtomicLong(latestId + 1);
                        idEnd = new AtomicLong(expandEndId);
                    } catch (Exception ex) {
                        throw new InitExeception("init seq id occur error: " + ex.getMessage());
                    }
                    state.compareAndSet(false, true);
                }
            }
        }
    }
}

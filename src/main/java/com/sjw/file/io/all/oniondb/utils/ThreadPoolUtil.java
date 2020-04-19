package com.sjw.file.io.all.oniondb.utils;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolUtil {

    private ExecutorService COMMON_WORK_POOL;

    private ThreadPoolUtil() {
        if (null == COMMON_WORK_POOL) {
            COMMON_WORK_POOL = new ThreadPoolExecutor(5, 20, 60L,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1),
                    new NamedThreadFactory("common-work-thread"));
        }
    }

    public static final ThreadPoolUtil instance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ThreadPoolUtil INSTANCE = new ThreadPoolUtil();
    }

    public void runTask(Runnable runnable) {
        if (null == runnable) {
            return;
        }
        try {
            COMMON_WORK_POOL.execute(runnable);
        } catch (RejectedExecutionException e) {
            log.error("work thread is busy , so reject task");
            throw e;
        }
    }

    public void shutdown() {
        COMMON_WORK_POOL.shutdown();
    }


}

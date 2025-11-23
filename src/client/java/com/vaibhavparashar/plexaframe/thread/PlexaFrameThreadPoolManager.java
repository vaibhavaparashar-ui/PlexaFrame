package com.vaibhavparashar.plexaframe.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PlexaFrameThreadPoolManager {

    private static final int THREADS = Math.max(2, Runtime.getRuntime().availableProcessors() - 1);

    private static final ThreadPoolExecutor EXECUTOR =
            new ThreadPoolExecutor(
                    THREADS, THREADS,
                    60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    new ThreadFactory() {
                        private final AtomicInteger id = new AtomicInteger();
                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r, "PlexaFrame-Worker-" + id.incrementAndGet());
                        }
                    });

    public static void submit(Runnable runnable) {
        EXECUTOR.submit(runnable);
    }

    public static int getQueueSize() {
        return EXECUTOR.getQueue().size();
    }

    public static int getActiveThreads() {
        return EXECUTOR.getActiveCount();
    }
}

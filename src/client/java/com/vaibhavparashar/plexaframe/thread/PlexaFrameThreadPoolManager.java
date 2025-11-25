package com.vaibhavparashar.plexaframe.thread;

import net.minecraft.client.Minecraft;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class PlexaFrameThreadPoolManager {
    private static final int DEFAULT_WORKERS = 1;
    private static final boolean SAFE_MODE = Boolean.parseBoolean(System.getProperty("plexaframe.safeMode", "true"));
    private static final int WORKERS = SAFE_MODE ? Math.max(1, DEFAULT_WORKERS) : Math.max(1, Runtime.getRuntime().availableProcessors() - 1);

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            WORKERS, WORKERS,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactory() {
                private final AtomicInteger id = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "PlexaFrame-Worker-" + id.getAndIncrement());
                    t.setDaemon(true);
                    return t;
                }
            }
    );

    private PlexaFrameThreadPoolManager() {}

    /** Submit CPU-only work (safe). */
    public static void submitSafe(Runnable r) {
        if (r == null) return;
        try {
            EXECUTOR.execute(() -> {
                try { r.run(); } catch (Throwable t) { t.printStackTrace(); }
            });
        } catch (RejectedExecutionException ex) {
            postToMainThread(r);
        }
    }

    /** Post runnable onto Minecraft main thread (for GPU/upload). */
    public static void postToMainThread(Runnable r) {
        if (r == null) return;
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null) {
                mc.execute(() -> {
                    try { r.run(); } catch (Throwable t) { t.printStackTrace(); }
                });
            } else {
                try { r.run(); } catch (Throwable ignored) {}
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static int getQueueSize() { return EXECUTOR.getQueue().size(); }
    public static int getActiveCount() { return EXECUTOR.getActiveCount(); }
    public static void shutdown() { EXECUTOR.shutdownNow(); }
}

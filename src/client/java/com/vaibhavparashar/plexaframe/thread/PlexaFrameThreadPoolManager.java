package com.vaibhavparashar.plexaframe.thread;

import net.minecraft.client.Minecraft;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Safe-mode thread pool manager for PojavLauncher / mobile.
 *
 * Key design choices:
 * - Small fixed pool (1 or 2 threads) to avoid starvation on mobile.
 * - Tasks must NOT perform any direct OpenGL work (no buffer uploads).
 * - Final GPU/renderer updates MUST be posted back to the main thread via Minecraft.getInstance().execute(...)
 * - Soft-disable via system property "plexaframe.safeMode=false" if you want to force normal mode.
 */
public final class PlexaFrameThreadPoolManager {

    // Default to single worker on mobile; increase safely on desktop.
    private static final int DEFAULT_WORKERS = 1;
    private static final String SAFE_PROP = "plexaframe.safeMode";

    private static final boolean SAFE_MODE = Boolean.parseBoolean(
            System.getProperty(SAFE_PROP, "true")
    );

    private static final int WORKERS = SAFE_MODE
            ? Math.max(1, Math.min(DEFAULT_WORKERS, Runtime.getRuntime().availableProcessors() - 1))
            : Math.max(1, Runtime.getRuntime().availableProcessors() - 1);

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

    /** Submit a safe rebuild task (work must be CPU-only and non-GL). */
    public static void submitSafe(Runnable cpuOnlyTask) {
        if (cpuOnlyTask == null) return;
        try {
            EXECUTOR.execute(() -> {
                try {
                    cpuOnlyTask.run();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (RejectedExecutionException ex) {
            // Fallback: run on main thread if pool refuses tasks (safe, one-off)
            postToMainThread(cpuOnlyTask);
        }
    }

    /** Post a runnable to the Minecraft main thread. Use for GPU/upload steps only. */
    public static void postToMainThread(Runnable mainThreadTask) {
        if (mainThreadTask == null) return;
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null) {
                mc.execute(() -> {
                    try {
                        mainThreadTask.run();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                // If Minecraft is not available, run safely anyway (rare during shutdown)
                try { mainThreadTask.run(); } catch (Throwable ignored) {}
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static int getQueueSize() { return EXECUTOR.getQueue().size(); }
    public static int getActiveCount() { return EXECUTOR.getActiveCount(); }

    /** Graceful shutdown (call on mod shutdown if desired). */
    public static void shutdown() {
        EXECUTOR.shutdownNow();
    }
}

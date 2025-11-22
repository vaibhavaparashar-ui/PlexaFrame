package com.vaibhavparashar.plexaframe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.minecraft.Util;

/**
 * Manages the thread pool used for multi-threaded chunk meshing in PlexaFrame.
 * Offloads heavy computational work from the main game thread.
 */
public class PlexaFrameThreadPoolManager {
    
    // Determine the number of threads to use. 
    // It's common to use the number of available CPU cores minus one (for the main thread),
    // or a fixed, safe number like 4-8.
    private static final int THREAD_COUNT = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);

    // The thread pool executor responsible for scheduling the tasks.
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(
        THREAD_COUNT,
        // Custom thread factory to name the threads for easier debugging
        (runnable) -> {
            Thread thread = new Thread(runnable, "PlexaFrame Chunk Thread");
            // Lowers the priority slightly so the threads don't starve the main game thread
            thread.setPriority(Math.max(Thread.MIN_PRIORITY, thread.getPriority() - 1));
            return thread;
        }
    );

    /**
     * Submits a chunk meshing task to the background thread pool.
     * @param task The Runnable task representing the chunk rebuild work.
     */
    public static void submitChunkTask(Runnable task) {
        EXECUTOR.submit(task);
    }
    
    /**
     * Shuts down the thread pool gracefully when the game closes.
     * This should be called from the main mod initializer's shutdown hook (if implemented).
     */
    public static void shutdown() {
        PlexaFrame.LOGGER.info("Shutting down PlexaFrame Chunk Thread Pool...");
        EXECUTOR.shutdown();
        try {
            if (!EXECUTOR.awaitTermination(60, TimeUnit.SECONDS)) {
                EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            EXECUTOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}   
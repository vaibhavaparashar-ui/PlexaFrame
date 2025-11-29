package com.vaibhavparashar.plexaframe.thread;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SodiumSafeScheduler {
    private static final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

    public static void submit(Runnable task) {
        queue.add(task);
    }

    public static void runPending() {
        Runnable task;
        while ((task = queue.poll()) != null) {
            task.run();
        }
    }
}

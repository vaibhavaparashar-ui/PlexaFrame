package com.vaibhavparashar.plexaframe.thread;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SodiumRebuildScheduler {

    private static final ConcurrentLinkedQueue<RenderSection> queue = new ConcurrentLinkedQueue<>();
    private static final ExecutorService workers =
            Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() - 1));

    public static void submit(RenderSection section) {
        queue.add(section);
        workers.submit(() -> process(section));
    }

    private static void process(RenderSection section) {
        try {
            section.rebuild();
        } catch (Exception ex) {
            System.err.println("[PlexaFrame] Rebuild fail: " + ex.getMessage());
        }
    }
}

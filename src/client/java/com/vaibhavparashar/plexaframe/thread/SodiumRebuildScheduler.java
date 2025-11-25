package com.vaibhavparashar.plexaframe.thread;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SodiumRebuildScheduler {

    private static final SodiumRebuildWorker[] workers = new SodiumRebuildWorker[4];

static {
    for (int i = 0; i < workers.length; i++) {
        workers[i] = new SodiumRebuildWorker();
        workers[i].setName("Plexa-Rebuild-" + i);
        workers[i].start();
    }
}
    public static void submit(ChunkRenderContainer container) {
        queue.offer(container);
    }

    public static ChunkRenderContainer poll() {
        return queue.poll();
    }

    public static int size() {
        return queue.size();
    }
}

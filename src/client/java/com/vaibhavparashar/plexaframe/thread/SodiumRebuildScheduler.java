package com.vaibhavparashar.plexaframe.thread;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SodiumRebuildScheduler {

    private static final ConcurrentLinkedQueue<ChunkRenderContainer> queue = new ConcurrentLinkedQueue<>();

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

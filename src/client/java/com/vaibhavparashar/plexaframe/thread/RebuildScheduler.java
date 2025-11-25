package com.vaibhavparashar.plexaframe.thread;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SodiumRebuildScheduler {

    private static final ConcurrentLinkedQueue<RenderSection> queue = new ConcurrentLinkedQueue<>();

    public static void submit(RenderSection section) {
        queue.add(section);
    }

    public static RenderSection poll() {
        return queue.poll();
    }
}

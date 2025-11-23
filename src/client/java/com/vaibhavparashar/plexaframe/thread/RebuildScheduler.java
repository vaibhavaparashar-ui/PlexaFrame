package com.vaibhavparashar.plexaframe.thread;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderSection;

import java.util.Comparator;
import java.util.PriorityQueue;

public class RebuildScheduler {

    private static final PriorityQueue<RenderSection> QUEUE = new PriorityQueue<>(
            Comparator.comparingDouble(s -> Minecraft.getInstance().player.distanceToSqr(
                    s.getOrigin().getX(), s.getOrigin().getY(), s.getOrigin().getZ()
            ))
    );

    public static synchronized void enqueue(RenderSection section, long hash) {
        QUEUE.add(section);
        PlexaFrameThreadPoolManager.submit(new RebuildTask(section, hash));
    }

    public static int queued() {
        return QUEUE.size();
    }
}

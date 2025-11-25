package com.vaibhavparashar.plexaframe.thread;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher.RenderSection;
import com.vaibhavparashar.plexaframe.mixin.accessor.RenderSectionAccessor;

import java.util.PriorityQueue;

/**
 * Schedules chunk rebuild tasks with a distance-based priority queue.
 */
public class RebuildScheduler {

    private static final PriorityQueue<RenderSection> QUEUE = new PriorityQueue<>(
                    (a, b) -> Double.compare(
                        ((RenderSectionAccessor) a).getOrigin().distToCenterSqr(0.0, 0.0, 0.0),
                        ((RenderSectionAccessor) b).getOrigin().distToCenterSqr(0.0, 0.0, 0.0)
                    )
    );

    public static synchronized void enqueue(RenderSection section, long hash) {
        QUEUE.add(section);
        PlexaFrameThreadPoolManager.submitSafe(new RebuildTask(section, hash));
    }

    public static synchronized int queued() {
        return QUEUE.size();
    }
}

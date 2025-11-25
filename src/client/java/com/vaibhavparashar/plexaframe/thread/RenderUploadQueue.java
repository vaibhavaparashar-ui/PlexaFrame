package com.vaibhavparashar.plexaframe.thread;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RenderUploadQueue {

    private static final ConcurrentLinkedQueue<RenderSection> uploadQueue = new ConcurrentLinkedQueue<>();

    public static void enqueue(RenderSection section) {
        uploadQueue.add(section);
    }

    public static void flushUploads() {
        RenderSection section;
        while ((section = uploadQueue.poll()) != null) {
            section.uploadMeshes();
        }
    }
}

package com.vaibhavparashar.plexaframe.thread;

import java.util.concurrent.ConcurrentLinkedQueue;

/** Enqueue rebuilds that need final main-thread upload. */
public final class RenderUploadQueue {
    private static final ConcurrentLinkedQueue<Object> UPLOADS = new ConcurrentLinkedQueue<>();

    public static void enqueue(Object section) {
        if (section == null) return;
        UPLOADS.add(section);
    }

    public static void flushUploads() {
        Object s;
        while ((s = UPLOADS.poll()) != null) {
            try {
                // try to call common upload method names
                boolean done = ReflectionUtils.tryInvokeAny(s, "upload", "uploadMesh", "uploadMeshes", "finishUpload", "finishRebuild");
                if (!done) ReflectionUtils.tryInvoke(s, "rebuild");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}

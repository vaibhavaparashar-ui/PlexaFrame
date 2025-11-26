package com.vaibhavparashar.plexaframe.thread;

import java.lang.reflect.Method;

/**
 * Worker that pulls intercepted Sodium RenderSections, optionally culls by distance,
 * does CPU-only mesh work, then requests a main-thread upload.
 */
public final class SodiumRebuildWorker implements Runnable {

    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            Object section = SodiumRebuildScheduler.poll();
            if (section == null) {
                try { Thread.sleep(2); } catch (InterruptedException ignored) {}
                continue;
            }

            try {
                // distance culling (safe reflection)
                if (FrustumDistanceChecker.isFar(section, 160)) {
                    continue;
                }

                // CPU-only meshing placeholder (replace with real algorithm)
                Object meshData = performCpuMeshing(section);

                // schedule upload on main thread (use invoker/accessor there if needed)
                Object finalSection = section;
                PlexaFrameThreadPoolManager.postToMainThread(() -> {
                    try {
                        // Try a few common method names for Sodium to finalize/upload the mesh
                        boolean uploaded = ReflectionUtils.tryInvokeAny(finalSection,
                                "upload", "uploadMesh", "uploadMeshes", "finishUpload", "finishRebuild");
                        if (!uploaded) {
                            // last resort: try calling 'rebuild' (safe if present and expected)
                            ReflectionUtils.tryInvoke(finalSection, "rebuild");
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                });

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void shutdown() { running = false; }

    private Object performCpuMeshing(Object section) {
        // TODO: implement real mesh generator using accessors to read section data
        try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        return new Object();
    }
}

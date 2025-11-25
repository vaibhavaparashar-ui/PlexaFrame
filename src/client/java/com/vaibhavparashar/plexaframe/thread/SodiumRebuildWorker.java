package com.vaibhavparashar.plexaframe.thread;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;

public class SodiumRebuildWorker extends Thread {

    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            RenderSection section = SodiumRebuildScheduler.poll();
            if (section != null) {

                if (!FrustumChecker.isVisible(section)) continue;
                if (!FrustumChecker.withinDistance(section, 150)) continue;

                section.rebuild();
                RenderUploadQueue.enqueue(section);

            } else {
                try { Thread.sleep(1); } catch (InterruptedException ignored) {}
            }
        }
    }

    public void shutdown() {
        running = false;
    }
}

package com.vaibhavparashar.plexaframe.handler;

import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import com.vaibhavparashar.plexaframe.thread.PlexaFrameThreadPoolManager;
import com.vaibhavparashar.plexaframe.thread.RenderUploadQueue;
import com.vaibhavparashar.plexaframe.thread.ReflectionUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.minecraft.client.Minecraft;

public final class ChunkPreloadHandler {

    public static void register() {
        ClientChunkEvents.CHUNK_LOAD.register((client, chunk) -> {
            var cfg = PlexaConfig.instance();
            if (!cfg.enableAsyncPreload) return;

            // do lightweight distance check
            double px = client.player.getX();
            double pz = client.player.getZ();
            double dx = px - chunk.getPos().getMinBlockX();
            double dz = pz - chunk.getPos().getMinBlockZ();
            if (dx*dx + dz*dz > (cfg.preloadDistance * cfg.preloadDistance)) return;

            // Submit a safe rebuild task: use your RebuildTask or a small wrapper
            PlexaFrameThreadPoolManager.submitSafe(() -> {
                try {
                    // Use reflection to try a common method that marks the chunk for rebuild;
                    ReflectionUtils.tryInvokeAny(chunk, "setNeedsRebuild", "markDirty", "markSectionDirty");
                    // If chunk builder exists in client, schedule mesh build via your RebuildTask queue:
                    // com.vaibhavparashar.plexaframe.thread.SodiumRebuildScheduler.submit(section) etc.
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }
}

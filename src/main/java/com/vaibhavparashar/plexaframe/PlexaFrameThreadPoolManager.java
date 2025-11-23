package com.vaibhavparashar.plexaframe.thread;

import com.vaibhavparashar.plexaframe.mixin.accessor.RenderSectionAccessor;
import com.vaibhavparashar.plexaframe.mixin.accessor.RenderSectionInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PlexaFrameThreadPoolManager {

    private static final ExecutorService EXECUTOR =
        Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors() - 1));

    public static void submitRebuildTask(Object sectionObj) {
        EXECUTOR.submit(() -> {
            try {
                RenderSectionAccessor accessor = (RenderSectionAccessor)sectionObj;
                BlockPos pos = accessor.plexaframe$getOrigin();

                // Dummy expensive rebuild simulation: replace with real meshing
                Thread.sleep(2);

                // Back on main thread: upload GPU buffers
                Minecraft.getInstance().execute(() -> {
                    ((RenderSectionInvoker)sectionObj).plexaframe$invokeBeginLayerCompile();
                });

            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

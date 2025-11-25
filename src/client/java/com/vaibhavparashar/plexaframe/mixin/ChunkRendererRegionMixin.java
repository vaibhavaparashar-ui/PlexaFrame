package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.thread.PlexaFrameThreadPoolManager;
import com.vaibhavparashar.plexaframe.thread.RebuildTask;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher.RenderSection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Replaces vanilla rebuild scheduling. If Sodium is installed, redirect into Sodium dispatcher.
 * Else fall back to PlexaFrame thread pool.
 */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public abstract class ChunkRendererRegionMixin {

    @Inject(method = "rebuild", at = @At("HEAD"), cancellable = true)
    private void plexa$overrideRebuild(CallbackInfoReturnable<Object> cir) {
        RenderSection section = (RenderSection) (Object) this;
        cir.cancel();

        if (FabricLoader.getInstance().isModLoaded("sodium")) {
            // Call sodium async dispatcher
            try {
                com.jomlrender.sodium.render.chunk.compile.ChunkCompileTaskDispatcher.INSTANCE.schedule(section);
                return;
            } catch (Throwable t) {
                System.err.println("[PlexaFrame] Failed to use Sodium dispatcher, falling back.");
            }
        }

        // No sodium or failed: use PlexaFrame scheduler
        PlexaFrameThreadPoolManager.submitSafe(new RebuildTask(section, section.hashCode()));
    }
}
    
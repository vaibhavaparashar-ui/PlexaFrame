package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.thread.PlexaFrameThreadPoolManager;
import com.vaibhavparashar.plexaframe.thread.RebuildTask;
import net.fabricmc.loader.api.FabricLoader;

// Vanilla imports
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher.RenderSection;

// Sodium imports – adjust if class path differs
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;              // placeholder
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildTaskDispatcher;  // placeholder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SectionRenderDispatcher.RenderSection.class)
public abstract class ChunkRendererRegionMixin {

    @Inject(method = "rebuild", at = @At("HEAD"), cancellable = true)
    private void plexaframe$overrideRebuild(CallbackInfoReturnable<Object> cir) {
        RenderSection section = (RenderSection)(Object)this;
        cir.cancel();

        if (FabricLoader.getInstance().isModLoaded("sodium")) {
            try {
                // Adjust method call depending on Sodium version
                ChunkBuildTaskDispatcher.INSTANCE.schedule((ChunkRenderContainer) section);
                return;
            } catch (Throwable t) {
                System.err.println("[PlexaFrame] Sodium dispatch failed, falling back.");
            }
        }

        PlexaFrameThreadPoolManager.submitSafe(new RebuildTask(section, section.hashCode()));
    }
}

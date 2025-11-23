package com.vaibhavparashar.plexaframe.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SectionRenderDispatcher.RenderSection.class)
public abstract class ChunkRendererRegionMixin {

    @Inject(method = "rebuild", at = @At("HEAD"), cancellable = true)
    private void plexaframe$redirectToThreadPool(CallbackInfoReturnable<Object> cir) {

        if (FabricLoader.getInstance().isModLoaded("sodium")) {
            return; // Sodium controls rebuilds → do not interfere
        }

        cir.cancel(); // cancel vanilla rebuild

        Object section = this;
        com.vaibhavparashar.plexaframe.thread.PlexaFrameThreadPoolManager.submitRebuildTask(section);
    }
}

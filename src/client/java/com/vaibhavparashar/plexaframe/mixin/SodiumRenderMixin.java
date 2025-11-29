package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.PlexaFrameX;
import com.vaibhavparashar.plexaframe.thread.SodiumTaskQueue;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultChunkRenderer.class)
public class SodiumRenderMixin {
    private static final SodiumTaskQueue<RenderSection> QUEUE = new SodiumTaskQueue<>();

    @Inject(method = "rebuildSection", at = @At("HEAD"), cancellable = true)
    private void plexa$moveTask(RenderSection section, boolean important, CallbackInfo ci) {
        if (!PlexaFrameX.sodiumLoaded) {
            return; // skip if no sodium
        }

        QUEUE.submit(section);
        ci.cancel();
    }
}

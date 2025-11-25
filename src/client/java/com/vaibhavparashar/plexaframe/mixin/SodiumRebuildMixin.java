package com.vaibhavparashar.plexaframe.mixin;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultChunkRenderer.class)
public class SodiumRenderMixin {

    @Inject(method = "rebuildSection", at = @At("HEAD"), cancellable = true)
    private void plexa$redirectRebuild(RenderSection section, boolean important, CallbackInfo ci) {
        // TODO: send to custom thread here
        // SodiumRebuildScheduler.submit(section);

        System.out.println("[PlexaFrame] Intercepted rebuild: " + section.getPosition());

        // cancel to prevent sodium default
        // ci.cancel();   // enable later when queue exists
    }
}

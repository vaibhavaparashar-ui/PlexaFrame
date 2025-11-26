package com.vaibhavparashar.plexaframe.mixin.sodium;

import com.vaibhavparashar.plexaframe.thread.SodiumRebuildScheduler;
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

        System.out.println("[PlexaFrame] Threaded rebuild -> " + section.getPosition());
        SodiumRebuildScheduler.submit(section);

        ci.cancel(); // prevent default thread on sodium
    }
}

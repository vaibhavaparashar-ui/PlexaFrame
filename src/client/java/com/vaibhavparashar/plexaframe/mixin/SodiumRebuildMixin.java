package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.thread.SodiumRebuildScheduler;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildTaskDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkBuildTaskDispatcher.class)
public class SodiumRebuildMixin {

    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private void plexa$interceptSubmit(ChunkRenderContainer container, CallbackInfo ci) {
        SodiumRebuildScheduler.submit(container);
        ci.cancel(); // prevent Sodium from immediately rebuilding
    }
}

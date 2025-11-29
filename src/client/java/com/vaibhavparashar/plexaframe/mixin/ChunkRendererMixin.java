package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.FrustumChecker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRendererRegion.class)
public class ChunkRendererMixin {
    @Inject(method = "markForRebuild", at = @At("HEAD"), cancellable = true)
    private void plexa$skipFarChunks(BlockPos pos, CallbackInfo ci) {
        if (FrustumChecker.shouldSkip(pos)) {
            ci.cancel();
        }
    }
}

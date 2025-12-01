package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.config.PlexaConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.render.MultiBufferSource;
import net.minecraft.client.render.LevelRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts expensive leaf rendering on the client side.
 * This mixin lives in the client source set so it can refer to client-only classes.
 */
@Mixin(LevelRenderer.class)
public class LeavesMixin {

    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    private void plexaframe$maybeSkipFancyLeaves(BlockPos pos,
                                                 BlockState state,
                                                 MultiBufferSource buffers,
                                                 LevelRenderer worldRenderer, CallbackInfo ci) {
        if (!PlexaConfig.DISABLE_FANCY_LEAVES) return;

        // Very conservative: only skip if the block is a leaf (this needs mapping check).
        try {
            String name = state.getBlock().getDescriptionId();
            if (name != null && name.toLowerCase().contains("leaf")) {
                ci.cancel();
            }
        } catch (Throwable t) {
            // fallback allow rendering
        }
    }
}

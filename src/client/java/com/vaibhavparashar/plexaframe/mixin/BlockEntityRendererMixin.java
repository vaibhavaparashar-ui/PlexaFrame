package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.frustum.FrustumChecker;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderer.class)
public class BlockEntityRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void plexa$skipFar(BlockEntity blockEntity, float tickDelta, CallbackInfo ci) {
        if (FrustumChecker.shouldSkip(blockEntity.getBlockPos())) ci.cancel();
    }
}

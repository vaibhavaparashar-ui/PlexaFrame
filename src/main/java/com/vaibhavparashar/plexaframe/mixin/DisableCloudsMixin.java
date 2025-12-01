package com.vaibhavparashar.plexaframe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.debug.SkyLightDebugRenderer;

@Mixin(SkyLightDebugRenderer.class)
public class DisableCloudsMixin {
    @Redirect(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderClouds(F)V"))
    private void cancelCloudsRender(Object instance, float tickDelta) {
        // Cancel cloud rendering completely
    }
}

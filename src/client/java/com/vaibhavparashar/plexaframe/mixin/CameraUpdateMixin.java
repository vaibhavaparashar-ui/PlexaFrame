package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.frustum.FrustumChecker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class CameraUpdateMixin {

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void plexa$updateFrustum(float tickDelta, long limitTime, PoseStack poseStack, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;
        Camera camera = mc.gameRenderer.getMainCamera();
        Frustum fr = camera.getFrustum();
        FrustumChecker.update(fr);
    }
}
    
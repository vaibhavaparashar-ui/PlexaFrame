package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.FrustumChecker;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.Frustum;

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(method = "update", at = @At("TAIL"))
    private void plexa$updateFrustum(CallbackInfo ci) {
        Frustum fr = ((Camera) (Object) this).getSubmergedFluidState() != null ? null : null;
        // update frustum safely
    }
}

package com.vaibhavparashar.plexaframe.mixin;

import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class DisableParticlesMixin {
    @Inject(method = "addParticle*", at = @At("HEAD"), cancellable = true)
    private void cancelParticles(CallbackInfo ci) {
        ci.cancel(); // Removes all particles → massive FPS boost!
    }
}

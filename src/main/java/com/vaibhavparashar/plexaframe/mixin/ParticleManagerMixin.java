package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Targets the client particle manager and cancels particle spawns when enabled.
 *
 * If compilation complains about the method signature, check your mappings and replace
 * the injected method name with the mapped name. Common names: "addParticle" or "addEmitter" etc.
 */
@Mixin(targets = "net.minecraft.client.particle.ParticleManager")
public class ParticleManagerMixin {

    // This is a broad signature — it may match many mappings. If compile fails,
    // change the method name to the mapped method in your environment.
    @Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
    private void plexaframe$onAddParticle(Object particleEffect, double x, double y, double z, double dx, double dy, double dz, CallbackInfoReturnable<?> cir) {
        if (!PlexaConfig.DISABLE_PARTICLES) return;

        // Cancel most particles. If the target method returns an instance, returning null cancels creation.
        cir.setReturnValue(null);
    }
}

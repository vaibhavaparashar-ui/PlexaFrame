package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", 
            at = @At("HEAD"), cancellable = true)
    private void plexaframe$blockParticles(ParticleEffect effect, double x, double y, double z,
                                           double dx, double dy, double dz,
                                           CallbackInfoReturnable<?> cir) {
        if (PlexaConfig.REDUCE_PARTICLES) {
            cir.setReturnValue(null);
        }
    }
}

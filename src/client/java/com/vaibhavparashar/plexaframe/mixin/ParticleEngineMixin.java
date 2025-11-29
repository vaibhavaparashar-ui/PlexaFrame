package com.vaibhavparashar.plexaframe.mixin;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin targeting the ParticleEngine to aggressively suppress most particle spawning,
 * while ensuring critical hit particles remain visible for gameplay feedback.
 */
@Mixin(ParticleManager.class)
public class ParticleEngineMixin {

    // Target the core method that adds a particle to the world.
    @Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
    private void plexaframe$suppressParticles(ParticleEffect parameters, double x, double y, double z, double vx, double vy, double vz, CallbackInfoReturnable<Particle> cir) {
        
        // --- Particle Suppression Logic (Excluding Crit) ---
        
        // 1. We don't have an `alwaysSpawn` boolean here — continue and allow critical types.
        
        // 2. Check for the specific Critical Hit particle type.
        // The game uses two main types for critical hits: CRIT and ENCHANTED_HIT.
        if (parameters.getType() == ParticleTypes.CRIT || parameters.getType() == ParticleTypes.ENCHANTED_HIT) {
            return; // Allow the particle to spawn, as it provides essential combat feedback.
        }
        
        // 3. Suppress all other non-essential, decorative particles.
        // If the particle is not marked 'alwaysSpawn' and is not a critical hit type, cancel it.
        // Prevent this particle from spawning by returning null.
        cir.setReturnValue(null);
    }
}
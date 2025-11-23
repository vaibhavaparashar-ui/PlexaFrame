package com.vaibhavparashar.plexaframe.mixin;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes; // <-- NEW IMPORT
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin targeting the ParticleEngine to aggressively suppress most particle spawning,
 * while ensuring critical hit particles remain visible for gameplay feedback.
 */
@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    // Target the core method that adds a particle to the world.
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void plexaframe$suppressParticles(ParticleOptions parameters, boolean alwaysSpawn, CallbackInfo ci) {
        
        // --- Particle Suppression Logic (Excluding Crit) ---
        
        // 1. Check if the particle is marked as critical (alwaysSpawn = true).
        if (alwaysSpawn) {
            return; // Allow the particle to spawn if it's considered critical/essential by the game.
        }
        
        // 2. Check for the specific Critical Hit particle type.
        // The game uses two main types for critical hits: CRIT and ENCHANTED_HIT.
        if (parameters.getType() == ParticleTypes.CRIT || parameters.getType() == ParticleTypes.ENCHANTED_HIT) {
            return; // Allow the particle to spawn, as it provides essential combat feedback.
        }
        
        // 3. Suppress all other non-essential, decorative particles.
        // If the particle is not marked 'alwaysSpawn' and is not a critical hit type, cancel it.
        ci.cancel();
    }
}
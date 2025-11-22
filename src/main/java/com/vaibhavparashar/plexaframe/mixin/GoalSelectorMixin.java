package com.vaibhavparashar.plexaframe.mixin;

import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Mixin targeting GoalSelector to implement AI throttling for improved CPU performance.
 * Reduces the frequency of expensive AI checks for entities far from the player.
 */
@Mixin(GoalSelector.class)
public abstract class GoalSelectorMixin {

    // 1. Shadow the field that holds the entity. 
    // This allows us to access the entity instance inside the GoalSelector, 
    // which is critical for checking its distance and type.
    @Shadow(aliases = {"this$0"}, remap = false)
    private Entity entity; 

    // 2. Define the throttling factor. 
    // An AI will only tick if the world time (tick count) is a multiple of this number.
    private static final int AI_THROTTLE_FACTOR = 5; // Skip 4 out of 5 ticks (80% reduction)

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void plexaframe$throttleDistantGoals(CallbackInfo ci) {
        // --- 1. Check if the entity is null or outside the world ---
        if (entity == null || entity.level().isClientSide() && entity.level().getGameTime() < 20) {
            return;
        }

        // --- 2. Implement distance check (Only throttle far-away entities) ---
        // We reuse the aggressive culling distance logic from WorldRendererMixin.
        final double AGGRESSIVE_CULLING_DISTANCE = 64.0; 
        final double CULLING_DISTANCE_SQ = AGGRESSIVE_CULLING_DISTANCE * AGGRESSIVE_CULLING_DISTANCE;
        
        // Get the entity's distance from the camera/player.
        double distanceSq = entity.position().distanceToSqr(entity.level().getProfiler().getRequestedEntityTarget().position());

        // If the entity is too close to the player, do NOT throttle its AI.
        // It must run every tick for responsive combat/interaction.
        if (distanceSq < CULLING_DISTANCE_SQ) {
            return; 
        }

        // --- 3. Apply the Throttle ---
        // Check if the current game time (tick count) is NOT a multiple of the throttle factor.
        if (entity.level().getGameTime() % AI_THROTTLE_FACTOR != 0) {
            // If it's not the designated tick (e.g., ticks 1, 2, 3, 4, 6, 7, 8, 9...), 
            // cancel the AI update for this entity.
            ci.cancel(); 
        }
    }
}
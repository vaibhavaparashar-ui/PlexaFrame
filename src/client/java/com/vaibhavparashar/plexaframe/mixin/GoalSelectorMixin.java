package com.vaibhavparashar.plexaframe.mixin;

import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// CLIENT ONLY IMPORT
import net.minecraft.client.Minecraft;

@Mixin(GoalSelector.class)
public abstract class GoalSelectorMixin {

    @Shadow(remap = true)
    private Entity entity;

    private static final int AI_THROTTLE_FACTOR = 5;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void plexaframe$throttleDistantGoals(CallbackInfo ci) {

        if (entity == null || entity.level() == null) {
            return;
        }

        Vec3 targetPos = null;

        // ----- CLIENT-ONLY SECTION -----
        if (entity.level().isClientSide()) {
            targetPos = plexaframe$getClientPlayerPos();
        }

        if (targetPos != null) {
            final double CULLING_DISTANCE = 64.0;
            final double CULLING_DISTANCE_SQ = CULLING_DISTANCE * CULLING_DISTANCE;

            double distanceSq = entity.position().distanceToSqr(targetPos);

            if (distanceSq < CULLING_DISTANCE_SQ) {
                return;
            }
        }

        long worldTime = entity.level().getGameTime();
        if (worldTime % AI_THROTTLE_FACTOR != 0) {
            ci.cancel();
        }
    }

    @Environment(EnvType.CLIENT)
    private Vec3 plexaframe$getClientPlayerPos() {
        if (Minecraft.getInstance().player != null) {
            return Minecraft.getInstance().player.position();
        }
        return null;
    }
}

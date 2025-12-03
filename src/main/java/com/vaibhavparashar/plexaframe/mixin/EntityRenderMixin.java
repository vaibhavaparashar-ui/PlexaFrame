package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Intercept entity render checks and prevent rendering extremely distant entities.
 * If compile-errors occur due to the method name, change "shouldRender" to the mapped name in your environment.
 */
@Mixin(targets = "net.minecraft.client.render.entity.EntityRenderer")
public class EntityRenderMixin {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void plexaframe$shouldRender(Entity entity, Object frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (!PlexaConfig.AGGRESSIVE_ENTITY_CULLING) return;

        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc == null || mc.player == null) return;

            double dx = entity.getX() - mc.player.getX();
            double dz = entity.getZ() - mc.player.getZ();
            double dy = entity.getY() - mc.player.getY();
            double distSq = dx*dx + dy*dy + dz*dz;
            double limit = (double) PlexaConfig.ENTITY_CULL_DISTANCE_BLOCKS;
            if (distSq > limit * limit) {
                cir.setReturnValue(false);
            }
        } catch (Throwable t) {
            // on error, do nothing (don't break rendering)
        }
    }
}

package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderMixin {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void plexaframe$shouldRender(Entity entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (!PlexaConfig.ENABLE_ENTITY_CULLING) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        double dx = entity.getX() - mc.player.getX();
        double dy = entity.getY() - mc.player.getY();
        double dz = entity.getZ() - mc.player.getZ();
        double distSq = dx * dx + dy * dy + dz * dz;

        int d = PlexaConfig.ENTITY_CULL_DISTANCE_BLOCKS;
        if (distSq > (double) d * d) {
            cir.setReturnValue(false);
        }
    }
}

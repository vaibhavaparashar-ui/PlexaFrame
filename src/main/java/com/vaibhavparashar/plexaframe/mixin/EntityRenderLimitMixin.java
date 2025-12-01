package com.vaibhavparashar.plexaframe.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderLimitMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void noFarEntityRender(Entity entity, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (entity.distanceTo(entity) > 35) { // reduce distance
            cir.setReturnValue(false);
        }
    }
}

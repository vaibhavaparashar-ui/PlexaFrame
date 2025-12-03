package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Cancel or shorten death tick to avoid expensive animations.
 * If your mapping uses a different method name for death tick, replace "tickDeath" below.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tickDeath", at = @At("HEAD"), cancellable = true)
    private void plexaframe$skipDeathAnimation(CallbackInfo ci) {
        if (!PlexaConfig.SKIP_DEATH_ANIMATION) return;
        ci.cancel();
    }
}

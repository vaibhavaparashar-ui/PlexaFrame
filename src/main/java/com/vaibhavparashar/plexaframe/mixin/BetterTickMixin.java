package com.vaibhavparashar.plexaframe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.world.tick.TickManager.class)
public class BetterTickMixin {
    @Redirect(method = "getTickRate", at = @At(value = "FIELD", target = "Lnet/minecraft/server/tick/TickManager;tickRate:I"))
    private int boostTick() {
        return 40; // Speed up ticking --> chunk updates faster
    }
}

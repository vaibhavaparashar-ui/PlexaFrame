package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Cancels cloud rendering by intercepting the cloud rendering call.
 * The method names vary by mapping; here we assume the cloud rendering method exists in LevelRenderer.
 */
@Mixin(WorldRenderer.class)
public class CloudRendererMixin {

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void plexaframe$disableClouds(CallbackInfo ci) {
        if (PlexaConfig.DISABLE_CLOUDS) {
            ci.cancel();
        }
    }
}

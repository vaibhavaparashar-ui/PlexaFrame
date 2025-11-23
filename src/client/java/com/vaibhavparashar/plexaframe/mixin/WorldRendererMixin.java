package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.frustum.FrustumChecker;
import com.vaibhavparashar.plexaframe.thread.RebuildScheduler;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.RenderSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "scheduleRebuild", at = @At("HEAD"), cancellable = true)
    public void redirectRebuild(RenderSection section, CallbackInfo ci) {

        if (FrustumChecker.shouldSkip(section.getOrigin())) {
            ci.cancel();
            return;
        }

        long hash = section.hashCode();
        RebuildScheduler.enqueue(section, hash);
        ci.cancel();
    }
}

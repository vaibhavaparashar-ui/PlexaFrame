package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.frustum.FrustumChecker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "scheduleRebuild", at = @At("HEAD"), cancellable = true)
    private void plexa$skipRebuildIfNotVisible(SectionRenderDispatcher.RenderSection section, CallbackInfo ci) {

        Vec3 origin = section.getOrigin(); // fixed mapping for 1.21.1

        if (FrustumChecker.shouldSkip(origin)) {
            ci.cancel();
        }
    }
}

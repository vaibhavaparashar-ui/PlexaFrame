package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.frustum.FrustumChecker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "setSectionDirty", at = @At("HEAD"), cancellable = true)
    private void plexa$skipRebuildIfNotVisible(SectionRenderDispatcher.RenderSection section, CallbackInfo ci) {

        // Correct getter name for 1.21.1 Fabric mappings
        var origin = section.getSectionPos().origin(); 

        // --- Skip if frustum says not visible ---
        if (FrustumChecker.shouldSkip(origin)) {
            ci.cancel();
            return;
        }

        // --- Distance-based skip to reduce rebuild spam ---
        double maxDistance = 160 * 160; // adjustable
        double dist = origin.distToCenterSqr(0, 0, 0);

        if (dist > maxDistance) {
            ci.cancel();
        }
    }
}

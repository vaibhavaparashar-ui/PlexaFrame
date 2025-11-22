package com.vaibhavparashar.plexaframe.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin targeting LevelRenderer (WorldRenderer) to implement multiple aggressive rendering optimizations:
 * 1. Aggressive Entity Culling
 * 2. Cloud Suppression
 * 3. Weather (Rain/Snow) Suppression
 */
@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    // --- 1. Chunk Rendering Loop (Optimization Target) ---
    // This method is primarily conceptual for finding the right injection point
    // but remains here as a placeholder for potential future chunk optimizations.
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;setupRender(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/culling/Frustum;ZZ)V", shift = At.Shift.AFTER))
    private void plexaframe$applyOptimizedFrustum(
            PoseStack poseStack,
            float tickDelta,
            long limitTime,
            boolean renderBlockOutline,
            Frustum frustum,
            RenderBuffers renderBuffers,
            CallbackInfo ci
        ) {
        // Optimization Logic (currently conceptual, placeholder for future frustum modification)
    }
    
    // --- 2. Aggressive Entity Culling ---
    // Targets the method that processes the list of visible entities for rendering.
    @Inject(
        method = "renderEntity", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private void plexaframe$aggressivelyCullEntities(
            net.minecraft.world.entity.Entity entity, 
            double x, 
            double y, 
            double z, 
            float tickDelta, 
            PoseStack poseStack, 
            MultiBufferSource bufferSource, 
            CallbackInfo ci
        ) {
        // Cast the Mixin object to the target class to access Minecraft fields
        LevelRenderer self = (LevelRenderer)(Object)this;
        // Get the current camera position
        Vec3 cameraPos = self.minecraft.gameRenderer.getMainCamera().getPosition();
        
        // Max distance in blocks for aggressive culling
        final double AGGRESSIVE_CULLING_DISTANCE = 64.0;
        final double CULLING_DISTANCE_SQ = AGGRESSIVE_CULLING_DISTANCE * AGGRESSIVE_CULLING_DISTANCE;
        
        // Calculate the squared distance between the entity and the camera
        double distanceSq = entity.position().distanceToSqr(cameraPos);
        
        // If the entity is too far away, cancel the rendering call.
        if (distanceSq > CULLING_DISTANCE_SQ) {
            ci.cancel(); 
        }
    }

    // --- 3. Cloud Rendering Suppression ---
    // Clouds are often a major source of lag. This aggressively suppresses them.
    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void plexaframe$suppressClouds(PoseStack matrices, float tickDelta, MultiBufferSource bufferSource, double camX, double camY, double camZ, CallbackInfo ci) {
        // For a simple, aggressive performance boost, we cancel the cloud render call.
        ci.cancel(); 
    }

    // --- 4. Weather Rendering Suppression ---
    // Weather effects involve many particle/texture draw calls.
    @Inject(method = "renderSnowAndRain", at = @At("HEAD"), cancellable = true)
    private void plexaframe$suppressWeather(LightTexture lightTexture, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        // This optimization stops the logic that spawns and renders all rain/snow particles.
        ci.cancel(); 
    }
}
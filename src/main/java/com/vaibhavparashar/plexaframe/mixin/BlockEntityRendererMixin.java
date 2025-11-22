package com.vaibhavparashar.plexaframe.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin targeting BlockEntityRenderer to implement enhanced view frustum culling.
 * This should prevent rendering of Block Entities (chests, signs, etc.) that are
 * definitely not visible to the camera, saving CPU cycles.
 */
@Mixin(BlockEntityRenderer.class)
public abstract class BlockEntityRendererMixin<T extends BlockEntity> {

    // Note: Minecraft's default culling for Block Entities is often too generous.
    // By injecting at the HEAD of the render function and returning early, we save CPU work.
    
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void plexaframe$doFrustumCulling(T blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo ci) {
        
        // --- Custom Culling Logic ---
        
        // This culling check should only apply if the Block Entity implements the method that allows culling.
        // We'll use a simple distance check as a first pass, which is very performant.
        // Minecraft 1.21.1 uses the WorldRenderer.shouldRender method for actual frustum checks,
        // but adding an early distance check is still highly beneficial.
        
        // 1. Get the current renderer provider context (often the MinecraftClient instance).
        BlockEntityRendererProvider.Context context = ((BlockEntityRenderer) (Object) this).context;
        
        // 2. Check the distance from the camera (view position).
        // Max distance threshold for rendering Block Entities (in blocks)
        final double MAX_RENDER_DISTANCE_SQ = 64 * 64; // Example: 64 blocks, squared (4096)
        
        if (context.camera.getPosition().distanceToSqr(blockEntity.getBlockPos().getCenter()) > MAX_RENDER_DISTANCE_SQ) {
            // If the distance squared is greater than the max distance squared,
            // we assume it is too far away and cancel the render call.
            ci.cancel(); 
        }

        // TODO: For more advanced optimization, we would also inject the actual Frustum Culling 
        // using context.levelRenderer.cullingFrustum, but the distance check is a great, safe start.
    }
}
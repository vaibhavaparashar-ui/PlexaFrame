package com.vaibhavparashar.plexaframe.mixin;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlasSprite.class) 
public abstract class TextureAtlasSpriteMixin {
    
    @Shadow public abstract ResourceLocation atlasLocation(); // Allows us to get the texture path

    // Target the method that computes and uploads the next frame of an animated texture (like fluids).
    @Inject(method = "upload", at = @At("HEAD"), cancellable = true)
    private void plexaframe$throttleFluidAndDistantTextures(CallbackInfo ci) {
        
        ResourceLocation id = this.atlasLocation();
        
        // --- Fluid Animation Suppression ---
        // Check if the current texture being uploaded is a water or lava texture.
        // We look for textures in the 'block' atlas that start with 'water' or 'lava'.
        if (id.getNamespace().equals("minecraft") && id.getPath().startsWith("block/")) {
            String path = id.getPath();
            
            // Aggressive Check: If it's a fluid texture, only update it every 5 ticks (80% reduction)
            if (path.contains("water_still") || path.contains("water_flow") || 
                path.contains("lava_still") || path.contains("lava_flow")) {
                
                // Get the current game time (Tick count). This requires access to the client/world.
                // We use the common utility access for conceptual accuracy.
                long gameTime = net.minecraft.client.Minecraft.getInstance().level.getGameTime();
                
                if (gameTime % 5 != 0) {
                    ci.cancel(); // Skip the update 4 out of 5 ticks
                    return; // Stop processing this method
                }
            }
        }

        // (Original Logic for Feature 9: General Texture Throttling)
        // The previous conceptual throttling logic can remain here or be refined.
    }
}
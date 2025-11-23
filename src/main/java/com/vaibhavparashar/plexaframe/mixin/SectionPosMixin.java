package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.BlockPosPoolManager; // Correctly imports the utility class
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin targeting SectionPos to reduce BlockPos memory allocations.
 * Uses BlockPosPoolManager to stabilize FPS by reducing Garbage Collection load.
 * This is a fundamental optimization against common GC lag spikes.
 */
@Mixin(SectionPos.class)
public class SectionPosMixin {

    // Redirect the call to 'new BlockPos(x, y, z)' to our pooling method.
    @Redirect(
        method = "toBlockPos(III)Lnet/minecraft/core/BlockPos;",
        at = @At(value = "NEW", target = "Lnet/minecraft/core/BlockPos;")
    )
    private static BlockPos plexaframe$reduceBlockPosAllocation(int x, int y, int z) {
        
        // --- The functional optimization for GC lag spikes ---
        // Instead of letting Minecraft allocate new memory via 'new BlockPos(x, y, z)',
        // we call the utility manager to get a REUSED, thread-local BlockPos object.
        
        return BlockPosPoolManager.getOrCreate(x, y, z); 
    }
}
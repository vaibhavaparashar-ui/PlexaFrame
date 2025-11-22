package com.vaibhavparashar.plexaframe;

import net.minecraft.core.BlockPos;

/**
 * Manages thread-local reusable mutable BlockPos instances to reduce memory allocation.
 * Used by SectionPosMixin to intercept 'new BlockPos()' calls and substitute them
 * with reused objects, thus reducing Garbage Collection pressure.
 */
public class BlockPosPoolManager {

    /**
     * A ThreadLocal holding a mutable BlockPos instance for each thread.
     * BlockPos.MutableBlockPos allows us to change the coordinates of the same object,
     * reusing it across many calls without allocating new memory each time.
     */
    private static final ThreadLocal<BlockPos.MutableBlockPos> POS_POOL = ThreadLocal.withInitial(
        () -> new BlockPos.MutableBlockPos(0, 0, 0)
    );

    /**
     * Gets the reusable BlockPos.MutableBlockPos instance associated with the current thread.
     * This object can be safely modified and returned without fear of conflicting 
     * with other threads or causing new memory allocations.
     * * @return The thread-local mutable position object.
     */
    public static BlockPos.MutableBlockPos getMutablePos() {
        return POS_POOL.get();
    }

    /**
     * This method provides the full logic for the Mixin to redirect to.
     * It handles the reuse and setting of coordinates.
     * * @param x The x-coordinate for the BlockPos.
     * @param y The y-coordinate for the BlockPos.
     * @param z The z-coordinate for the BlockPos.
     * @return The reusable BlockPos object with updated coordinates.
     */
    public static BlockPos.MutableBlockPos getOrCreate(int x, int y, int z) {
        return getMutablePos().set(x, y, z);
    }
}
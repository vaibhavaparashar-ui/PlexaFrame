package com.vaibhavparashar.plexaframe.frustum;

import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.culling.Frustum;

public class FrustumChecker {

    public static Frustum currentFrustum;
    public static double cameraX, cameraY, cameraZ;

    public static void updateFrustum(Frustum frustum) {
        currentFrustum = frustum;
    }

    public static boolean shouldSkip(BlockPos pos) {
        if (currentFrustum == null) return false;

        // Build AABB using explicit coordinates — mappings require doubles rather than BlockPos
        double x1 = pos.getX();
        double y1 = pos.getY();
        double z1 = pos.getZ();
        AABB box = new AABB(x1, y1, z1, x1 + 16.0, y1 + 16.0, z1 + 16.0);
        return !currentFrustum.isVisible(box);
    }
}

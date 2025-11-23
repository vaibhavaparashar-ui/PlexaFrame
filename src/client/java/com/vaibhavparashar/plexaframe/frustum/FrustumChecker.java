package com.vaibhavparashar.plexaframe.frustum;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;

public class FrustumChecker {

    private static final Minecraft MC = Minecraft.getInstance();
    private static Frustum frustum;

    // Max render distance for multithreaded meshing intensive rebuilds
    private static final double MAX_DIST_SQ = 120 * 120;

    public static void update(Frustum currentFrustum) {
        frustum = currentFrustum;
    }

    public static boolean shouldSkip(BlockPos pos) {
        if (frustum != null && !frustum.isVisible(pos)) {
            return true;
        }

        double distSq = MC.player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
        return distSq > MAX_DIST_SQ;
    }
}

package com.vaibhavparashar.plexaframe.frustum;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

/** Lightweight frustum + distance checker. */
public final class FrustumChecker {
    private static Frustum current = null;
    private static final double MAX_DIST_SQ = 160 * 160;

    public static void update(Frustum fr) { current = fr; }

    public static boolean shouldSkip(BlockPos pos) {
        if (current != null) {
            AABB box = new AABB(
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 16, pos.getY() + 16, pos.getZ() + 16
            );
            if (!current.isVisible(box)) return true;
        }
        var mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) return false;
        double d = mc.player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
        return d > MAX_DIST_SQ;
    }
}

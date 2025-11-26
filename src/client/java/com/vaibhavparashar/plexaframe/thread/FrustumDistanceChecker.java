package com.vaibhavparashar.plexaframe.thread;

import net.minecraft.client.Minecraft;

/**
 * Simple distance-based culling. Uses reflection to get section origin where possible.
 */
public final class FrustumDistanceChecker {

    private FrustumDistanceChecker() {}

    public static boolean isFar(Object section, int maxDistanceBlocks) {
        try {
            // try several common method names to get section coordinates
            double sx = ReflectionUtils.readDouble(section, "getOriginX", "getPosX", "getX", "getPositionX");
            double sy = ReflectionUtils.readDouble(section, "getOriginY", "getPosY", "getY", "getPositionY");
            double sz = ReflectionUtils.readDouble(section, "getOriginZ", "getPosZ", "getZ", "getPositionZ");

            if (Double.isNaN(sx)) return false;

            var mc = Minecraft.getInstance();
            if (mc == null || mc.player == null) return false;
            double px = mc.player.getX();
            double py = mc.player.getY();
            double pz = mc.player.getZ();

            double dx = px - sx;
            double dy = py - sy;
            double dz = pz - sz;
            double distSq = dx*dx + dy*dy + dz*dz;
            return distSq > (double)maxDistanceBlocks * maxDistanceBlocks;
        } catch (Throwable t) {
            // on error, do not skip
            return false;
        }
    }
}

package com.vaibhavparashar.plexaframe.thread;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import net.minecraft.client.Minecraft;

public class FrustumChecker {

    public static boolean withinDistance(RenderSection section, int maxDistance) {
        var cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        double dx = cam.getPosition().x - section.getOriginX();
        double dy = cam.getPosition().y - section.getOriginY();
        double dz = cam.getPosition().z - section.getOriginZ();
        return (dx * dx + dy * dy + dz * dz) < maxDistance * maxDistance;
    }

    public static boolean isVisible(RenderSection section) {
        return section.getRenderBounds().intersects(Minecraft.getInstance().levelRenderer.globalFrustum);
    }
}

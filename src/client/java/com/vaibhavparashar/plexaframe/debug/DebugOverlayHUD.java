package com.vaibhavparashar.plexaframe.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import com.vaibhavparashar.plexaframe.thread.PlexaFrameThreadPoolManager;

public class DebugOverlayHUD {
    public static void render(GuiGraphics gfx, PoseStack ms) {
        var mc = Minecraft.getInstance();
        gfx.drawString(mc.font, "PlexaFrame: Q=" + PlexaFrameThreadPoolManager.getQueueSize() + " A=" + PlexaFrameThreadPoolManager.getActiveCount(), 8, 8, 0xFFFFFF);
    }
}

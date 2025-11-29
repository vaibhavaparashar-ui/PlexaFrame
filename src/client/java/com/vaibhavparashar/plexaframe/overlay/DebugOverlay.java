package com.vaibhavparashar.plexaframe.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class DebugOverlay {
    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int fps = client.getCurrentFps();

        context.drawText(client.textRenderer,
                "PlexaFrameX: " + fps + " FPS", 6, 6, 0x00FF00, true);
    }
}

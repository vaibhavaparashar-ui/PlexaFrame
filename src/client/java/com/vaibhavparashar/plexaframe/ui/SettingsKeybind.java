package com.vaibhavparashar.plexaframe.ui;

import com.vaibhavparashar.plexaframe.PlexaClient;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class SettingsKeybind {
    private static KeyMapping KEY;

    public static void register() {
        KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.plexaframe.settings",
                GLFW.GLFW_KEY_P,
                "category.plexaframe"
        ));
    }

    public static void tick() {
        if (KEY != null && KEY.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null) {
                mc.setScreen(new PlexaSettingsScreen());
            }
        }
    }
}

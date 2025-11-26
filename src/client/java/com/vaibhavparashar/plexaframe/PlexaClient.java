package com.vaibhavparashar.plexaframe;

import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import com.vaibhavparashar.plexaframe.thread.PlexaFrameThreadPoolManager;
import com.vaibhavparashar.plexaframe.ui.SettingsKeybind;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class PlexaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Load config
        PlexaConfig.load();

        // Register keybind to open settings
        SettingsKeybind.register();

        // Tick handler for keybind
        ClientTickEvents.END_CLIENT_TICK.register(client -> SettingsKeybind.tick());

        // Hook to flush uploads (already from previous instructions)
        WorldRenderEvents.END.register(ctx -> com.vaibhavparashar.plexaframe.thread.RenderUploadQueue.flushUploads());

        System.out.println("[PlexaFrame] Client initialized (settings + async preload loaded).");
    }
}

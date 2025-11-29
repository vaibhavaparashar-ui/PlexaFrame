package com.vaibhavparashar.plexaframe.client;

import com.vaibhavparashar.plexaframe.PlexaClient;
import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class PlexaClientTickHandler {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                tick(client);
            } catch (Throwable t) {
                PlexaClient.LOGGER.warn("Exception in PlexaFrame tick handler", t);
            }
        });
    }

    private static void tick(MinecraftClient client) {
        if (PlexaConfig.ENABLE_ENTITY_CULLING && client.player != null) {
            // No heavy work here - mixins do all performance operations now.
        }
    }
}

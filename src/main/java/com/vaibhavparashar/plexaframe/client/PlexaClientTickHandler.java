package com.vaibhavparashar.plexaframe.client;

import com.vaibhavparashar.plexaframe.PlexaClient;
import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient; // <-- FIXED import

public class PlexaClientTickHandler {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                if (client == null) return;
                tick(client);
            } catch (Throwable t) {
                PlexaClient.LOGGER.warn("Exception in PlexaFrame tick handler", t);
            }
        });
    }

    // use MinecraftClient instead of Minecraft
    private static void tick(MinecraftClient client) { 
        if (client.player == null || client.world == null) {
            return;
        }

        if (PlexaConfig.HEAVY_DISABLE_MODE) {
            // optionally implement periodic purging or more aggressive checks
        }
    }
}

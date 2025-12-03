package com.vaibhavparashar.plexaframe.client;

import com.vaibhavparashar.plexaframe.PlexaClient;
import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GraphicsMode;

public class PlexaClientTickHandler {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client == null) return;
            try {
                tick(client);
            } catch (Throwable t) {
                PlexaClient.LOGGER.warn("Exception in PlexaFrame tick handler", t);
            }
        });
    }

    private static void tick(MinecraftClient client) {
        if (!PlexaConfig.FPS_BOOST_ENABLED) return;
        if (client.player == null) return;
        TurboOptimizer.optimize(client);

        try {
            // Clouds
            if (PlexaConfig.DISABLE_CLOUDS) {
                // safe setter via options
                var cloudOpt = client.options.getCloudRenderMode();
                if (cloudOpt != null) cloudOpt.setValue(CloudRenderMode.OFF);
            }

            // Graphics = FAST
            if (PlexaConfig.DISABLE_FANCY_LEAVES || PlexaConfig.HEAVY_DISABLE_MODE) {
                var gfx = client.options.getGraphicsMode();
                if (gfx != null) gfx.setValue(GraphicsMode.FAST);
            }

            // Particle fallback if mixin fails — attempt to reduce particle spawn scale where available.
            // Many modern mappings removed direct particle setting; mixin is main path.

        } catch (Throwable t) {
            PlexaClient.LOGGER.warn("Failed to apply runtime FPS options", t);
        }
    }
}

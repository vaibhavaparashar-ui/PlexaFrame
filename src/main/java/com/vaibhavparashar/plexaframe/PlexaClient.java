package com.vaibhavparashar.plexaframe;

import com.vaibhavparashar.plexaframe.client.PlexaClientTickHandler;
import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class PlexaClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("plexaframe");

    @Override
    public void onInitializeClient() {
        LOGGER.info("PlexaFrame initialized — PlexaBoost (PlexaFrame) active.");
        PlexaConfig.load(); // load defaults (in-memory minimal config)
        PlexaClientTickHandler.register();
    }
    public void onInitialize() {
        System.out.println("PlexaFrame FPS Boost Loaded!");
    }
}

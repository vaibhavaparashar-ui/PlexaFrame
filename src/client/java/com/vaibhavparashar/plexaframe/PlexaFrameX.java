package com.vaibhavparashar.plexaframe;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlexaFrameX implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("PlexaFrameX");
    public static boolean sodiumLoaded = false;

    @Override
    public void onInitializeClient() {
        try {
            Class.forName("me.jellysquid.mods.sodium.client.SodiumClientMod");
            sodiumLoaded = true;
            LOGGER.info("[PlexaFrameX] Sodium detected!");
        } catch (Throwable e) {
            LOGGER.warn("[PlexaFrameX] Sodium not detected — running fallback mode");
        }

        LOGGER.info("[PlexaFrameX] Android Ultra Mode Activated!");
    }
}

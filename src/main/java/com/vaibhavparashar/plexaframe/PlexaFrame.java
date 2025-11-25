package com.vaibhavparashar.plexaframe;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main entry point for the PlexaFrame mod.
 * Implements ModInitializer, which Fabric calls during the loading process.
 * Responsible for initial setup including frustum/distance optimization startup
 */
public class PlexaFrame implements ModInitializer {

    public static final String MOD_ID = "plexaframe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Confirms the mod has been successfully loaded by Fabric
        LOGGER.info("PlexaFrame Initialized! Frustum & distance optimization enabled.");
        
        // Multithreading system will be added later after core logic is stable
        // LOGGER.debug("Thread pool initialization disabled for now.");
    }
}

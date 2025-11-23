package com.vaibhavparashar.plexaframe;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The client-side entry point for the PlexaFrame mod.
 * Used for client-specific setup, such as rendering or UI initialization.
 */
public class PlexaFrameClient implements ClientModInitializer {
    
    // We can reuse the logger from the main class
    public static final Logger LOGGER = LoggerFactory.getLogger(PlexaFrame.MOD_ID);

    @Override
    public void onInitializeClient() {
        // This method is called after the main mod entry point, but only on the client.
        LOGGER.info("PlexaFrame client setup complete.");
        
        // In this specific structure, most of your work is done in Mixins, 
        // so this class currently serves as the required entry point anchor.
    }
}
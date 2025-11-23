package com.vaibhavparashar.plexaframe;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main entry point for the PlexaFrame mod.
 * Implements ModInitializer, which Fabric calls during the loading process.
 * Responsible for initial setup, including starting the custom thread pool.
 */
public class PlexaFrame implements ModInitializer {
    
    public static final String MOD_ID = "plexaframe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This confirms the mod has been successfully loaded by the Fabric loader.
        LOGGER.info("PlexaFrame Initialized! Let's boost those frame rates.");
        
        // --- Multi-Threading Initialization ---
        // Ensure the thread pool manager is ready when the mod loads.
        // The submission of a test task ensures the thread pool is initialized 
        // and its background threads are running, ready for chunk meshing work.
        PlexaFrameThreadPoolManager.submitChunkTask(() -> 
            LOGGER.debug("PlexaFrame thread pool test task executed.")
        );
        
        // Note: For a proper shutdown, you would register a JVM shutdown hook 
        // to call PlexaFrameThreadPoolManager.shutdown(); 
    }
}
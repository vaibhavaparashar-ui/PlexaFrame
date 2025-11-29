package com.vaibhavparashar.plexaframe;

import net.fabricmc.api.ModInitializer;

public class PlexaFrame implements ModInitializer {
    public static final String MOD_ID = "plexaframe";

    @Override
    public void onInitialize() {
        System.out.println("[PlexaFrame] Loaded (C-Safe mode).");
    }
}

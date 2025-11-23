package com.vaibhavparashar.plexaframe.compat;

import net.fabricmc.loader.api.FabricLoader;

public class SodiumAdapter {

    private static final boolean SODIUM =
            FabricLoader.getInstance().isModLoaded("sodium");

    public static boolean isSodiumPresent() {
        return SODIUM;
    }

    public static void integrate() {
        if (!SODIUM) return;

        System.out.println("[PlexaFrame] Sodium detected, advanced integration enabled");
    }
}

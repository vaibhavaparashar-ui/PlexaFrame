package com.vaibhavparashar.plexaframe.config;

public class PlexaConfig {
    // toggles — can add persistence later
    public static boolean ENABLE_ENTITY_CULLING = true;
    public static boolean REDUCE_PARTICLES = true;
    public static boolean SKIP_DEATH_ANIMATION = true;

    public static int ENTITY_CULL_DISTANCE_BLOCKS = 64; // distance in blocks to render entities

    public static void load() {
        // placeholder: later read from file. For now defaults above are used.
    }
}

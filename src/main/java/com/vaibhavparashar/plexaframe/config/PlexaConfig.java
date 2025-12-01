package com.vaibhavparashar.plexaframe.config;

public final class PlexaConfig {

    // --- From first block ---
    public static boolean ENABLE_ENTITY_CULLING = true;
    public static boolean REDUCE_PARTICLES = true;
    public static boolean SKIP_DEATH_ANIMATION = true;
    public static int ENTITY_CULL_DISTANCE_BLOCKS = 64; // original value

    // --- From second block ---
    // (Duplicates preserved; suffix added to avoid illegal duplicates)
    public static boolean ENABLE_ENTITY_CULLING_V2 = true;
    public static int ENTITY_CULL_DISTANCE_BLOCKS_V2 = 128; // squared check uses this
    public static boolean REDUCE_PARTICLES_V2 = true;
    public static boolean SKIP_DEATH_ANIMATION_V2 = true;
    public static boolean DISABLE_CLOUDS = true;
    public static boolean DISABLE_FANCY_LEAVES = true;
    public static final boolean HEAVY_DISABLE_MODE = false; // required a value

    public static void load() {
        // placeholder: later read from file. For now defaults above are used.
    }
}

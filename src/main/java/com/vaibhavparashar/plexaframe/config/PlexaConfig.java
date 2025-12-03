package com.vaibhavparashar.plexaframe.config;

public final class PlexaConfig {
    // Master enable
    public static boolean FPS_BOOST_ENABLED = true;

    // Option toggles
    public static boolean DISABLE_CLOUDS = true;
    public static boolean DISABLE_FANCY_LEAVES = true;
    public static boolean HEAVY_DISABLE_MODE = true; // extra aggressive: forces FAST graphics
    public static boolean DISABLE_PARTICLES = true; // will cancel many particles via mixin
    public static boolean SKIP_DEATH_ANIMATION = true; // skip ticks that animate death
    public static boolean AGGRESSIVE_ENTITY_CULLING = true;
    public static int ENTITY_CULL_DISTANCE_BLOCKS = 64; // cull beyond this distance
}

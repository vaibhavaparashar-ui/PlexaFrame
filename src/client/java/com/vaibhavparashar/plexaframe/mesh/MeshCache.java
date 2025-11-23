package com.vaibhavparashar.plexaframe.mesh;

import net.minecraft.core.BlockPos;

import java.util.HashMap;

public class MeshCache {

    private static final HashMap<BlockPos, Long> CACHE = new HashMap<>();

    public static boolean isValid(BlockPos pos, long hash) {
        return CACHE.containsKey(pos) && CACHE.get(pos) == hash;
    }

    public static void store(BlockPos pos, long hash) {
        CACHE.put(pos, hash);
    }

    public static void clear() {
        CACHE.clear();
    }
}

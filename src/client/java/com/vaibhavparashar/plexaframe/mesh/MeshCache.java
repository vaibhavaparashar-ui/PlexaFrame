package com.vaibhavparashar.plexaframe.mesh;

import net.minecraft.core.BlockPos;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public final class MeshCache {
    private static final Map<BlockPos, Long> CACHE = new ConcurrentHashMap<>();
    public static boolean isValid(BlockPos pos, long hash) {
        return CACHE.containsKey(pos) && CACHE.get(pos).longValue() == hash;
    }
    public static void store(BlockPos pos, long hash) { CACHE.put(pos, hash); }
    public static void clear() { CACHE.clear(); }
}

package com.vaibhavparashar.plexaframe;

import net.minecraft.util.math.BlockPos;
import net.minecraft.client.render.Frustum;

public class FrustumChecker {
    private static Frustum frustum;

    public static void update(Frustum fr) {
        frustum = fr;
    }

    public static boolean shouldSkip(BlockPos pos) {
        return frustum != null && !frustum.isVisible(pos);
    }
}

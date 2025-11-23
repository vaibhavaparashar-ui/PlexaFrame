package com.vaibhavparashar.plexaframe.thread;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderSection;
import com.vaibhavparashar.plexaframe.mesh.MeshCache;

public class RebuildTask implements Runnable {

    private final RenderSection section;
    private final long hash;

    public RebuildTask(RenderSection section, long hash) {
        this.section = section;
        this.hash = hash;
    }

    @Override
    public void run() {
        if (MeshCache.isValid(section.getOrigin(), hash)) {
            return;
        }

        var result = section.rebuild();
        RenderSystem.recordRenderCall(() -> section.setBuffer(result));

        MeshCache.store(section.getOrigin(), hash);
    }
}

package com.vaibhavparashar.plexaframe.mixin;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Allows PlexaFrame to safely call internal GPU upload routines
 * only from Minecraft main thread via PlexaFrameThreadPoolManager.postToMainThread().
 */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public interface RenderSectionInvoker {

    @Invoker("rebuild")
    void plexaframe$invokeRebuild(float cameraX, float cameraY, float cameraZ, Object task);

    @Invoker("compile")
    Object plexaframe$invokeCompile(RenderType renderType);
}

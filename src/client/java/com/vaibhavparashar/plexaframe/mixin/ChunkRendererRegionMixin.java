package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.thread.PlexaFrameThreadPoolManager;
import com.vaibhavparashar.plexaframe.thread.RebuildTask;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher.RenderSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Redirects chunk mesh rebuild scheduling to our custom thread-safe scheduler.
 */
@Mixin(SectionRenderDispatcher.class)
public abstract class ChunkRendererRegionMixin {

    @Shadow
    protected abstract void scheduleSectionUpdate(RenderSection section);

    @Inject(method = "setDirty", at = @At("HEAD"), cancellable = true)
    private void plexa$redirectChunkRebuild(RenderSection section, boolean rerenderOnMainThread, CallbackInfo ci) {
        // cancel vanilla scheduling
        ci.cancel();

        // optional snapshot hash
        long hash = section.hashCode();

        // schedule CPU rebuild work on safe executor
        RebuildTask task = new RebuildTask(section, hash);
        PlexaFrameThreadPoolManager.submitSafe(task);
    }
}

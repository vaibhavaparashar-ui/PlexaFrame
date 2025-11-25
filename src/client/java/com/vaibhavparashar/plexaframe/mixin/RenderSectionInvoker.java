package com.vaibhavparashar.plexaframe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;

/**
 * Invoker mixin to call internal rebuild/upload methods on RenderSection.
 * The method name below (rebuild/compile/upload) may need adjustment per mappings;
 * I used a safe zero-arg invoker as a placeholder — see build errors if it needs parameters.
 */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public interface RenderSectionInvoker {

    // If the actual method signature requires args (camera pos / compile task), I will fix it.
    @Invoker("rebuild")
    void plexaframe$invokeRebuild();
}

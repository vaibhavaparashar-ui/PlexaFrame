package com.vaibhavparashar.plexaframe.mixin.accessor;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SectionRenderDispatcher.RenderSection.class)
public interface RenderSectionInvoker {

    // Invokes the upload function back on the main thread after background rebuild finishes
    @Invoker("beginLayerCompile")
    void plexaframe$invokeBeginLayerCompile();
}

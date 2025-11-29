package com.vaibhavparashar.plexaframe.mixin.accessor;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SectionRenderDispatcher.RenderSection.class)
public interface RenderSectionAccessor {

    // Get world-space block position of this section (used for threading/range culling)
    @Accessor("origin")
    BlockPos getOrigin();
}

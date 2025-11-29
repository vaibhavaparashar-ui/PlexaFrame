package com.vaibhavparashar.plexaframe.mixin;

import com.vaibhavparashar.plexaframe.overlay.DebugOverlay;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void plexa$debug(MatrixStack matrices, float tickDelta, long limit, boolean rendering, CallbackInfo ci) {
        DrawContext ctx = new DrawContext(matrices);
        DebugOverlay.render(ctx);
    }
}

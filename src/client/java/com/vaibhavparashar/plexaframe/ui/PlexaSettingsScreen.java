package com.vaibhavparashar.plexaframe.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vaibhavparashar.plexaframe.config.PlexaConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PlexaSettingsScreen extends Screen {
    private Checkbox threadingCheckbox;
    private EditBox workerCountBox;
    private Checkbox preloadCheckbox;

    protected PlexaSettingsScreen() {
        super(Component.literal("PlexaFrame Settings"));
    }

    @Override
    protected void init() {
        PlexaConfig cfg = PlexaConfig.instance();

        threadingCheckbox = new Checkbox(this.width/2 - 100, 50, 200, 20, Component.literal("Enable Threading"), cfg.enableThreading);
        addRenderableWidget(threadingCheckbox);

        workerCountBox = new EditBox(this.font, this.width/2 - 100, 80, 200, 20, Component.literal("Worker count"));
        workerCountBox.setValue(String.valueOf(cfg.workerCount));
        addRenderableWidget(workerCountBox);

        preloadCheckbox = new Checkbox(this.width/2 - 100, 110, 200, 20, Component.literal("Enable async preload"), cfg.enableAsyncPreload);
        addRenderableWidget(preloadCheckbox);

        addRenderableWidget(new Button(this.width/2 - 100, 150, 98, 20, Component.literal("Save"), b -> {
            try {
                cfg.enableThreading = threadingCheckbox.selected();
                cfg.workerCount = Math.max(1, Integer.parseInt(workerCountBox.getValue()));
                cfg.enableAsyncPreload = preloadCheckbox.selected();
                PlexaConfig.save();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            onClose();
        }));

        addRenderableWidget(new Button(this.width/2 + 2, 150, 98, 20, Component.literal("Cancel"), b -> onClose()));
    }

    @Override
    public void removed() {
        // no-op
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        drawCenteredString(pPoseStack, this.font, "PlexaFrame Settings", this.width/2, 20, 0xFFFFFF);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}

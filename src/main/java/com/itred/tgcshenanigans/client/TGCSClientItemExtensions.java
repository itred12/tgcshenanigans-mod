package com.itred.tgcshenanigans.client;

import com.itred.tgcshenanigans.block.entity.renderer.ProphecyPanelBEWLR;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class TGCSClientItemExtensions implements IClientItemExtensions {

    private final ProphecyPanelBEWLR bewlr = new ProphecyPanelBEWLR();

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return bewlr;
    }
}

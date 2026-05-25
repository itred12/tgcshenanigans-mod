package com.itred.tgcshenanigans.client;

import com.itred.tgcshenanigans.block.TGCSBlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class TGCSClientItemExtensions implements IClientItemExtensions {

    private final TGCSBlockEntityWithoutLevelRenderer bewlr = new TGCSBlockEntityWithoutLevelRenderer();

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return bewlr;
    }
}

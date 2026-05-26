package com.itred.tgcshenanigans.item.custom;

import com.itred.tgcshenanigans.client.render.TGCSRenderTypes;
import com.itred.tgcshenanigans.item.ICustomGlintItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class ProphecyPanelItem extends BlockItem implements ICustomGlintItem {

    public ProphecyPanelItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public RenderType getGlint() {
        return TGCSRenderTypes.DEPTHS_GLINT;
    }
}

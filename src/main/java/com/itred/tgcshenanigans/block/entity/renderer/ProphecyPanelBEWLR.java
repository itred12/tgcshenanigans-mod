package com.itred.tgcshenanigans.block.entity.renderer;

import com.itred.tgcshenanigans.block.AbstractProphecyPanelBlock;
import com.itred.tgcshenanigans.block.TGCSBlocks;
import com.itred.tgcshenanigans.block.entity.ProphecyPanelBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Map;

public class ProphecyPanelBEWLR extends BlockEntityWithoutLevelRenderer {

    private BlockEntityRenderDispatcher renderDispatcher;

    public ProphecyPanelBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.renderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();

    }

    private static final ProphecyPanelBlockEntity panelDefault = getDefaultProphecyPanelBlockEntity(TGCSBlocks.PROPHECY_PANEL_NEW);
    private static final ProphecyPanelBlockEntity panelGreen = getDefaultProphecyPanelBlockEntity(TGCSBlocks.PROPHECY_PANEL_GREEN);

    private static final Map<AbstractProphecyPanelBlock, BlockEntity> PROPHECY_PANEL_LOOKUP = Map.of(
            TGCSBlocks.PROPHECY_PANEL_NEW.get(), panelDefault,
            TGCSBlocks.PROPHECY_PANEL_GREEN.get(), panelGreen
    );


    private static ProphecyPanelBlockEntity getDefaultProphecyPanelBlockEntity(DeferredBlock<AbstractProphecyPanelBlock> block) {
        return new ProphecyPanelBlockEntity(BlockPos.ZERO, block.get().defaultBlockState(), block.get().getColor());
    }



    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractProphecyPanelBlock panel) {
            renderDispatcher.renderItem(PROPHECY_PANEL_LOOKUP.get(panel), poseStack, buffer, packedLight, packedOverlay);
        }
    }
}

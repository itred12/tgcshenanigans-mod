package com.itred.tgcshenanigans.block;

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

import java.util.HashMap;
import java.util.Map;

public class TGCSBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {

    private BlockEntityRenderDispatcher renderDispatcher;

    public TGCSBlockEntityWithoutLevelRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.renderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();

        TGCSBlocks.BLOCKS_PROPHECY.forEach(block -> PROPHECY_PANEL_LOOKUP.put(block.get(), getDefaultProphecyPanelBlockEntity(block)));



    }
    private final Map<AbstractProphecyPanelBlock, BlockEntity> PROPHECY_PANEL_LOOKUP = HashMap.newHashMap(TGCSBlocks.BLOCKS_PROPHECY.size());




    private static ProphecyPanelBlockEntity getDefaultProphecyPanelBlockEntity(DeferredBlock<AbstractProphecyPanelBlock> block) {
        return new ProphecyPanelBlockEntity(BlockPos.ZERO, block.get().defaultBlockState(), block.get().getColor());
    }



    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractProphecyPanelBlock panel && PROPHECY_PANEL_LOOKUP.containsKey(panel)) {
            renderDispatcher.renderItem(PROPHECY_PANEL_LOOKUP.get(panel), poseStack, buffer, packedLight, packedOverlay);
        }
    }
}

package com.itred.tgcshenanigans.block.entity;

import com.itred.tgcshenanigans.block.TGCSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ProphecyPanelBlockEntity extends BlockEntity {

    private int color = FastColor.ABGR32.color(255, 255, 255, 255);

    public ProphecyPanelBlockEntity(BlockPos pos, BlockState blockState) {
        super((BlockEntityType) TGCSBlockEntities.PROPHECY_PANEL.get(), pos, blockState);
    }

    public ProphecyPanelBlockEntity(BlockPos pos, BlockState blockState, int color) {
        this(pos, blockState);
        this.color = color;
    }

    public boolean shouldRenderFace(Direction face) {
        return true;
    }

    public int getColor() {
        return this.color;
    }



}



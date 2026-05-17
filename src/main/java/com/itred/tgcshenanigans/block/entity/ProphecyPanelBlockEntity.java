package com.itred.tgcshenanigans.block.entity;

import com.itred.tgcshenanigans.block.TGCSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ProphecyPanelBlockEntity extends BlockEntity {


    public ProphecyPanelBlockEntity(BlockPos pos, BlockState blockState) {
        super((BlockEntityType) TGCSBlockEntities.PROPHECY_PANEL.get(), pos, blockState);
    }

    public boolean shouldRenderFace(Direction face) {
        return face.getAxis() == Direction.Axis.Y;
    }


}



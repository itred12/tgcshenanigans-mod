package com.itred.tgcshenanigans.block;

import com.itred.tgcshenanigans.block.entity.ProphecyPanelBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractProphecyPanelBlock extends BaseEntityBlock {


    public static final MapCodec<AbstractProphecyPanelBlock> CODEC = simpleCodec(AbstractProphecyPanelBlock::new);

    // The physical shape of the block, basically its collision box
    protected static final VoxelShape PHYSICAL_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    private int color = FastColor.ABGR32.color(255, 255, 255, 255);

    public int getColor() {
        return color;
    }

    protected AbstractProphecyPanelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y));
    }

    protected AbstractProphecyPanelBlock(Properties properties, int color) {
        this(properties);
        this.color = color;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return PHYSICAL_SHAPE;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ProphecyPanelBlockEntity(blockPos, blockState, this.color);
    }




}

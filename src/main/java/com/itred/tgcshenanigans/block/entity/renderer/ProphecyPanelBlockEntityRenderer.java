package com.itred.tgcshenanigans.block.entity.renderer;

import com.itred.tgcshenanigans.TGCSUtils;
import com.itred.tgcshenanigans.block.AbstractProphecyPanelBlock;
import com.itred.tgcshenanigans.block.entity.ProphecyPanelBlockEntity;
import com.itred.tgcshenanigans.client.render.TGCSRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ProphecyPanelBlockEntityRenderer implements BlockEntityRenderer<ProphecyPanelBlockEntity> {

    public static final ResourceLocation DEPTHS = TGCSUtils.modLocation("textures/entity/depths_blue.png");

    // Add the constructor parameter for the lambda below. You may also use it to get some context
    // to be stored in local fields, such as the entity renderer dispatcher, if needed.
    public ProphecyPanelBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

        // THE PLAN
    // Modulo can be used to determine, from any scale, where the block entity is on the texture
    // I.e., if the texture is scaled 2x, then there's four possible locations:
    // Top left corner, top right corner,
    // Bottom left corner, bottom right corner
    private static final int SCALE = 2;

    // So taking mod 2 from the block's x and y location, which of these four corners can be found as the block's corner

    // This method is called every frame in order to render the block entity. Parameters are:
    // - blockEntity:   The block entity instance being rendered. Uses the generic type passed to the super interface.
    // - partialTick:   The amount of time, in fractions of a tick (0.0 to 1.0), that has passed since the last tick.
    // - poseStack:     The pose stack to render to.
    // - bufferSource:  The buffer source to get vertex buffers from.
    // - packedLight:   The light value of the block entity.
    // - packedOverlay: The current overlay value of the block entity, usually OverlayTexture.NO_OVERLAY.
    public void render(@NotNull ProphecyPanelBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Matrix4f matrix4f = poseStack.last().pose();
        this.renderCube(blockEntity, matrix4f, bufferSource.getBuffer(this.renderType()), blockEntity.getColor());
    }

    private void renderCube(ProphecyPanelBlockEntity blockEntity, Matrix4f pose, VertexConsumer consumer, int color) {

        // The position of any given corner of the prophecy panel is deterministic, based on its world position.
        // This lets us make any given block "connect" with any other on a whim.


        Direction.Axis axis = blockEntity.getBlockState().getValue(AbstractProphecyPanelBlock.AXIS);
        int axisOffset = axis.isHorizontal() ? 0 : 1;


        BlockPos blockPos = blockEntity.getBlockPos();
        int offsetX = Math.abs(blockPos.getX()) % SCALE;
        int offsetY = Math.abs(blockPos.getY()) % SCALE;
        int offsetZ = Math.abs(blockPos.getZ()) % SCALE;

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F,
                Direction.SOUTH,
                offsetX - offsetZ + axisOffset,
                offsetY,
                color
                );

        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of south
                1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F,
                Direction.NORTH,
                offsetX - offsetZ + axisOffset,
                offsetY,
                color
        );

        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of west, lines up with north
                1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F,
                Direction.EAST,
                offsetZ - offsetX + 1 + axisOffset,
                offsetY,
                color
        );

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F,
                Direction.WEST,
                offsetZ - offsetX + 1 + axisOffset,
                offsetY,
                color
        );

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F, 1.0F, 0F, 0F, 0.0F, 0.0F, 1.0F, 1.0F,
                Direction.DOWN,
                offsetX + axisOffset,
                offsetZ + 1,
                color
        );

        // TODO: FIX THIS VERY VERY TEMP FIX FOR HELD ITEM VERSIONS OF THE BLOCK
        if (blockEntity.getLevel() != null) {
            this.renderFace(
                    blockEntity, pose, consumer,
                    1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F,
                    Direction.UP,
                    offsetX + (1 - axisOffset),
                    offsetZ ,
                    color
            );
        } else {
            this.renderFace(
                    blockEntity, pose, consumer,
                    1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F,
                    Direction.UP,
                    offsetX + 1,
                    offsetZ + 1 ,
                    color
            );
        }


    }

    private void renderFace(
            ProphecyPanelBlockEntity blockEntity,
            Matrix4f pose,
            VertexConsumer consumer,
            float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3,
            Direction direction,
            int relativeXOffset,
            int relativeYOffset,
            int color
    ) {
        if (blockEntity.shouldRenderFace(direction)) {

            float iScale = 0.25f / SCALE;

            consumer.addVertex(pose, x0, y0, z0).setColor(color)
                    .setUv(
                            (relativeXOffset) * iScale,
                            (relativeYOffset) * iScale
                    );

            consumer.addVertex(pose, x1, y0, z1).setColor(color)
                    .setUv(
                            (1 + relativeXOffset) * iScale,
                            (relativeYOffset) * iScale
                    );

            consumer.addVertex(pose, x1, y1, z2).setColor(color)
                    .setUv(
                            (1 + relativeXOffset) * iScale,
                            (1 + relativeYOffset) * iScale
                    );

            consumer.addVertex(pose, x0, y1, z3).setColor(color)
                    .setUv(
                            (relativeXOffset) * iScale,
                            (1 + relativeYOffset) * iScale
                    );





        }



    }


    protected float getOffsetUp() {
        return 1.0f;
    }

    protected float getOffsetDown() {
        return 0.0f;
    }

    public static RenderType renderType() {
        return TGCSRenderTypes.RENDERTYPE_DEPTHS;
    }


    @Override
    public boolean shouldRenderOffScreen(ProphecyPanelBlockEntity blockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(blockEntity);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(ProphecyPanelBlockEntity blockEntity, Vec3 cameraPos) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
    }

    @Override
    public AABB getRenderBoundingBox(ProphecyPanelBlockEntity blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }


}

package com.itred.tgcshenanigans.block.entity.renderer;

import com.itred.tgcshenanigans.TGCSUtils;
import com.itred.tgcshenanigans.block.AbstractProphecyPaneBlock;
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
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ProphecyPanelBlockEntityRenderer implements BlockEntityRenderer<ProphecyPanelBlockEntity> {

    public static final ResourceLocation DEPTHS = TGCSUtils.modLocation("textures/misc/depths_monochrome.png");

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
    public void render(@NotNull ProphecyPanelBlockEntity blockEntity, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Matrix4f matrix4f = poseStack.last().pose();
        if (blockEntity.getBlockState().getBlock() instanceof AbstractProphecyPaneBlock) {
            this.renderPane(blockEntity, matrix4f, bufferSource.getBuffer(this.renderType()), FastColor.ABGR32.fromArgb32(blockEntity.getColor()));
        } else {
            this.renderCube(blockEntity, matrix4f, bufferSource.getBuffer(this.renderType()), FastColor.ABGR32.fromArgb32(blockEntity.getColor()));
        }

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

        float faceSizeOffset = 0.03f;
        float faceDepthOffset = 0.01f;


        this.renderFace(
                blockEntity, pose, consumer,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                Direction.SOUTH,
                offsetX - offsetZ + axisOffset,
                offsetY,
                color
                );

        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of south
                1.0F - faceSizeOffset, 0.0F + faceSizeOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                Direction.NORTH,
                offsetX - offsetZ + axisOffset,
                offsetY,
                color
        );

        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of west, lines up with north
                1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                1.0F - faceSizeOffset, 0.0F + faceSizeOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                Direction.EAST,
                offsetZ - offsetX + 1 + axisOffset,
                offsetY,
                color
        );

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                1.0F - faceSizeOffset, 0.0F + faceSizeOffset,
                Direction.WEST,
                offsetZ - offsetX + 1 + axisOffset,
                offsetY,
                color
        );

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset, 0F + faceSizeOffset, 0F + faceSizeOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                Direction.DOWN,
                offsetX + axisOffset,
                offsetZ + 1,
                color
        );

        // TODO: FIX THIS VERY VERY TEMP FIX FOR HELD ITEM VERSIONS OF THE BLOCK
        if (blockEntity.getLevel() != null) {
            this.renderFace(
                    blockEntity, pose, consumer,
                    1.0F - faceSizeOffset, 0.0F + faceSizeOffset,
                    1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                    0.0F + faceSizeOffset, 0.0F + faceSizeOffset,
                    1.0F - faceSizeOffset, 1.0F - faceSizeOffset,
                    Direction.UP,
                    offsetX + (1 - axisOffset),
                    offsetZ,
                    color
            );
        } else {
            this.renderFace(
                    blockEntity, pose, consumer,
                    1.0F - faceSizeOffset, 0.0F + faceSizeOffset,
                    1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                    0.0F + faceSizeOffset, 0.0F + faceSizeOffset,
                    1.0F - faceSizeOffset, 1.0F - faceSizeOffset,
                    Direction.UP,
                    offsetX + 1,
                    offsetZ + 1 ,
                    color
            );
        }


    }

    private void renderPane(ProphecyPanelBlockEntity blockEntity, Matrix4f pose, VertexConsumer consumer, int color) {

        // The position of any given corner of the prophecy panel is deterministic, based on its world position.
        // This lets us make any given block "connect" with any other on a whim.

        Direction.Axis axis = blockEntity.getBlockState().getValue(AbstractProphecyPanelBlock.AXIS);
        int axisOffset = axis.isHorizontal() ? 0 : 1;

        boolean isNorth = blockEntity.getBlockState().getValue(AbstractProphecyPaneBlock.NORTH);
        boolean isSouth = blockEntity.getBlockState().getValue(AbstractProphecyPaneBlock.SOUTH);
        boolean isEast = blockEntity.getBlockState().getValue(AbstractProphecyPaneBlock.EAST);
        boolean isWest = blockEntity.getBlockState().getValue(AbstractProphecyPaneBlock.WEST);

        BlockPos blockPos = blockEntity.getBlockPos();
        int offsetX = Math.abs(blockPos.getX()) % SCALE;
        int offsetY = Math.abs(blockPos.getY()) % SCALE;
        int offsetZ = Math.abs(blockPos.getZ()) % SCALE;

        float faceSizeOffset = 0.012f;
        float faceDepthOffset = 0.45f;

        float westUVOffset = -0.125f * 5.5f;
        float southUVOffset = -0.125f ;
        float eastUVOffset  = -0.125f;
        float northUVOffset = 0.125f * 6.2f;

        float westOffset = isWest ? 0.0F : 0.4375F;
        float eastOffset = isEast ? 1.0F : 0.5625F;
        float southOffset = isSouth ? 1.0F : 0.5625F;
        float northOffset = isNorth ? 0.0F : 0.4375F;

        // South face
        this.renderFace(
                blockEntity, pose, consumer,
                westOffset + faceSizeOffset, eastOffset - faceSizeOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                Direction.SOUTH,
                offsetX - offsetZ + axisOffset,
                offsetY,
                color,
                westOffset + faceSizeOffset + southUVOffset, eastOffset - faceSizeOffset + southUVOffset,
                0, 1
        );

        // North face
        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of south
                eastOffset - faceSizeOffset, westOffset + faceSizeOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                Direction.NORTH,
                offsetX - offsetZ + axisOffset,
                offsetY,
                color,
                -(eastOffset - faceSizeOffset + northUVOffset), -(westOffset + faceSizeOffset + northUVOffset),
                0, 1
        );

        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of west, lines up with north
                1.0F - faceDepthOffset, 1.0f - faceDepthOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                 southOffset - faceSizeOffset,northOffset + faceSizeOffset,
                 northOffset + faceSizeOffset,southOffset - faceSizeOffset,
                Direction.EAST,
                offsetZ - offsetX + 1 + axisOffset,
                offsetY,
                color,
                -(southOffset - faceSizeOffset + eastUVOffset), -(northOffset + faceSizeOffset + eastUVOffset),
                0, 1
        );

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                northOffset + faceSizeOffset, southOffset - faceSizeOffset,
                southOffset - faceSizeOffset, northOffset + faceSizeOffset,
                Direction.WEST,
                offsetZ - offsetX + 1 + axisOffset,
                offsetY,
                color,
                northOffset + faceSizeOffset + westUVOffset, southOffset - faceSizeOffset + westUVOffset,
                0, 1
        );




    }

    private void renderFace(
            ProphecyPanelBlockEntity blockEntity,
            Matrix4f pose,
            VertexConsumer consumer,
            float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3,
            Direction direction,
            int relativeXOffset,
            int relativeYOffset,
            int color,
            float uVX0,
            float uVX1,
            float uVY0,
            float uVY1
    ) {
        if (blockEntity.shouldRenderFace(direction)) {

            float iScale = 0.25f / SCALE;

            consumer.addVertex(pose, x0, y0, z0).setColor(color)
                    .setUv(
                            (uVX0 + relativeXOffset) * iScale,
                            (uVY0 + relativeYOffset) * iScale
                    );

            consumer.addVertex(pose, x1, y0, z1).setColor(color)
                    .setUv(
                            (uVX1 + relativeXOffset) * iScale,
                             (uVY0 + relativeYOffset) * iScale
                    );

            consumer.addVertex(pose, x1, y1, z2).setColor(color)
                    .setUv(
                            (uVX1 + relativeXOffset) * iScale,
                            (uVY1 + relativeYOffset) * iScale
                    );

            consumer.addVertex(pose, x0, y1, z3).setColor(color)
                    .setUv(
                            (uVX0 + relativeXOffset) * iScale,
                            (uVY1 + relativeYOffset) * iScale
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
        renderFace(blockEntity, pose, consumer, x0, x1, y0, y1, z0, z1, z2, z3, direction, relativeXOffset, relativeYOffset, color, 0, 1, 0, 1);
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
    public boolean shouldRenderOffScreen(@NotNull ProphecyPanelBlockEntity blockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(blockEntity);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(@NotNull ProphecyPanelBlockEntity blockEntity, @NotNull Vec3 cameraPos) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull ProphecyPanelBlockEntity blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }


}

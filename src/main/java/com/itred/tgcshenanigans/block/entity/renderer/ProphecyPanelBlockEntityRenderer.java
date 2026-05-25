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

    public static final ResourceLocation DEPTHS = TGCSUtils.modLocation("textures/entity/depths_monochrome.png");

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
                0.0F + faceSizeOffset, 1.0F - faceSizeOffset, 0.0F + faceSizeOffset, 1.0F - faceSizeOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                Direction.SOUTH,
                offsetX - offsetZ + axisOffset,
                offsetY,
                color
                );

        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of south
                1.0F - faceSizeOffset, 0.0F + faceSizeOffset, 0.0F + faceSizeOffset, 1.0F - faceSizeOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                Direction.NORTH,
                offsetX - offsetZ + axisOffset,
                offsetY,
                color
        );

        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of west, lines up with north
                1.0F - faceSizeOffset, 1.0F - faceSizeOffset, 0.0F + faceSizeOffset, 1.0F - faceSizeOffset, 1.0F - faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 1.0F - faceDepthOffset,
                Direction.EAST,
                offsetZ - offsetX + 1 + axisOffset,
                offsetY,
                color
        );

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F + faceSizeOffset, 0.0F + faceSizeOffset, 0.0F + faceSizeOffset, 1.0F - faceSizeOffset, 0.0F + faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset, 0.0F + faceDepthOffset,
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
                    1.0F - faceSizeOffset, 0.0F + faceSizeOffset, 1.0F - faceSizeOffset, 1.0F - faceSizeOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                    Direction.UP,
                    offsetX + (1 - axisOffset),
                    offsetZ ,
                    color
            );
        } else {
            this.renderFace(
                    blockEntity, pose, consumer,
                    1.0F - faceSizeOffset, 0.0F + faceSizeOffset, 1.0F - faceSizeOffset, 1.0F - faceSizeOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
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
        float connectedSizeCorrection = 0.001f; // Go a liiittle bit in to avoid a literally one-pixel gap that appears at corners
        float faceDepthOffset = 0.45f;


        float westOffset = isWest ? 0.0F : 0.4375F;
        float eastOffset = isEast ? 1.0F : 0.5625F;
        float southOffset = isSouth ? 1.0F : 0.5625F;
        float northOffset = isNorth ? 0.0F : 0.4375F;

        float frac716 = 0.4375f;
        float frac916 = 0.5625f;

        // A glass pane is 2 pixels wide if it's not attaching to anything, starting at x=8 and ending at the end of x=9
        // It's 8 pixels wide if it's attaching on the left or the right,
            // x=0 - x=8 on the left, and x=9 - x=16 on the right
                // 8 pixels from the left, 8 pixels from the right
        // And 16 pixels wide if it's attaching on both the left and the right

        // Each pane face is rendered in three parts:
            // The 6 pixel-wide section to the left of the middle, only if it's attaching to something there
            // The 6 pixel-wide section to the right of the middle, only if it's attaching to something there
            // The 2 pixel-wide section in the very middle, only if nothing is attaching to its face

        // Also need to adjust the UV depending on where it's connected.
        // The left and the right connect to the middle, rather than vice-versa, to allow the middle to still unrender when its covered by connecting at its face

        if (!isSouth) {

            // South face middle
            this.renderFace(
                    blockEntity, pose, consumer,
                    frac716 + faceSizeOffset,  frac916 - faceSizeOffset,
                    0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                    1.0F - faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                    Direction.SOUTH,
                    offsetX - offsetZ + axisOffset,
                    offsetY + 1,
                    color,
                    frac716 + faceSizeOffset, frac916 - faceSizeOffset,
                    0, 1
            );
        }

        if (isWest) {
            // South face left
            this.renderFace(
                    blockEntity, pose, consumer,
                    0.0F + faceSizeOffset,  frac716 + faceSizeOffset + connectedSizeCorrection,
                    0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                    1.0F - faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                    Direction.SOUTH,
                    offsetX - offsetZ + axisOffset,
                    offsetY + 1,
                    color,
                    // Apply a UV offset if the pane is only connected at the west OR the east to keep the texture aligned with everything else
                    0.0F, frac716 + faceSizeOffset + connectedSizeCorrection,
                    0.0F, 1.0F
            );

            // North face right
            this.renderFace(
                    blockEntity, pose, consumer,
                    // Inverse of south
                    frac716 + faceSizeOffset + connectedSizeCorrection, 0.0F + faceSizeOffset,
                    0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                    0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                    Direction.NORTH,
                    offsetX - offsetZ + axisOffset,
                    offsetY,
                    color,
                    frac916 - faceSizeOffset - connectedSizeCorrection, 0.0F,
                    0.0F, 1.0F
            );
        }

        if (isEast) {
            // South face right
            this.renderFace(
                    blockEntity, pose, consumer,
                    frac916 - faceSizeOffset - connectedSizeCorrection,  1.0F - faceSizeOffset,
                    0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                    1.0F - faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                    Direction.SOUTH,
                    offsetX - offsetZ + axisOffset,
                    offsetY + 1,
                    color,
                    frac916 - faceSizeOffset - connectedSizeCorrection, 1.0F,
                    0, 1
            );
            // North face left
            this.renderFace(
                    blockEntity, pose, consumer,
                    // Inverse of south
                    1.0F - faceSizeOffset, frac916 - faceSizeOffset - connectedSizeCorrection,
                    0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                    0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                    Direction.NORTH,
                    offsetX - offsetZ + axisOffset,
                    offsetY,
                    color,
                    1.0F, frac716 + faceSizeOffset + connectedSizeCorrection,
                    0.0F, 1.0F
            );
        }

        if (!isNorth) {
            // North face middle
            this.renderFace(
                    blockEntity, pose, consumer,
                    // Inverse of south
                    frac916 - faceSizeOffset, frac716 + faceSizeOffset,
                    0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                    0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceDepthOffset,
                    Direction.NORTH,
                    offsetX - offsetZ + axisOffset,
                    offsetY,
                    color,
                    frac716 + faceSizeOffset, frac916 - faceSizeOffset,
                    0.0F, 1.0F
            );

        }

        if (!isEast) {
            // East face middle
            this.renderFace(
                    blockEntity, pose, consumer,
                    // Inverse of west, lines up with north
                    1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                    0.0F + faceSizeOffset, 1.0F - faceSizeOffset,

                    frac916 - faceSizeOffset, frac716 + faceSizeOffset,
                    frac716 + faceSizeOffset, frac916 - faceSizeOffset,
                    Direction.EAST,
                    offsetZ - offsetX + 1 + axisOffset,
                    offsetY,
                    color,
                    frac716 + faceSizeOffset, frac916 - faceSizeOffset,
                    0, 1
            );

        }

        if (isNorth) {
            // East face right
            this.renderFace(
                    blockEntity, pose, consumer,
                    // Inverse of west, lines up with north
                    1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                    0.0F + faceSizeOffset, 1.0F - faceSizeOffset,

                    frac716 + faceSizeOffset, 0.0F + faceSizeOffset,
                    0.0F + faceSizeOffset, frac716 + faceSizeOffset,
                    Direction.EAST,
                    offsetZ - offsetX + 1 + axisOffset,
                    offsetY,
                    color,
                    frac916 - faceSizeOffset, 1.0F,
                    0, 1
            );
        }

        if (isSouth) {
            // East face left
            this.renderFace(
                    blockEntity, pose, consumer,
                    // Inverse of west, lines up with north
                    1.0F - faceDepthOffset, 1.0F - faceDepthOffset,
                    0.0F + faceSizeOffset, 1.0F - faceSizeOffset,
                    1.0F - faceSizeOffset, frac916 - faceSizeOffset,
                    frac916 - faceSizeOffset, 1.0F - faceSizeOffset,
                    Direction.EAST,
                    offsetZ - offsetX + 1 + axisOffset,
                    offsetY,
                    color,
                    0.0F, frac716 + faceSizeOffset,
                    0, 1
            );
        }






        // Prevent this face from rendering if it's covered by another pane/block, unless if theres a block to its left or right that it's also connecting with
        if (!isEast || (isNorth || isSouth)) {

            // If this is connecting to a face to the north or the south, move it in a bit to prevent clipping into the other pane
            float eastFaceOffset = northOffset + faceSizeOffset + (isSouth ^ isNorth ? 0.125f : 0);



        }



        if (!isWest || (isNorth || isSouth)) {

            this.renderFace(
                    blockEntity, pose, consumer,
                    0.0F + faceDepthOffset, 0.0F + faceDepthOffset, 0.0F + faceSizeOffset, 1.0F - faceSizeOffset, northOffset + faceSizeOffset, southOffset - faceSizeOffset, southOffset - faceSizeOffset, northOffset + faceSizeOffset,
                    Direction.WEST,
                    offsetZ - offsetX + 1 + axisOffset,
                    offsetY,
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

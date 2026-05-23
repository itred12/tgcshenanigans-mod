package com.itred.tgcshenanigans.block.entity.renderer;

import com.itred.tgcshenanigans.TGCSUtils;
import com.itred.tgcshenanigans.block.ProphecyPanelBlock;
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
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
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
        this.renderCube(blockEntity, matrix4f, bufferSource.getBuffer(this.renderType()));
    }

    private void renderCube(ProphecyPanelBlockEntity blockEntity, Matrix4f pose, VertexConsumer consumer) {

        // The position of any given corner of the prophecy panel is deterministic, based on its world position.
        // This lets us make any given block "connect" with any other on a whim.


        Direction.Axis axis = blockEntity.getBlockState().getValue(ProphecyPanelBlock.AXIS);
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
                offsetY
                );

        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of south
                1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F,
                Direction.NORTH,
                offsetX - offsetZ + axisOffset,
                offsetY
        );

        this.renderFace(
                blockEntity, pose, consumer,
                // Inverse of west, lines up with north
                1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F,
                Direction.EAST,
                offsetZ - offsetX + 1 + axisOffset,
                offsetY
        );

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F,
                Direction.WEST,
                offsetZ - offsetX + 1 + axisOffset,
                offsetY
        );

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F, 1.0F, 0F, 0F, 0.0F, 0.0F, 1.0F, 1.0F,
                Direction.DOWN,
                offsetX + axisOffset,
                offsetZ
        );

        this.renderFace(
                blockEntity, pose, consumer,
                0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F,
                Direction.UP,
                offsetX + axisOffset,
                offsetZ
        );

    }

    private void renderFace(
            ProphecyPanelBlockEntity blockEntity,
            Matrix4f pose,
            VertexConsumer consumer,
            float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3,
            Direction direction,
            int relativeXOffset,
            int relativeYOffset
    ) {
        if (blockEntity.shouldRenderFace(direction)) {

            float iScale = 0.25f / SCALE;

            consumer.addVertex(pose, x0, y0, z0)
                    .setUv(
                            (relativeXOffset) * iScale ,
                            (relativeYOffset) * iScale
                    );

            consumer.addVertex(pose, x1, y0, z1)
                    .setUv(
                            (1 + relativeXOffset) * iScale,
                            (relativeYOffset) * iScale
                    );

            consumer.addVertex(pose, x1, y1, z2)
                    .setUv(
                            (1 + relativeXOffset) * iScale,
                            (1 + relativeYOffset) * iScale
                    );

            consumer.addVertex(pose, x0, y1, z3)
                    .setUv(
                            (relativeXOffset) * iScale,
                            (1 + relativeYOffset) * iScale
            );

        }



    }

    private Vec2 getTargetBlockPositionFromFace(Direction face, BlockPos blockPos) {

        Vec3i normal = face.getNormal();

        if (face.getAxis().isHorizontal()) {
            return new Vec2(normal.getX() == 0 ? blockPos.getX() : blockPos.getZ(), blockPos.getY());
        } else {
            return new Vec2(blockPos.getX(), blockPos.getZ());
        }
    }

    protected float getOffsetUp() {
        return 1.0f;
    }

    protected float getOffsetDown() {
        return 0.0f;
    }

    protected RenderType renderType() {
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

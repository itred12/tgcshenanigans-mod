package com.itred.tgcshenanigans.client.render;

import com.itred.tgcshenanigans.item.TGCSItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CustomArmorModelRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final float BABY_BODY_SCALE = 0.5F;
    private static final float BABY_BODY_Y_OFFSET = 1.5F;

    private static ItemRenderer itemRenderer;

    public CustomArmorModelRenderer(RenderLayerParent<T, M> entityRendererIn) {
        super(entityRendererIn);
        itemRenderer = Minecraft.getInstance().getItemRenderer();
    }


    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        ItemStack chestArmor = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (chestArmor.is(TGCSItems.SPOTTED_TIE)) {
            itemRenderer.renderStatic(chestArmor, ItemDisplayContext.HEAD, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, livingEntity.level(), 0);
        }

    }

    public static <T extends  LivingEntity, M extends EntityModel<T>> void renderModel(M parentModel, LivingEntity livingEntity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack item) {

    }

    private static <L extends LivingEntity, M extends EntityModel<L>> void translateRotateAndScale(M parentModel, LivingEntity livingEntity, PoseStack poseStack, boolean wearsArmor) {
        if (parentModel instanceof HumanoidModel<?> humanoidModel) {
            if (livingEntity.isBaby() && !(livingEntity instanceof Player)) {
                poseStack.scale(BABY_BODY_SCALE, BABY_BODY_SCALE, BABY_BODY_SCALE);
                poseStack.translate(0.0F, BABY_BODY_Y_OFFSET, 0.0F);
            }

            humanoidModel.body.translateAndRotate(poseStack);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        float zOffset = wearsArmor ? -0.35f : -0.3f;
        float yOffset = -0.25f;

        poseStack.translate(0, yOffset, zOffset);

        if (livingEntity instanceof Player) {
            return;
        }
    }



}

package com.itred.tgcshenanigans.mixin.client;

import com.itred.tgcshenanigans.client.render.CustomEnchantGlintHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void tgcshenanigans$setTargetStack(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel p_model, CallbackInfo ci) {
        CustomEnchantGlintHandler.setTargetStack(itemStack);
    }

    // Attempting to be as unintrusive as possible here

    @WrapOperation(method = "getArmorFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorEntityGlint()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType tgcshenanigans$getArmorEntityGlint(Operation<RenderType> original) {
        RenderType glint = CustomEnchantGlintHandler.getArmorEntityGlint();
        return glint != null ? glint : original.call();
    }

    @WrapOperation(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entityGlint()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType tgcshenanigans$getEntityGlint(Operation<RenderType> original) {
        RenderType glint = CustomEnchantGlintHandler.getEntityGlint();
        return glint != null ? glint : original.call();
    }


    @WrapOperation(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glint()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType tgcshenanigans$getGlint(Operation<RenderType> original) {
        RenderType glint = CustomEnchantGlintHandler.getGlint();
        return glint != null ? glint : original.call();
    }

    @WrapOperation(method = "getFoilBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glintTranslucent()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType tgcshenanigans$getGlintTranslucent(Operation<RenderType> original) {
        RenderType glint = CustomEnchantGlintHandler.getGlintTranslucent();
        return glint != null ? glint : original.call();
    }

    @WrapOperation(method = "getFoilBufferDirect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glint()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType tgcshenanigans$getDirectGlint(Operation<RenderType> original) {
        RenderType glint = CustomEnchantGlintHandler.getGlint();
        return glint != null ? glint : original.call();
    }

    @WrapOperation(method = "getFoilBufferDirect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entityGlintDirect()Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType tgcshenanigans$getDirectEntityGlint(Operation<RenderType> original) {
        RenderType glint = CustomEnchantGlintHandler.getEntityGlintDirect();
        return glint != null ? glint : original.call();
    }







}

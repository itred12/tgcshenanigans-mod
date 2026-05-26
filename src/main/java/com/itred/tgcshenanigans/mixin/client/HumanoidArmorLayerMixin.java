package com.itred.tgcshenanigans.mixin.client;

import com.itred.tgcshenanigans.client.render.CustomEnchantGlintHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    @Inject(method = "getArmorModelHook", at = @At("HEAD"), remap = false)
    private void tgcshenanigans$setTargetStack(T entity, ItemStack itemStack, EquipmentSlot slot, A model, CallbackInfoReturnable<Model> cir) {
        CustomEnchantGlintHandler.setTargetStack(itemStack);
    }

    @WrapOperation(method = "renderGlint(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorEntityGlint()Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType tgcshenanigans$getArmorGlint(Operation<RenderType> original) {
        RenderType glint = CustomEnchantGlintHandler.getArmorGlint();
        return glint != null ? glint : original.call();
    }


}

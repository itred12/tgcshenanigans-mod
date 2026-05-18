package com.itred.tgcshenanigans.mixin;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.event.common.configurable.DurabilityRework;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"))
    private <T extends LivingEntity> void tgcshenanigans$disableDurability(int p_220158_, ServerLevel p_346256_, LivingEntity p_220160_, Consumer<Item> p_348596_, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;

        if (Config.DURABILITY_REWORK.get() && DurabilityRework.shouldBeUnbreakable(stack) && p_220160_ instanceof ServerPlayer player) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(player, stack, stack.getDamageValue());
        }

    }

    @Inject(method = "isDamageableItem", at = @At("HEAD"), cancellable = true)
    private void tgcshenanigans$disableDurability(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (Config.DURABILITY_REWORK.get() && DurabilityRework.shouldBeUnbreakable(stack)) {
            cir.setReturnValue(false);
        }
    }

}

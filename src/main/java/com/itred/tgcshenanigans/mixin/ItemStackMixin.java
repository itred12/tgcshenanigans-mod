package com.itred.tgcshenanigans.mixin;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.config.server.DurabilityRework;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"))
    private  void tgcshenanigans$disableDurability(int p_220158_, ServerLevel p_346256_, LivingEntity p_220160_, Consumer<Item> p_348596_, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;

        if (Config.DURABILITY_REWORK.get() && DurabilityRework.shouldBeUnbreakable(stack) && p_220160_ instanceof ServerPlayer player) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(player, stack, stack.getDamageValue());
        }

    }

    @WrapMethod(method = "isDamageableItem")
    private boolean tgcshenanigans$disableDurability(Operation<Boolean> original) {
        ItemStack stack = (ItemStack) (Object) this;
        if (Config.DURABILITY_REWORK.get() && DurabilityRework.shouldBeUnbreakable(stack)) {
            return false;
        }
        return original.call();
    }

}

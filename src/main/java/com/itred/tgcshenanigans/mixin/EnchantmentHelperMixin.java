package com.itred.tgcshenanigans.mixin;

import com.itred.tgcshenanigans.item.TGCSItems;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {


    // Return after the first enchantment is applied, before any further enchantments are applied, if the item is wax paper
    @Inject(method = "selectEnchantment", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER), cancellable = true)
    private static void tgcshenanigans$waxPaperOnlyOneEnchantment(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentInstance>> cir, @Local(name = "list") List<EnchantmentInstance> list) {
        if (stack.is(TGCSItems.WAX_PAPER)) {
            cir.setReturnValue(list);
        }
    }

    @WrapMethod(method = "getComponentType")
    private static DataComponentType<ItemEnchantments> tgcshenanigans$waxPaperStoreEnchantments(ItemStack stack, Operation<DataComponentType<ItemEnchantments>> original) {
        if (!stack.is(TGCSItems.ENCHANTED_PARCHMENT)) {
            return original.call(stack);
        }
        return DataComponents.STORED_ENCHANTMENTS;

    }

}

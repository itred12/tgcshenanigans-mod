package com.itred.tgcshenanigans.mixin;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.config.server.DurabilityRework;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(IItemExtension.class)
public interface IItemExtensionMixin {


    @ModifyReturnValue(
            method = "supportsEnchantment",
            at = @At(value = "TAIL")
    )
    private static boolean tgcshenanigans$disallowUnbreakingOnUnbreakable(
            boolean original,
            @Local(argsOnly = true, name = "stack", ordinal = 0) ItemStack stack,
            @Local(argsOnly = true, name = "enchantment", ordinal = 0) Holder<Enchantment> enchantment
    ) {
        // Attempt to make Unbreaking and Mending unsupported on already-unbreakable items while Durability Rework is enabled, as the enchantment would be useless.
        if (
                Config.DURABILITY_REWORK.get()
                        && DurabilityRework.preventEnchantment(enchantment, stack)
        )
        {
            return false;
        }
        return original;
    }
}

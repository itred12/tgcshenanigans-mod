package com.itred.tgcshenanigans.item.custom;

import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WaxPaperItem extends Item {

    public WaxPaperItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    // Supports any enchant
    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        return true;
    }

    @Override
    public int getEnchantmentValue(@NotNull ItemStack stack) {
        return 22; // Just as good as gold
    }

    @Override
    public @NotNull ItemStack applyEnchantments(ItemStack stack, List<EnchantmentInstance> enchantments) {

        ItemStack newStack = stack.transmuteCopy(TGCSItems.ENCHANTED_PARCHMENT);
        // Only one enchantment will be sent through at most but iterate through all of them just to be safe
        for (EnchantmentInstance enchantmentInstance : enchantments) {
            newStack.enchant(enchantmentInstance.enchantment, enchantmentInstance.level);
        }

        return newStack;

    }
}

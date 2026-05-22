package com.itred.tgcshenanigans;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class TGCSUtils {

    public static ResourceLocation modLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, path);
    }

    // Was tired of doing it manually over and over lol
    // There's probably a better way that I'm missing....
    public static boolean testForEnchant(ItemStack stack, ResourceKey<Enchantment> enchant) {
        for (Holder<Enchantment> itemEnchantment : stack.getTagEnchantments().keySet()) {
            if (itemEnchantment.is(enchant)) {
                return true;
            }
        }
        return false;
    }


}

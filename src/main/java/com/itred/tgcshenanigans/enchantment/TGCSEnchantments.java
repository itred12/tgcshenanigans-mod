package com.itred.tgcshenanigans.enchantment;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class TGCSEnchantments {

    public static final ResourceKey<Enchantment> DEEP_BREATH;

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        context.register(DEEP_BREATH,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                5,
                                5,
                                Enchantment.dynamicCost(5, 7),
                                Enchantment.dynamicCost(25, 7),
                                2,
                                EquipmentSlotGroup.MAINHAND
                        )).exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE)).build(TGCSEnchantments.DEEP_BREATH.location())
        );
    }

    private static ResourceKey<Enchantment> create(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name));
    }

    static {
        DEEP_BREATH = create("deep_breath");
    }


}

package com.itred.tgcshenanigans.enchantment;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;

public class TGCSEnchantments {

    public static final ResourceKey<Enchantment> DEEP_BREATH = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "deep_breath"));



    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        HolderGetter<DamageType> damageTypeGetter = context.lookup(Registries.DAMAGE_TYPE);

        registerEnchantment(context, DEEP_BREATH, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(5, 7),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.MAINHAND
                )).exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                /*
                .withEffect(EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM,
                        new DeepBreathEnchantmentEffect(damageTypeGetter.getOrThrow(DamageTypes.PLAYER_ATTACK))
                        // TODO: Configurable per-level damage, damage cap, falloff per level... Every private-static field in the enchantment effect, basically.
                )
                 */

        );

    }

    private static void registerEnchantment(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }

}

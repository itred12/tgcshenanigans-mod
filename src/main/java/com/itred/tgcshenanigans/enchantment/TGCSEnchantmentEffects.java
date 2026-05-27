package com.itred.tgcshenanigans.enchantment;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TGCSEnchantmentEffects {

    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENCHANTMENT_EFFECT_REGISTRY = DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, ThisGCsShenanigans.MODID);

    public static void registerAll(IEventBus bus) {
        ENCHANTMENT_EFFECT_REGISTRY.register(bus);
    }

}


package com.itred.tgcshenanigans.enchantment;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.enchantment.custom.DeepBreathEnchantmentEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TGCSEnchantmentEffects {

    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENCHANTMENT_EFFECT_REGISTRY = DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, ThisGCsShenanigans.MODID);

    public static final Supplier<MapCodec<? extends  EnchantmentEntityEffect>> DEEP_BREATH = ENCHANTMENT_EFFECT_REGISTRY.register(
            "deep_breath",
            () -> DeepBreathEnchantmentEffect.CODEC
    );


    public static void registerAll(IEventBus bus) {
        ENCHANTMENT_EFFECT_REGISTRY.register(bus);
    }

}


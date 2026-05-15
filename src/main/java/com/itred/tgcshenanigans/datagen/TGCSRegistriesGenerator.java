package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.enchantment.TGCSEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TGCSRegistriesGenerator extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER;

    public TGCSRegistriesGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ThisGCsShenanigans.MODID));
    }

    static {
        BUILDER = (new RegistrySetBuilder())
                .add(Registries.ENCHANTMENT, TGCSEnchantments::bootstrap);
    }
}
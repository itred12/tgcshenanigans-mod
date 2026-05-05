package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.datagen.loot.TGCSChestLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TGCSLootTableProvider extends LootTableProvider {


    public TGCSLootTableProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, requiredTables,
                List.of(
                        new LootTableProvider.SubProviderEntry(TGCSChestLootProvider::new, LootContextParamSets.CHEST)
                )
                , registries);
        ThisGCsShenanigans.LOGGER.info("Running loot!");
    }
}

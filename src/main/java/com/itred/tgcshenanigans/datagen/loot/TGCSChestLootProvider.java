package com.itred.tgcshenanigans.datagen.loot;

import com.itred.tgcshenanigans.item.TGCSItems;
import com.itred.tgcshenanigans.loot.TGCSLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class TGCSChestLootProvider implements LootTableSubProvider {

    public TGCSChestLootProvider(HolderLookup.Provider lookupProvider) {
        // Required for some reason???
    }


    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {

        consumer.accept(TGCSLootTables.INJECT_BASTION_TREASURE,
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                // Binomial distribution. Number of trials and chance of success per trial.
                                // 1 trial with a 10% chance of sucess means that this item will appear in 1 of 10 treasure bastion chests
                                .setRolls(BinomialDistributionGenerator.binomial(1, 0.1f))
                                .add(
                                        LootItem.lootTableItem(TGCSItems.DISC_AIZO.get())
                                                .setWeight(1) // Low weight makes it rarer. Takes into account weight of existing items

                                )

                )
        );


    }
}

package com.itred.tgcshenanigans.datagen.loot;

import com.itred.tgcshenanigans.enchantment.TGCSEnchantments;
import com.itred.tgcshenanigans.item.TGCSItems;
import com.itred.tgcshenanigans.loot.TGCSLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public record TGCSChestLootProvider(HolderLookup.Provider registries) implements LootTableSubProvider {

    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {

        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);


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

        consumer.accept(TGCSLootTables.INJECT_BURIED_TREASURE,
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                // Binomial distribution. Number of trials and chance of success per trial.
                                // 1 trial with a 10% chance of sucess means that this item will appear in 1 of 10 treasure bastion chests
                                .setRolls(BinomialDistributionGenerator.binomial(1, 0.1f))
                                .add(
                                        LootItem.lootTableItem(TGCSItems.DISC_DEATHODYSSEY.get())
                                                .setWeight(1) // Low weight makes it rarer. Takes into account weight of existing items

                                )

                )
        );

        consumer.accept(TGCSLootTables.INJECT_ANCIENT_CITY,
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                // Binomial distribution. Number of trials and chance of success per trial.
                                // 1 trial with a 10% chance of sucess means that this item will appear in 1 of 10 treasure bastion chests
                                .setRolls(BinomialDistributionGenerator.binomial(1, 0.1f))
                                .add(
                                        LootItem.lootTableItem(TGCSItems.DISC_DAUGHTEROFHALLOWNEST.get())
                                                .setWeight(1) // Low weight makes it rarer. Takes into account weight of existing items

                                )

                ).withPool(
                        LootPool.lootPool()
                                .setRolls(BinomialDistributionGenerator.binomial(1, 0.2f))
                                .add(loot(Items.BOOK, 1).apply(EnchantRandomlyFunction.randomEnchantment().withEnchantment(enchantments.getOrThrow(TGCSEnchantments.DEEP_BREATH))))
                )
        );


    }

    private static LootPoolSingletonContainer.Builder<?> loot(ItemLike item, int... val) {
        return val.length > 1 ? LootItem.lootTableItem(item).setWeight(val[0]).apply(SetItemCountFunction.setCount(UniformGenerator.between((float)val[1], (float)val[2]))) : LootItem.lootTableItem(item).setWeight(val[0]);
    }
}

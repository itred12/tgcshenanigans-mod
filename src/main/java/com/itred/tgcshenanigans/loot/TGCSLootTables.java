package com.itred.tgcshenanigans.loot;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public class TGCSLootTables {
    public static final ResourceKey<LootTable> INJECT_BASTION_TREASURE = lootTableFromExisting(BuiltInLootTables.BASTION_TREASURE);
    public static final ResourceKey<LootTable> INJECT_ANCIENT_CITY = lootTableFromExisting(BuiltInLootTables.ANCIENT_CITY);
    public static final ResourceKey<LootTable> INJECT_BURIED_TREASURE = lootTableFromExisting(BuiltInLootTables.ANCIENT_CITY);

    private static ResourceKey<LootTable> lootTableFromExisting(ResourceKey<LootTable> target) {
        return ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "inject/" + target.location().getPath())
        );
    }

}

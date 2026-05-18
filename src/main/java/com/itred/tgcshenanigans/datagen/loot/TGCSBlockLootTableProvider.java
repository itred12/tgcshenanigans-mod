package com.itred.tgcshenanigans.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

import java.util.Set;

public class TGCSBlockLootTableProvider extends BlockLootSubProvider {

    protected TGCSBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // dropSelf()

        /*
        add(<Block>,
                block -> createOreDrop(<Block>, <item>));
         */
    }

    /*
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return TGCSBlocks.BLOCKS.getEntries().stream.map(Holder::value)::iterator;
    }
    */
}

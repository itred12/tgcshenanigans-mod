package com.itred.tgcshenanigans.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class TGCSDataMapProvider extends DataMapProvider {

    protected TGCSDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @SuppressWarnings("removal")
    @Override
    protected void gather() {
        /*
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(TGCSItems.<Item>.getId(), new FurnaceFuel(<Burntime>), <replace?>);

         */


    }
}

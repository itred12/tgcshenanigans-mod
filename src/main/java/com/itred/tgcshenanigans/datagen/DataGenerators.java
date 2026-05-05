package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.datagen.create.TGCSPressingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


@SuppressWarnings("removal")
@EventBusSubscriber(modid = ThisGCsShenanigans.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void gatherData(GatherDataEvent event) {
        // Get the things we need to start from the event and its generator
        DataGenerator generator = event.getGenerator();
        PackOutput pack = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Block loot tables
        /*
        generator.addProvider(event.includeServer(), new LootTableProvider(
                pack,
                Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(TGCSBlockLootTableProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider
        ));

         */

        // Block tags
        BlockTagsProvider blockTagsProvider = new TGCSBlockTagProvider(pack, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);

        // Item tags
        generator.addProvider(event.includeServer(), new TGCSItemTagProvider(pack, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));

        // Biome tags
        generator.addProvider(event.includeServer(), new TGCSBiomeTagProvider(pack, lookupProvider, existingFileHelper));

        // Recipes
            // Vanilla
        generator.addProvider(event.includeServer(), new TGCSRecipeProvider(pack, lookupProvider));
            // Create
        generator.addProvider(event.includeServer(), new TGCSPressingRecipeGen(pack, lookupProvider));


        // Loot tables
        generator.addProvider(true, new TGCSLootTableProvider(pack, Set.of(), lookupProvider));

        // Datamapping
        generator.addProvider(event.includeServer(), new TGCSDataMapProvider(pack, lookupProvider));

        // Advancements
        generator.addProvider(event.includeServer(), new TGCSAdvancementProvider(pack, lookupProvider, existingFileHelper));

        // Item models (client side)
        generator.addProvider(event.includeClient(), new TGCSItemModelProvider(pack, existingFileHelper));
        // Blockstate models (client side)
        generator.addProvider(event.includeClient(), new TGCSBlockStateProvider(pack, existingFileHelper));





    }
}

package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.datagen.loot.TGCSLootTableProvider;
import com.itred.tgcshenanigans.datagen.recipe.TGCSRecipeProvider;
import com.itred.tgcshenanigans.datagen.recipe.create.TGCSPressingRecipeGen;
import com.itred.tgcshenanigans.datagen.tag.TGCSBiomeTagProvider;
import com.itred.tgcshenanigans.datagen.tag.TGCSBlockTagProvider;
import com.itred.tgcshenanigans.datagen.tag.TGCSItemTagProvider;
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

        // Add entires to the lookup, this must now be used for further entries
        CompletableFuture<HolderLookup.Provider> newLookup = ((TGCSRegistriesGenerator)generator.addProvider(event.includeServer(), new TGCSRegistriesGenerator(pack, lookupProvider))).getRegistryProvider();

        // Block tags
        BlockTagsProvider blockTagsProvider = new TGCSBlockTagProvider(pack, newLookup, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);

        // Item tags
        generator.addProvider(event.includeServer(), new TGCSItemTagProvider(pack, newLookup, blockTagsProvider.contentsGetter(), existingFileHelper));

        // Biome tags
        generator.addProvider(event.includeServer(), new TGCSBiomeTagProvider(pack, newLookup, existingFileHelper));

        // Recipes
            // Vanilla
        generator.addProvider(event.includeServer(), new TGCSRecipeProvider(pack, newLookup));
            // Create
        generator.addProvider(event.includeServer(), new TGCSPressingRecipeGen(pack, newLookup));


        // Loot tables
        generator.addProvider(true, new TGCSLootTableProvider(pack, Set.of(), newLookup));

        // Datamapping
        generator.addProvider(event.includeServer(), new TGCSDataMapProvider(pack, newLookup));

        // Advancements
        generator.addProvider(event.includeServer(), new TGCSAdvancementProvider(pack, newLookup, existingFileHelper));

        // Datapack
        // generator.addProvider(event.includeServer(), new TGCSDatapackProvider(pack, lookupProvider));

        // Item models (client side)
        generator.addProvider(event.includeClient(), new TGCSItemModelProvider(pack, existingFileHelper));
        // Blockstate models (client side)
        generator.addProvider(event.includeClient(), new TGCSBlockStateProvider(pack, existingFileHelper));






    }
}

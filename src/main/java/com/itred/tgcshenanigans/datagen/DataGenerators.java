package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.datagen.create.TGCSPressingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;


@SuppressWarnings("removal")
@EventBusSubscriber(modid = ThisGCsShenanigans.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {


    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // Get the things we need to start from the event and its generator
        DataGenerator generator = event.getGenerator();
        PackOutput pack = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Block loot tables (serverside)
        /*
        generator.addProvider(event.includeServer(), new LootTableProvider(
                pack,
                Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(TGCSBlockLootTableProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider
        ));

         */

        // Block tags (serverside)
        BlockTagsProvider blockTagsProvider = new TGCSBlockTagProvider(pack, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);

        // Item tags (serverside)
        generator.addProvider(event.includeServer(), new TGCSItemTagProvider(pack, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));

        // Recipes (serverside)
        generator.addProvider(event.includeServer(), new TGCSRecipeProvider(pack, lookupProvider));

        // Create recipes
        generator.addProvider(event.includeServer(), new TGCSPressingRecipeGen(pack, lookupProvider));

        // Datamapping (serverside)
        generator.addProvider(event.includeServer(), new TGCSDataMapProvider(pack, lookupProvider));

        // Item models (client side)
        generator.addProvider(event.includeClient(), new TGCSItemModelProvider(pack, existingFileHelper));
        // Blockstate models (client side)
        generator.addProvider(event.includeClient(), new TGCSBlockStateProvider(pack, existingFileHelper));




    }
}

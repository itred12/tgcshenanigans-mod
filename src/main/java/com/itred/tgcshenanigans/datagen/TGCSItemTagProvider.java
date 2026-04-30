package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TGCSItemTagProvider extends ItemTagsProvider {


    public TGCSItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ThisGCsShenanigans.MODID, existingFileHelper);
    }

    public static final TagKey<Item> PHANTASMAL_ITEMS = createItemTag("phantasmal_items");


    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Add tags in the same way as block tags
        tag(PHANTASMAL_ITEMS)
                .add(Items.PHANTOM_MEMBRANE)
                .add(Items.SOUL_SOIL);

    }

    private static TagKey<Item> createItemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name));
    }
}

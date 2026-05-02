package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
import com.itred.tgcshenanigans.tag.TGCSBiomeTags;
import com.itred.tgcshenanigans.tag.TGCSItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TGCSItemTagProvider extends ItemTagsProvider {


    public TGCSItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ThisGCsShenanigans.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // Add tags in the same way as block tags
        tag(TGCSItemTags.PHANTASMAL_ITEMS)
                .add(Items.PHANTOM_MEMBRANE)
                .add(Items.SOUL_SOIL);

        tag(TGCSItemTags.CRYSTALLINE_DISC_ITEMS)
                .add(TGCSItems.CRYSTALLINE_DISC_VOICELESS.get())
                .add(TGCSItems.CRYSTALLINE_DISC_AIZO.get());

    }

}

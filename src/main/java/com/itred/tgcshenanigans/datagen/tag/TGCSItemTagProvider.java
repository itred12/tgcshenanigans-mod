package com.itred.tgcshenanigans.datagen.tag;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
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
                .add(TGCSItems.CRYSTALLINE_DISC_AIZO.get())
                .add(TGCSItems.CRYSTALLINE_DISC_FIREPLACE.get())
                .add(TGCSItems.CRYSTALLINE_DISC_CATSWING.get())
                .add(TGCSItems.CRYSTALLINE_DISC_FROMNOWON.get())
                .add(TGCSItems.CRYSTALLINE_DISC_DEATHODYSSEY.get())
                .add(TGCSItems.CRYSTALLINE_DISC_DAUGHTEROFHALLOWNEST.get())
                .add(TGCSItems.CRYSTALLINE_DISC_REMEMBER.get());

        tag(TGCSItemTags.UNBREAKABLE_ITEMS)
                .add(Items.ELYTRA)
                .add(Items.TRIDENT)
                .add(Items.MACE);

        tag(TGCSItemTags.UNBREAKABLE_MATERIALS)
                .add(Items.NETHERITE_INGOT);


        // Aizo found in Bastion chests

    }

}

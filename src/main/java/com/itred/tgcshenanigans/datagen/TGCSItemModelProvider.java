package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class TGCSItemModelProvider extends ItemModelProvider {

    public TGCSItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ThisGCsShenanigans.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        fromParent(TGCSItems.DISC_FIREPLACE, "template_music_disc");
        fromParent(TGCSItems.DISC_AIZO, "template_music_disc");

        fromParent(TGCSItems.CRYSTALLINE_DISC_VOICELESS, "template_music_disc");
        fromParent(TGCSItems.CRYSTALLINE_DISC_AIZO, "template_music_disc");

        basicItem(TGCSItems.AMETHYST_PLATE.get());

    }

    private void fromParent(DeferredItem<?> item, String parent) {
        withExistingParent(
                item.getId().toString(),
                mcLoc("item/" + parent)
        )
                .texture("layer0", "item/" + item.getId().toString().substring(ThisGCsShenanigans.MODID.length() + 1)); // Snip out the namespace
    }
}

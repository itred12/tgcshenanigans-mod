package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TGCSItemModelProvider extends ItemModelProvider {

    public TGCSItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ThisGCsShenanigans.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        withExistingParent(TGCSItems.DISC_FIREPLACE.getId().toString(), mcLoc("item/template_music_disc")).texture("layer0", "item/music_disc_fireplace");
    }
}

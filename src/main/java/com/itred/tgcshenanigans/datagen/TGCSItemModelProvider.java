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
        fromParent(TGCSItems.DISC_CATSWING, "template_music_disc");
        fromParent(TGCSItems.DISC_FROMNOWON, "template_music_disc");
        fromParent(TGCSItems.DISC_DEATHODYSSEY, "template_music_disc");
        fromParent(TGCSItems.DISC_DAUGHTEROFHALLOWNEST, "template_music_disc");
        fromParent(TGCSItems.DISC_REMEMBER, "template_music_disc");

        fromParent(TGCSItems.CRYSTALLINE_DISC_VOICELESS, "template_music_disc");
        withOtherTexture(TGCSItems.CRYSTALLINE_DISC_AIZO, TGCSItems.CRYSTALLINE_DISC_VOICELESS, "template_music_disc");
        withOtherTexture(TGCSItems.CRYSTALLINE_DISC_FIREPLACE, TGCSItems.CRYSTALLINE_DISC_VOICELESS, "template_music_disc");
        withOtherTexture(TGCSItems.CRYSTALLINE_DISC_CATSWING, TGCSItems.CRYSTALLINE_DISC_VOICELESS, "template_music_disc");
        withOtherTexture(TGCSItems.CRYSTALLINE_DISC_FROMNOWON, TGCSItems.CRYSTALLINE_DISC_VOICELESS, "template_music_disc");
        withOtherTexture(TGCSItems.CRYSTALLINE_DISC_DEATHODYSSEY, TGCSItems.CRYSTALLINE_DISC_VOICELESS, "template_music_disc");
        withOtherTexture(TGCSItems.CRYSTALLINE_DISC_DAUGHTEROFHALLOWNEST, TGCSItems.CRYSTALLINE_DISC_VOICELESS, "template_music_disc");
        withOtherTexture(TGCSItems.CRYSTALLINE_DISC_REMEMBER, TGCSItems.CRYSTALLINE_DISC_VOICELESS, "template_music_disc");

        basicItem(TGCSItems.AMETHYST_PLATE.get());
        basicItem(TGCSItems.WAX_PAPER.get());
        basicItem(TGCSItems.ENCHANTED_PARCHMENT.get());

        basicItem(TGCSItems.SHADOW_HELM.get());
        basicItem(TGCSItems.SHADOW_CHESTPLATE.get());
        basicItem(TGCSItems.SHADOW_LEGGINGS.get());
        basicItem(TGCSItems.SHADOW_BOOTS.get());

        basicItem(TGCSItems.SHADOW_CRYSTAL.get());


    }

    private void fromParent(DeferredItem<?> item, String parent) {
        withExistingParent(
                item.getId().toString(),
                mcLoc("item/" + parent)
        )
                .texture("layer0", "item/" + item.getId().toString().substring(ThisGCsShenanigans.MODID.length() + 1)); // Snip out the namespace
    }


    private void withOtherTexture(DeferredItem<?> item, DeferredItem<?> otherTexture, String parent) {
        withExistingParent(
                item.getId().toString(),
                mcLoc("item/" + parent)
        )
                .texture("layer0", "item/" + otherTexture.getId().toString().substring(ThisGCsShenanigans.MODID.length() + 1)); // Snip out the namespace
    }
}

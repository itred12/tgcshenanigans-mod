package com.itred.tgcshenanigans.datagen.recipe.create;


import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Pseudo;

import java.util.concurrent.CompletableFuture;

@Pseudo

public class TGCSPressingRecipeGen extends com.simibubi.create.api.data.recipe.PressingRecipeGen {

    GeneratedRecipe AMETHYST_PLATE_PRESSING = create(
            () -> Items.AMETHYST_SHARD,
            builder -> builder.output(TGCSItems.AMETHYST_PLATE)
    );

    public TGCSPressingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ThisGCsShenanigans.MODID);
    }


}

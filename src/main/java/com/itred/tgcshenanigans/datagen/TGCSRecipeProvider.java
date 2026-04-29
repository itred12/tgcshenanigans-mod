package com.itred.tgcshenanigans.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class TGCSRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public TGCSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        /*
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TGCSBlocks.<Block>.get())
            .pattern("BBB")
            .pattern("BBB")
            .pattern("BBB")
            .define('B', TGCSItems.<Item>.get())
            .unlockedBy("has_blank", has(<TGCSItems.<Item>)).save(recipeOutput);


         */


    }
}

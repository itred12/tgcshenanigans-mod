package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
import com.itred.tgcshenanigans.tag.TGCSItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TGCSRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public TGCSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TGCSItems.CRYSTALLINE_DISC_VOICELESS)
            .pattern("GPG")
            .pattern("PAP")
            .pattern("GPG")
                .define('G', Items.GOLD_NUGGET)
                .define('P', TGCSItemTags.PHANTASMAL_ITEMS)
                .define('A', TGCSItems.AMETHYST_PLATE.get())
            .unlockedBy("has_amethyst_plate", has(TGCSItems.AMETHYST_PLATE)).save(recipeOutput);

        // Craft a crystalline disc with itself to clear it
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TGCSItems.CRYSTALLINE_DISC_VOICELESS)
                .requires(TGCSItemTags.CRYSTALLINE_DISC_ITEMS)
                .unlockedBy("has_crystalline_disc", has(TGCSItems.CRYSTALLINE_DISC_VOICELESS))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "crystalline_disc_clear"));


        SmithingTransformRecipeBuilder.smithing(
                Ingredient.EMPTY,
                Ingredient.of(TGCSItems.CRYSTALLINE_DISC_AIZO),
                Ingredient.of(TGCSItems.DISC_LABEL),
                RecipeCategory.MISC,
                TGCSItems.DISC_AIZO.get()
        ).unlocks("has_crystalline_disc", has(TGCSItems.CRYSTALLINE_DISC_VOICELESS))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "crystalline_disc_label_smithing_aizo"));


    }
}

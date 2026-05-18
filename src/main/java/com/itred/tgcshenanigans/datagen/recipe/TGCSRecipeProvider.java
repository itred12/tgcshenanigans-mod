package com.itred.tgcshenanigans.datagen.recipe;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
import com.itred.tgcshenanigans.tag.TGCSItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.registries.DeferredItem;
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


        discRecipeSmithing(
                Ingredient.of(TGCSItems.CRYSTALLINE_DISC_AIZO),
                Ingredient.of(Items.BLACK_DYE),
                TGCSItems.DISC_AIZO,
                "aizo",
                recipeOutput
        );

        discRecipeSmithing(
                Ingredient.of(TGCSItems.CRYSTALLINE_DISC_FIREPLACE),
                Ingredient.of(Items.GREEN_DYE),
                TGCSItems.DISC_FIREPLACE,
                "fireplace",
                recipeOutput
        );

        discRecipeSmithing(
                Ingredient.of(TGCSItems.CRYSTALLINE_DISC_CATSWING),
                Ingredient.of(Items.PINK_DYE, Items.YELLOW_DYE),
                TGCSItems.DISC_CATSWING,
                "catswing",
                recipeOutput
        );

        discRecipeSmithing(
                Ingredient.of(TGCSItems.CRYSTALLINE_DISC_FROMNOWON),
                Ingredient.of(Items.PURPLE_DYE),
                TGCSItems.DISC_FROMNOWON,
                "fromnowon",
                recipeOutput
        );

        discRecipeSmithing(
                Ingredient.of(TGCSItems.CRYSTALLINE_DISC_DEATHODYSSEY),
                Ingredient.of(Items.RED_DYE, Items.BLUE_DYE),
                TGCSItems.DISC_DEATHODYSSEY,
                "deathodyssey",
                recipeOutput
        );

        discRecipeSmithing(
                Ingredient.of(TGCSItems.CRYSTALLINE_DISC_DAUGHTEROFHALLOWNEST),
                Ingredient.of(Items.RED_DYE),
                TGCSItems.DISC_DAUGHTEROFHALLOWNEST,
                "daughterofhallownest",
                recipeOutput
        );

        discRecipeSmithing(
                Ingredient.of(TGCSItems.CRYSTALLINE_DISC_REMEMBER),
                Ingredient.of(Items.PINK_DYE, Items.GRAY_DYE),
                TGCSItems.DISC_REMEMBER,
                "remember",
                recipeOutput
        );


    }

    private void discRecipeSmithing(Ingredient crystallineDisc, Ingredient catalyst, DeferredItem<Item> discOutput, String name, RecipeOutput output) {
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.PAPER),
                crystallineDisc,
                catalyst,
                RecipeCategory.MISC,
                discOutput.get()
        )
                .unlocks("has_crystalline_disc", has(TGCSItems.CRYSTALLINE_DISC_VOICELESS))
                .save(output, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "crystalline_disc_label_smithing_" + name));
    }
}

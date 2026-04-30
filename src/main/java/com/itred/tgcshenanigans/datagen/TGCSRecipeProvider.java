package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.component.CrystallineDiscSong;
import com.itred.tgcshenanigans.component.TGCSDataComponents;
import com.itred.tgcshenanigans.datagen.recipe.CrystallineDiscApplyLabel;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class TGCSRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public TGCSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TGCSItems.CRYSTALLINE_DISC)
            .pattern("GPG")
            .pattern("PAP")
            .pattern("GPG")
                .define('G', Items.GOLD_NUGGET)
                .define('P', TGCSItemTagProvider.PHANTASMAL_ITEMS)
                .define('A', TGCSItems.AMETHYST_PLATE.get())
            .unlockedBy("has_amethyst_plate", has(TGCSItems.AMETHYST_PLATE)).save(recipeOutput);

        // Craft a crystalline disc with itself to clear it
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TGCSItems.CRYSTALLINE_DISC)
                .requires(TGCSItems.CRYSTALLINE_DISC)
                .unlockedBy("has_crystalline_disc", has(TGCSItems.CRYSTALLINE_DISC))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "crystalline_disc_clear"));

        // Smith a label onto a crystalline disc (lol)
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.EMPTY,
                Ingredient.of(new ItemStack(
                        TGCSItems.CRYSTALLINE_DISC,
                        1,
                        DataComponentPatch.builder()
                                .set(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT.get(), CrystallineDiscSong.AIZO)
                                .build())
                ),
                Ingredient.of(Items.PAPER),
                RecipeCategory.MISC,
                TGCSItems.DISC_AIZO.get()
        ).unlocks("has_crystalline_disc", has(TGCSItems.CRYSTALLINE_DISC))

                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "disc_smithing_aizo"));


        SpecialRecipeBuilder.special(CrystallineDiscApplyLabel::new).save(recipeOutput, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "crystalline_disc_label_applying"));
    }
}

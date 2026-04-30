package com.itred.tgcshenanigans.datagen.recipe;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.component.CrystallineDiscSong;
import com.itred.tgcshenanigans.component.TGCSDataComponents;
import com.itred.tgcshenanigans.item.CrystallineDiscItem;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class CrystallineDiscApplyLabel extends CustomRecipe {

    public CrystallineDiscApplyLabel(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {

        // The input resizes itself according to placed ingredients???
        // Okay.
        // SO the item in question (just paper for now) will always be on top of the disc if the disc is at index 1, and the item is at index 0.

        if (input.size() < 2) {
            return false;
        }

        ItemStack maybeDisc = input.getItem(1);
        ItemStack maybeLabel = input.getItem(0);

        if (!maybeDisc.isEmpty() && maybeDisc.is(TGCSItems.CRYSTALLINE_DISC)) {
            if (!maybeLabel.isEmpty() && maybeLabel.is(Items.PAPER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, HolderLookup.@NotNull Provider registries) {

        if (input.size() < 2) {
            return ItemStack.EMPTY;
        }

        ItemStack maybeDisc = input.getItem(1);
        ItemStack maybeLabel = input.getItem(0);


        if (!maybeDisc.isEmpty() && maybeDisc.is(TGCSItems.CRYSTALLINE_DISC)) {
            if (!maybeLabel.isEmpty() && maybeLabel.is(Items.PAPER)) {
                // Everything is assembled properly, get the data

                CrystallineDiscSong targetSong = maybeDisc.get(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT);
                if (targetSong == null) {
                    return ItemStack.EMPTY;
                }
                return new ItemStack(targetSong.getItemTarget().get());

            }
        }

        return ItemStack.EMPTY;
    }

    // Can be crafted in a 2x2 grid
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }


    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ThisGCsShenanigans.CRYSTALLINE_DISC_LABEL_SERIALIZER.get();
    }
}

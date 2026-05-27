package com.itred.tgcshenanigans.compat.justenoughitems;


import com.itred.tgcshenanigans.TGCSUtils;
import com.itred.tgcshenanigans.item.TGCSItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        registration.addIngredientInfo(TGCSItems.AMETHYST_PLATE, Component.translatable("gui.tgcshenanigans.amethyst_plate_anvilcrafting_details"));

    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return TGCSUtils.modLocation("jei_plugin");
    }

}

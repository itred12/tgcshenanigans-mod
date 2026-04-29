package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TGCSCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ThisGCsShenanigans.MODID);

    public static final Supplier<CreativeModeTab> GENERAL_ITEMS_TAB = CREATIVE_MODE_TABS.register("tgcs_items_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Items.CAMPFIRE))
                    .title(Component.translatable("creativetab.tgcshenanigans.tgcs_items_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // Add items here

                        output.accept(TGCSItems.AMETHYST_PLATE);
                        output.accept(TGCSItems.CRYSTALLINE_DISC);

                        output.accept(TGCSItems.DISC_FIREPLACE);
                        output.accept(TGCSItems.DISC_AIZO);
                    })
                    .build()
            );


    public static void registerAll(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}

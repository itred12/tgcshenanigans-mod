package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.block.TGCSBlocks;
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

                        // Add blocks ehre
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_MONOCHROME);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_WHITE);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_ORANGE);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_MAGENTA);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_LIGHT_BLUE);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_YELLOW);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_PINK);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_GRAY);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_LIGHT_GRAY);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_CYAN);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_PURPLE);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_BLUE);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_BROWN);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_GREEN);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_RED);
                        output.accept(TGCSBlocks.BLOCK_PROPHECY_BLACK);



                        // Add items here
                        output.accept(TGCSItems.BLUNT_CLEAVER);
                        output.accept(TGCSItems.SPOTTED_TIE);

                        output.accept(TGCSItems.SHADOW_HELM);
                        output.accept(TGCSItems.SHADOW_CHESTPLATE);
                        output.accept(TGCSItems.SHADOW_LEGGINGS);
                        output.accept(TGCSItems.SHADOW_BOOTS);

                        output.accept(TGCSItems.AMETHYST_PLATE);
                        // output.accept(TGCSItems.DISC_LABEL);

                        output.accept(TGCSItems.CRYSTALLINE_DISC_VOICELESS);
                        /*
                        output.accept(TGCSItems.CRYSTALLINE_DISC_AIZO);
                        output.accept(TGCSItems.CRYSTALLINE_DISC_FIREPLACE);
                        output.accept(TGCSItems.CRYSTALLINE_DISC_CATSWING);
                        output.accept(TGCSItems.CRYSTALLINE_DISC_FROMNOWON);
                        */
                        output.accept(TGCSItems.DISC_AIZO);
                        output.accept(TGCSItems.DISC_REMEMBER);
                        output.accept(TGCSItems.DISC_CATSWING);
                        output.accept(TGCSItems.DISC_FIREPLACE);
                        output.accept(TGCSItems.DISC_FROMNOWON);
                        output.accept(TGCSItems.DISC_DEATHODYSSEY);
                        output.accept(TGCSItems.DISC_DAUGHTEROFHALLOWNEST);

                    })
                    .build()
            );


    public static void registerAll(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}

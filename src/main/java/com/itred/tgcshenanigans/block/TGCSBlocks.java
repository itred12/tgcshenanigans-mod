package com.itred.tgcshenanigans.block;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TGCSBlocks {

    public static final DeferredRegister.Blocks BLOCKS_REGISTRY = DeferredRegister.createBlocks(ThisGCsShenanigans.MODID);

    public static final DeferredBlock<Block> PROPHECY_PANEL = registerBlock("prophecy_panel",
            () -> new GlazedTerracottaBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.GLASS)
            )
    );


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS_REGISTRY.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }


    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        TGCSItems.ITEMS_REGISTRY.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void registerAll(IEventBus bus) {
        BLOCKS_REGISTRY.register(bus);
    }

}

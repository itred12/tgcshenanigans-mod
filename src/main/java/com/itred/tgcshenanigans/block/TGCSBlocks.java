package com.itred.tgcshenanigans.block;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
import com.itred.tgcshenanigans.item.custom.ProphecyPanelItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class TGCSBlocks {

    public static final DeferredRegister.Blocks BLOCKS_REGISTRY = DeferredRegister.createBlocks(ThisGCsShenanigans.MODID);

    public static final DeferredBlock<Block> PROPHECY_PANEL_OLD = registerBlock("prophecy_panel_old",
            () -> new GlazedTerracottaBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.GLASS)
            )
    );



    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_MONOCHROME = baseProphecyPanelBlock("block_prophecy", FastColor.ABGR32.color(255, 255, 255, 255));

    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_WHITE = newProphecyPanel(DyeColor.WHITE);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_ORANGE = newProphecyPanel(DyeColor.ORANGE);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_MAGENTA = newProphecyPanel(DyeColor.MAGENTA);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_LIGHT_BLUE = newProphecyPanel(DyeColor.LIGHT_BLUE);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_YELLOW = newProphecyPanel(DyeColor.YELLOW);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_LIME = newProphecyPanel(DyeColor.LIME);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_PINK = newProphecyPanel(DyeColor.PINK);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_GRAY = newProphecyPanel(DyeColor.GRAY);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_LIGHT_GRAY = newProphecyPanel(DyeColor.LIGHT_GRAY);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_CYAN = newProphecyPanel(DyeColor.CYAN);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_PURPLE = newProphecyPanel(DyeColor.PURPLE);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_BLUE = newProphecyPanel(DyeColor.BLUE);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_BROWN = newProphecyPanel(DyeColor.BROWN);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_GREEN = newProphecyPanel(DyeColor.GREEN);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_RED = newProphecyPanel(DyeColor.RED);
    public static final DeferredBlock<AbstractProphecyPanelBlock> BLOCK_PROPHECY_BLACK = newProphecyPanel(DyeColor.BLACK);

    public static List<DeferredBlock<AbstractProphecyPanelBlock>> BLOCKS_PROPHECY = List.of(
        BLOCK_PROPHECY_MONOCHROME,
        BLOCK_PROPHECY_WHITE,
        BLOCK_PROPHECY_ORANGE,
        BLOCK_PROPHECY_MAGENTA,
        BLOCK_PROPHECY_LIGHT_BLUE,
        BLOCK_PROPHECY_YELLOW,
        BLOCK_PROPHECY_LIME,
        BLOCK_PROPHECY_PINK,
        BLOCK_PROPHECY_GRAY,
        BLOCK_PROPHECY_LIGHT_GRAY,
        BLOCK_PROPHECY_CYAN,
        BLOCK_PROPHECY_PURPLE,
        BLOCK_PROPHECY_BLUE,
        BLOCK_PROPHECY_BROWN,
        BLOCK_PROPHECY_GREEN,
        BLOCK_PROPHECY_RED,
        BLOCK_PROPHECY_BLACK
    );

    public static final DeferredBlock<AbstractProphecyPaneBlock> PANE_PROPHECY_MONOCHROME = registerBlock("block_prophecy_pane",
            () -> new AbstractProphecyPaneBlock(
                    BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .isViewBlocking((state, level, pos) -> false),
                    FastColor.ABGR32.color(255, 255, 255, 255)
            )
    );




    private static DeferredBlock<AbstractProphecyPanelBlock> newProphecyPanel(DyeColor color) {
        return baseProphecyPanelBlock("block_prophecy_" + color.getName(), color.getMapColor().calculateRGBColor(MapColor.Brightness.HIGH));
    }

    private static DeferredBlock<AbstractProphecyPanelBlock> baseProphecyPanelBlock(String name, int color) {
        DeferredBlock<AbstractProphecyPanelBlock> toReturn = BLOCKS_REGISTRY.register(name,
                () -> new AbstractProphecyPanelBlock(
                        BlockBehaviour.Properties.of()
                                .noOcclusion()
                                .isViewBlocking((state, level, pos) -> false),
                        color)
        );
        TGCSItems.ITEMS_REGISTRY.register(name, () -> new ProphecyPanelItem(toReturn.get(), new Item.Properties()
                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
        return toReturn;

    }

    private static DeferredBlock<AbstractProphecyPanelBlock> newProphecyPane(DyeColor color) {
        return registerBlock("block_prophecy_" + color.getName() + "pane",
                () -> new AbstractProphecyPanelBlock(
                        BlockBehaviour.Properties.of()
                                .noOcclusion()
                                .isViewBlocking((state, level, pos) -> false),
                        color.getMapColor().calculateRGBColor(MapColor.Brightness.HIGH)
                )
        );
    }

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

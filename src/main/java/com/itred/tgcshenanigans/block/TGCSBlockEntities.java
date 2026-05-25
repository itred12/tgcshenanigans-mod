package com.itred.tgcshenanigans.block;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.block.entity.ProphecyPanelBlockEntity;
import com.mojang.datafixers.types.Type;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TGCSBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ThisGCsShenanigans.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ProphecyPanelBlockEntity>> PROPHECY_PANEL = BLOCK_ENTITIES.register(
            "prophecy_panel_block_entity",
            // The block entity type, created using a builder.
            () -> BlockEntityType.Builder.of(
                            // The supplier to use for constructing the block entity instances.
                            ProphecyPanelBlockEntity::new,
                            // A vararg of blocks that can have this block entity.
                            // This assumes the existence of the referenced blocks as DeferredBlock<Block>s.
                            TGCSBlocks.BLOCK_PROPHECY_MONOCHROME.get(),
                            TGCSBlocks.BLOCK_PROPHECY_WHITE.get(),
                            TGCSBlocks.BLOCK_PROPHECY_ORANGE.get(),
                            TGCSBlocks.BLOCK_PROPHECY_MAGENTA.get(),
                            TGCSBlocks.BLOCK_PROPHECY_LIGHT_BLUE.get(),
                            TGCSBlocks.BLOCK_PROPHECY_YELLOW.get(),
                            TGCSBlocks.BLOCK_PROPHECY_LIME.get(),
                            TGCSBlocks.BLOCK_PROPHECY_PINK.get(),
                            TGCSBlocks.BLOCK_PROPHECY_GRAY.get(),
                            TGCSBlocks.BLOCK_PROPHECY_LIGHT_GRAY.get(),
                            TGCSBlocks.BLOCK_PROPHECY_CYAN.get(),
                            TGCSBlocks.BLOCK_PROPHECY_PURPLE.get(),
                            TGCSBlocks.BLOCK_PROPHECY_BLUE.get(),
                            TGCSBlocks.BLOCK_PROPHECY_BROWN.get(),
                            TGCSBlocks.BLOCK_PROPHECY_GREEN.get(),
                            TGCSBlocks.BLOCK_PROPHECY_RED.get(),
                            TGCSBlocks.BLOCK_PROPHECY_BLACK.get(),

                            TGCSBlocks.PANE_PROPHECY_MONOCHROME.get()
                    )
                    // Build using null; vanilla does some datafixer shenanigans with the parameter that we don't need.
                    .build((Type<?>) null)
    );



    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

}

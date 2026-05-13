package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class TGCSBlockStateProvider extends BlockStateProvider {

    public TGCSBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ThisGCsShenanigans.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {


        /*
        simpleBlockWithItem(
                TGCSBlocks.PROPHECY_PANEL.get(),
                models().withExistingParent(
                        TGCSBlocks.PROPHECY_PANEL.getRegisteredName(),
                        mcLoc("block/template_glazed_terracotta")
                ).texture(
                        "pattern",
                        modLoc("block/prophecy_panel")
                )
        );

         */


        /*

        simpleBlockWithItem(
                TGCSBlocks.PROPHECY_PANEL.get(),
                models().cube(
                        TGCSBlocks.PROPHECY_PANEL.getRegisteredName(),
                        modLoc("block/prophecy_panel_topleft"),
                        modLoc("block/prophecy_panel_bottomright"),
                        modLoc("block/prophecy_panel_topleft"),
                        modLoc("block/prophecy_panel_topright"),
                        modLoc("block/prophecy_panel_bottomleft"),
                        modLoc("block/prophecy_panel_bottomright")

                )
                );

        */
    }


    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }


}

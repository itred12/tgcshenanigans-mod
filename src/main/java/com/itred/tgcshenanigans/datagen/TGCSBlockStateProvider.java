package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.block.AbstractProphecyPanelBlock;
import com.itred.tgcshenanigans.block.TGCSBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class TGCSBlockStateProvider extends BlockStateProvider {



    public TGCSBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ThisGCsShenanigans.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        for (DeferredBlock<AbstractProphecyPanelBlock> block : TGCSBlocks.BLOCKS_PROPHECY) {
            unconventionalCubeBlock(block, modLoc("block/sanctuary_glass"), mcLoc("block/obsidian"));
        }

        lessPickyBlockInternalWithRenderType(TGCSBlocks.PANE_PROPHECY_MONOCHROME.get(), TGCSBlocks.PANE_PROPHECY_MONOCHROME.getRegisteredName(), modLoc("block/sanctuary_glass"), modLoc("block/sanctuary_glass_edge"), mcLoc(RenderType.cutoutMipped().name));
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

    /** Used for block entities without any actual textures to their name. A BlockEntityWithoutLevelRendered must be used to render the item. */
    private void unconventionalTexturelessBlock(DeferredBlock<?> deferredBlock, ResourceLocation breakParticle) {
        String path = deferredBlock.getId().toString();
        simpleBlock(deferredBlock.get(), this.models().getBuilder(path)
                .texture("particle", breakParticle));
        itemModels().withExistingParent(path, modLoc("item/prophecy_panel_item_template"));
    }

    private void unconventionalCubeBlock(DeferredBlock<?> deferredBlock, ResourceLocation cubeTexture, ResourceLocation breakParticle) {
        String path = deferredBlock.getId().toString();
        simpleBlock(deferredBlock.get(), this.models().withExistingParent(path, "block/cube_all")
                .texture("all", cubeTexture)
                .texture("particle", breakParticle)
                .renderType(RenderType.cutoutMipped().name));
        itemModels().withExistingParent(path, modLoc("item/prophecy_panel_item_template"));
    }

    private void lessPickyBlockInternalWithRenderType(Block block, String baseName, ResourceLocation pane, ResourceLocation edge, ResourceLocation renderType) {
        ModelFile post = models().panePost(baseName + "_post", pane, edge).renderType(renderType);
        ModelFile side = models().paneSide(baseName + "_side", pane, edge).renderType(renderType);
        ModelFile sideAlt = models().paneSideAlt(baseName + "_side_alt", pane, edge).renderType(renderType);
        ModelFile noSide = models().paneNoSide(baseName + "_noside", pane).renderType(renderType);
        ModelFile noSideAlt = models().paneNoSideAlt(baseName + "_noside_alt", pane).renderType(renderType);
        lessPicyPaneBlock(block, post, side, sideAlt, noSide, noSideAlt);
    }

    private void lessPicyPaneBlock(Block block, ModelFile post, ModelFile side, ModelFile sideAlt, ModelFile noSide, ModelFile noSideAlt) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
                .part().modelFile(post).addModel().end();
        PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> {
            Direction dir = e.getKey();
            if (dir.getAxis().isHorizontal()) {
                boolean alt = dir == Direction.SOUTH;
                builder.part().modelFile(alt || dir == Direction.WEST ? sideAlt : side).rotationY(dir.getAxis() == Direction.Axis.X ? 90 : 0).addModel()
                        .condition(e.getValue(), true).end()
                        .part().modelFile(alt || dir == Direction.EAST ? noSideAlt : noSide).rotationY(dir == Direction.WEST ? 270 : dir == Direction.SOUTH ? 90 : 0).addModel()
                        .condition(e.getValue(), false);
            }
        });
    }





}

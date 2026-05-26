package com.itred.tgcshenanigans.event;

import com.itred.tgcshenanigans.TGCSUtils;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.block.TGCSBlockEntities;
import com.itred.tgcshenanigans.block.TGCSBlocks;
import com.itred.tgcshenanigans.block.entity.renderer.ProphecyPanelBlockEntityRenderer;
import com.itred.tgcshenanigans.client.TGCSClientItemExtensions;
import com.itred.tgcshenanigans.client.render.CustomArmorModelRenderer;
import com.itred.tgcshenanigans.client.render.CustomCrosshairRenderer;
import com.itred.tgcshenanigans.client.render.TGCSRenderTypes;
import com.itred.tgcshenanigans.event.client.BluntCleaverClientUtils;
import com.itred.tgcshenanigans.event.client.OriginsKeyPressListener;
import com.itred.tgcshenanigans.event.client.TGCSShaders;
import com.itred.tgcshenanigans.particle.RatioParticle;
import com.itred.tgcshenanigans.particle.TGCSParticles;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Map;

@EventBusSubscriber(modid = ThisGCsShenanigans.MODID, value = Dist.CLIENT)
public class TGCSClientEvents {

    public static void onBoot(IEventBus modBus) {
        // Reading keys held for doing stuff with origins
        if (ModList.get().isLoaded("origins")) {
            ThisGCsShenanigans.LOGGER.info("Client event: registering OriginsKeyPressListener");
            NeoForge.EVENT_BUS.register(OriginsKeyPressListener.class);
        }


        NeoForge.EVENT_BUS.register(BluntCleaverClientUtils.class);
        modBus.register(TGCSShaders.class);
        modBus.addListener(TGCSClientEvents::registerReloadListener);
        modBus.addListener(TGCSClientEvents::registerRenderBuffers);

    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TGCSBlockEntities.PROPHECY_PANEL.get(), ProphecyPanelBlockEntityRenderer::new);
    }

    // Everyone say "Thank you Sophisticated Backpacks"
    public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) resourceManager -> {
            registerArmorLayer();
        });
    }

    @SuppressWarnings("java:S3740") //explanation below
    private static void registerArmorLayer() {
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        Map<PlayerSkin.Model, EntityRenderer<? extends Player>> skinMap = renderManager.getSkinMap();
        for (EntityRenderer<? extends Player> renderer : skinMap.values()) {
            if (renderer instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
                //noinspection rawtypes ,unchecked - this is not going to fail as the LivingRenderer makes sure the types are right, but there doesn't seem to be a way to us inference here
                livingEntityRenderer.addLayer(new CustomArmorModelRenderer(livingEntityRenderer));
            }
        }

        renderManager.renderers.forEach((e, r) -> {
            if (r instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
                //noinspection rawtypes ,unchecked - this is not going to fail as the LivingRenderer makes sure the types are right, but there doesn't seem to be a way to us inference here
                livingEntityRenderer.addLayer(new CustomArmorModelRenderer(livingEntityRenderer));
            }
        });
    }

    public static final LayeredDraw.Layer NEW_CROSSHAIR = CustomCrosshairRenderer::render;

    @SubscribeEvent
    public static void registerHudOverlays(RegisterGuiLayersEvent ev) {
        ev.registerAbove(VanillaGuiLayers.CROSSHAIR, TGCSUtils.modLocation("custom_crosshair"), NEW_CROSSHAIR);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(TGCSParticles.RATIO_PARTICLE.get(), RatioParticle.Provider::new);
    }

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
                // The only instance of our IClientItemExtensions, and as such, the only instance of our BEWLR.
                new TGCSClientItemExtensions(),
                // A vararg list of items that use this BEWLR.
                TGCSBlocks.BLOCK_PROPHECY_MONOCHROME.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_WHITE.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_ORANGE.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_MAGENTA.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_LIGHT_BLUE.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_YELLOW.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_LIME.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_PINK.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_GRAY.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_LIGHT_GRAY.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_CYAN.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_PURPLE.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_BLUE.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_BROWN.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_GREEN.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_RED.asItem(),
                TGCSBlocks.BLOCK_PROPHECY_BLACK.asItem()
        );
    }

    // register custom enchant glint
    public static void registerRenderBuffers(RegisterRenderBuffersEvent event) {
        Map<RenderType, ByteBufferBuilder> glints = TGCSRenderTypes.getGlintTypesAndBuffers();

        for (Map.Entry<RenderType, ByteBufferBuilder> entry : glints.entrySet()) {
            event.registerRenderBuffer(entry.getKey());
        }
    }





}

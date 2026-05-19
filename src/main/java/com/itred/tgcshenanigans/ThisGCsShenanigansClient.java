package com.itred.tgcshenanigans;

import com.itred.tgcshenanigans.client.render.CustomArmorModelRenderer;
import com.itred.tgcshenanigans.client.render.CustomCrosshairRenderer;
import com.itred.tgcshenanigans.event.TGCSClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.Map;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ThisGCsShenanigans.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ThisGCsShenanigans.MODID, value = Dist.CLIENT)
public class ThisGCsShenanigansClient {

    public static final LayeredDraw.Layer NEW_CROSSHAIR = CustomCrosshairRenderer::render;

    public ThisGCsShenanigansClient(IEventBus bus, ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        TGCSClientEvents.onBoot(bus);
        bus.addListener(ThisGCsShenanigansClient::registerReloadListener);


    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        ThisGCsShenanigans.LOGGER.info("HELLO FROM CLIENT SETUP");
        ThisGCsShenanigans.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());


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

    @SubscribeEvent
    public static void registerHudOverlays(RegisterGuiLayersEvent ev) {

        ev.registerAbove(VanillaGuiLayers.CROSSHAIR, Util.modLocation("custom_crosshair"), NEW_CROSSHAIR);
    }







}

package com.itred.tgcshenanigans;

import com.iafenvoy.origins.attachment.OriginDataHolder;
import com.iafenvoy.origins.data.layer.Layer;
import com.iafenvoy.origins.data.layer.LayerRegistries;
import com.iafenvoy.origins.data.power.builtin.regular.ResourcePower;
import com.iafenvoy.origins.data.power.component.builtin.ResourceComponent;
import com.itred.tgcshenanigans.network.payload.KeyPressC2SPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ThisGCsShenanigans.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ThisGCsShenanigans.MODID, value = Dist.CLIENT)
public class ThisGCsShenanigansClient {
    public ThisGCsShenanigansClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        ThisGCsShenanigans.LOGGER.info("HELLO FROM CLIENT SETUP");
        ThisGCsShenanigans.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Pre event) {

        Minecraft clientMinecraft = Minecraft.getInstance();
        LocalPlayer player = clientMinecraft.player;
        Options options = clientMinecraft.options;

        if (player == null) {
            return;
        }

        OriginDataHolder data = OriginDataHolder.get(player);
        Optional<HolderLookup.RegistryLookup<Layer>> lookup = data.getAccess().lookup(LayerRegistries.LAYER_KEY);

        if (data.hasActivePower(ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "space_held"), ResourcePower.class)) {

            int value = data.getComponent(ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "space_held"), ResourceComponent.class).map(ResourceComponent::getValue).orElse(0);

            if (options.keyJump.isDown() && value == 0) {
                PacketDistributor.sendToServer(new KeyPressC2SPayload("key.jump", true));
            }

            if (!options.keyJump.isDown() && value == 1) {
                PacketDistributor.sendToServer(new KeyPressC2SPayload("key.jump", false));
            }


        }


    }





}

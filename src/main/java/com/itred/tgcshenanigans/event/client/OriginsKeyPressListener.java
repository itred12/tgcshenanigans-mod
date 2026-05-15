package com.itred.tgcshenanigans.event.client;

import com.iafenvoy.origins.attachment.OriginDataHolder;
import com.iafenvoy.origins.data.layer.Layer;
import com.iafenvoy.origins.data.layer.LayerRegistries;
import com.iafenvoy.origins.data.power.builtin.regular.ResourcePower;
import com.iafenvoy.origins.data.power.component.builtin.ResourceComponent;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.network.payload.KeyPressC2SPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

public class OriginsKeyPressListener {


    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {

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

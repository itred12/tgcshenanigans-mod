package com.itred.tgcshenanigans.event.client;

import com.itred.tgcshenanigans.data.TGCSAttachments;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public class BluntCleaverClientUtils {

    @SubscribeEvent
    public static void onAttributeTooltip(AddAttributeTooltipsEvent event) {
        ItemStack stack = event.getStack();
        if (stack.is(TGCSItems.BLUNT_CLEAVER)) {
            event.addTooltipLines(Component.translatable("item.tgcshenanigans.blunt_cleaver.tooltip")
                    .withStyle(ChatFormatting.RED));
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayPre(RenderGuiLayerEvent.Pre ev) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player != null && ev.getName().equals(VanillaGuiLayers.CROSSHAIR) && player.getData(TGCSAttachments.NANAMI_CROSSHAIR_ATTACHMENT)) {
            ev.setCanceled(true);
        }

    }
}

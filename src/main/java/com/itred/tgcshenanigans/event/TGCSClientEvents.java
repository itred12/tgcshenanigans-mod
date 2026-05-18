package com.itred.tgcshenanigans.event;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.event.client.OriginsKeyPressListener;
import com.itred.tgcshenanigans.event.client.configurable.BlueAxolotlPing;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = ThisGCsShenanigans.MODID, value = Dist.CLIENT)
public class TGCSClientEvents {

    public static void onBoot(IEventBus modBus) {
        // Reading keys held for doing stuff with origins
        if (ModList.get().isLoaded("origins")) {
            ThisGCsShenanigans.LOGGER.info("Client event: registering OriginsKeyPressListener");
            NeoForge.EVENT_BUS.register(OriginsKeyPressListener.class);
        }
    }

    @SubscribeEvent
    public static void worldStaticMechanics(LevelEvent.Load event) {

        if (Config.ENABLE_BLUE_AXOLOTL_PING.get()) {
            NeoForge.EVENT_BUS.register(BlueAxolotlPing.class);
        }

    }

    @SubscribeEvent
    public static void unloadWorldStaticMechanics(LevelEvent.Unload event) {
        NeoForge.EVENT_BUS.unregister(BlueAxolotlPing.class);
    }


}

package com.itred.tgcshenanigans.event;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.event.common.DeepBreathEnchantmentHitEvent;
import com.itred.tgcshenanigans.event.common.DisableEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;


@EventBusSubscriber(modid = ThisGCsShenanigans.MODID)
public class TGCSCommonEvents {

    public static void onBoot(IEventBus modBus) {
        // Deep breath enchantment
        NeoForge.EVENT_BUS.register(DeepBreathEnchantmentHitEvent.class);
    }

    @SubscribeEvent
    public static void worldMechanics(ServerStartingEvent event) {

        if (ModList.get().isLoaded("spawn") && Config.DISABLE_CRAB.get()) {
            NeoForge.EVENT_BUS.register(DisableEntity.class);
        }

    }

    @SubscribeEvent
    public static void unloadWorldMechanics(ServerStoppingEvent event) {
        NeoForge.EVENT_BUS.unregister(DisableEntity.class);
    }



}

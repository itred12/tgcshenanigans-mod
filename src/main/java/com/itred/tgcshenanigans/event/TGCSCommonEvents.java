package com.itred.tgcshenanigans.event;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.event.common.DeepBreathEnchantmentHitEvent;
import com.itred.tgcshenanigans.event.common.configurable.DisableEntity;
import com.itred.tgcshenanigans.event.common.configurable.DurabilityRework;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
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

        if (!Config.DISABLED_ENTITIES_LIST.get().isEmpty()) {
            DisableEntity.updateEntityList();
            NeoForge.EVENT_BUS.register(DisableEntity.class);
        }

        if (Config.DURABILITY_REWORK.get()) {
            NeoForge.EVENT_BUS.register(DurabilityRework.class);
        }

    }

    @SubscribeEvent
    public static void unloadWorldMechanics(ServerStoppingEvent event) {
        NeoForge.EVENT_BUS.unregister(DisableEntity.class);
        NeoForge.EVENT_BUS.unregister(DurabilityRework.class);
    }



}

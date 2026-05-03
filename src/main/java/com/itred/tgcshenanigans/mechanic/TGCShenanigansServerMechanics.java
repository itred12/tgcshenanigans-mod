package com.itred.tgcshenanigans.mechanic;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.mechanic.server.DisableEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = ThisGCsShenanigans.MODID)
public class TGCShenanigansServerMechanics {


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

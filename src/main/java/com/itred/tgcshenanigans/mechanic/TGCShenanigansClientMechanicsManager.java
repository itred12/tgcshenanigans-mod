package com.itred.tgcshenanigans.mechanic;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.mechanic.client.BlueAxolotlPing;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;


@EventBusSubscriber(modid = ThisGCsShenanigans.MODID, value = Dist.CLIENT)
public class TGCShenanigansClientMechanicsManager {

    /*
    private static final Map<Class<?>, ModConfigSpec.ConfigValue<?>> CLIENT_MECHANICS = Map.of(
            BlueAxolotlPing.class, Config.ENABLE_BLUE_AXOLOTL_PING
    );

     */

    @SubscribeEvent
    public static void worldMechanics(LevelEvent.Load event) {

        if (Config.ENABLE_BLUE_AXOLOTL_PING.get()) {
            NeoForge.EVENT_BUS.register(BlueAxolotlPing.class);
        }

    }

    @SubscribeEvent
    public static void unloadWorldMechanics(LevelEvent.Unload event) {
        NeoForge.EVENT_BUS.unregister(BlueAxolotlPing.class);
    }



}

package com.itred.tgcshenanigans.event;

import com.itred.tgcshenanigans.event.common.BluntCleaverUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;


// @EventBusSubscriber(modid = ThisGCsShenanigans.MODID)
public class TGCSCommonEvents {

    public static void onBoot(IEventBus modBus) {
        // Deep breath enchantment
        // NeoForge.EVENT_BUS.register(DeepBreathEnchantmentHitEvent.class);
        NeoForge.EVENT_BUS.register(BluntCleaverUtils.class);
    }





}

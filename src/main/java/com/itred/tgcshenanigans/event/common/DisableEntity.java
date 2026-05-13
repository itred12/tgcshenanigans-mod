package com.itred.tgcshenanigans.event.common;

import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.List;

public class DisableEntity {

    private static final List<String> DISABLED_RAWNAMES = List.of(
            "entity.spawn.spider_crab"
    );

    @SubscribeEvent
    static void onMobSpawn(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        if (DISABLED_RAWNAMES.contains(entity.getType().toString())) {
            event.setCanceled(true);
        }

    }
}

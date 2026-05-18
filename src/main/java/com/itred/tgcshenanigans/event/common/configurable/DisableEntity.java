package com.itred.tgcshenanigans.event.common.configurable;

import com.itred.tgcshenanigans.Config;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.ArrayList;
import java.util.List;

public class DisableEntity {

    private static final List<EntityType<?>> DISABLED_ENTITIES = new ArrayList<>();


    public static void updateEntityList() {

        DISABLED_ENTITIES.clear();

        List<? extends String> disabledResourceLocations = Config.DISABLED_ENTITIES_LIST.get();

        for (String name : disabledResourceLocations) {
            ResourceLocation location = ResourceLocation.tryParse(name);
            if (location != null) {
                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(name));
                DISABLED_ENTITIES.add(entityType);
            }

        }


    }

    @SubscribeEvent
    private static void onMobSpawn(EntityJoinLevelEvent event) {
        EntityType<?> entityType = event.getEntity().getType();

        if (DISABLED_ENTITIES.contains(entityType)) {
            event.setCanceled(true);
        }

    }
}

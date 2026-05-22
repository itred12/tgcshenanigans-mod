package com.itred.tgcshenanigans.config.server;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.config.IConfiguredEventHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.ArrayList;
import java.util.List;

public class DisableEntity implements IConfiguredEventHandler {

    private static final List<EntityType<?>> DISABLED_ENTITIES = new ArrayList<>();

    @SubscribeEvent
    private void onMobSpawn(EntityJoinLevelEvent event) {
        EntityType<?> entityType = event.getEntity().getType();

        if (DISABLED_ENTITIES.contains(entityType)) {
            event.setCanceled(true);
        }

    }


    @Override
    public boolean shouldEnable() {
        return !Config.DISABLED_ENTITIES_LIST.get().isEmpty();
    }

    @Override
    public void enable(IEventBus bus, IConfiguredEventHandler instance) {

        DISABLED_ENTITIES.clear();
        List<? extends String> disabledResourceLocations = Config.DISABLED_ENTITIES_LIST.get();

        for (String name : disabledResourceLocations) {
            ResourceLocation location = ResourceLocation.tryParse(name);
            if (location != null) {
                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(name));
                DISABLED_ENTITIES.add(entityType);
            }

        }

        NeoForge.EVENT_BUS.register(instance);

    }

    @Override
    public void disable(IEventBus bus, IConfiguredEventHandler instance) {
        NeoForge.EVENT_BUS.unregister(instance);
    }


}

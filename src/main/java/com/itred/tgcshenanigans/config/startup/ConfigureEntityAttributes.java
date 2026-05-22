package com.itred.tgcshenanigans.config.startup;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.config.IConfiguredEventHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import java.util.Map;

public class ConfigureEntityAttributes implements IConfiguredEventHandler {

    private static final Map<EntityType<? extends LivingEntity>, ModConfigSpec.DoubleValue> HEALTH_MODIFIERS = Map.of(
            EntityType.ENDER_DRAGON, Config.ENDER_DRAGON_HEALTH,
            EntityType.WITHER, Config.WITHER_HEALTH,
            EntityType.WARDEN, Config.WARDEN_HEALTH
    );

    // Modify the health of entities in the above table
    @SubscribeEvent
    public void onBossAttributesLoaded(EntityAttributeModificationEvent event) {

        for (Map.Entry<EntityType<? extends LivingEntity>, ModConfigSpec.DoubleValue> entry : HEALTH_MODIFIERS.entrySet()) {

            EntityType<? extends LivingEntity> entity = entry.getKey();
            ModConfigSpec.DoubleValue health = entry.getValue();

            if (!health.get().equals(health.getDefault())) {
                event.add(entity, Attributes.MAX_HEALTH, health.get());
            }

        }

    }

    @Override
    public boolean shouldEnable() {
        return true; // I *could* check the table to not enable this if it's changed, but this event is fired like. Once. I think its fine.
    }

    @Override
    public void enable(IEventBus bus, IConfiguredEventHandler instance) {
        bus.register(instance);
    }

    @Override
    public void disable(IEventBus bus, IConfiguredEventHandler instance) {
        // Cant disable a startup thing, lol.
    }
}

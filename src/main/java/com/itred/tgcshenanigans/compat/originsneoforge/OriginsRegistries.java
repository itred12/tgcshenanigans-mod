package com.itred.tgcshenanigans.compat.originsneoforge;

import com.iafenvoy.origins.data.action.ActionRegistries;
import com.iafenvoy.origins.data.action.EntityAction;
import com.iafenvoy.origins.data.power.Power;
import com.iafenvoy.origins.data.power.PowerRegistries;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.compat.originsneoforge.entityaction.AddVelocityAction;
import com.itred.tgcshenanigans.compat.originsneoforge.power.JaggedHoverPower;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OriginsRegistries {

    public static final DeferredRegister<MapCodec<? extends EntityAction>> ENTITY_ACTION_REGISTRY;
    public static final DeferredHolder<MapCodec<? extends EntityAction>, MapCodec<AddVelocityAction>> ADD_VELOCITY;

    public static final DeferredRegister<MapCodec<? extends Power>> POWER_REGISTRY;
    public static final DeferredHolder<MapCodec<? extends Power>, MapCodec<JaggedHoverPower>> JAGGED_HOVER;

    static {
        ENTITY_ACTION_REGISTRY = DeferredRegister.create(ActionRegistries.ENTITY_ACTION, ThisGCsShenanigans.MODID);
        POWER_REGISTRY = DeferredRegister.create(PowerRegistries.POWER_TYPE, ThisGCsShenanigans.MODID);

        ADD_VELOCITY = ENTITY_ACTION_REGISTRY.register("add_velocity", () -> AddVelocityAction.CODEC);
        JAGGED_HOVER = POWER_REGISTRY.register("jagged_hover", () -> JaggedHoverPower.CODEC);
    }
}

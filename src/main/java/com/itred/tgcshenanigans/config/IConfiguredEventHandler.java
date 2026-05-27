package com.itred.tgcshenanigans.config;

import net.neoforged.bus.api.IEventBus;

public interface IConfiguredEventHandler {
    default boolean shouldEnable() {
        return true;
    };
    void enable(IEventBus bus, IConfiguredEventHandler instance);
    void disable(IEventBus bus, IConfiguredEventHandler instance);
}

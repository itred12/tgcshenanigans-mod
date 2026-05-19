package com.itred.tgcshenanigans.particle;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TGCSParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES_REGISTRY = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ThisGCsShenanigans.MODID);

    public static final Supplier<SimpleParticleType> RATIO_PARTICLE = PARTICLES_REGISTRY.register("ratio_particle",
            () -> new SimpleParticleType(true)
            );

    public static void register(IEventBus bus) {
        PARTICLES_REGISTRY.register(bus);
    }
}

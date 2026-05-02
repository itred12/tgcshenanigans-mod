package com.itred.tgcshenanigans.component;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.CrystallineDiscItem;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class TGCSDataComponents {

    @SuppressWarnings("removal")
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(ThisGCsShenanigans.MODID);

    // Component used to store the crystalline disc's current song
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CrystallineDiscItem.CrystallineDiscSong>> CRYSTALLINE_DISC_SONG_COMPONENT = registerComponent(
            "crystalline_disc_song",
            song -> song.persistent(CrystallineDiscItem.CrystallineDiscSong.CODEC).networkSynchronized(CrystallineDiscItem.CrystallineDiscSong.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CRYSTALLINE_DISC_COUNTER_COMPONENT = registerComponent(
            "crystalline_disc_counter",
            builder -> builder.persistent(Codec.INT)
    );

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerAll(IEventBus bus) {
        DATA_COMPONENT_TYPES.register(bus);
    }

}

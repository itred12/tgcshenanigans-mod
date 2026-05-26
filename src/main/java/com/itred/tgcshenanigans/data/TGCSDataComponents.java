package com.itred.tgcshenanigans.data;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.custom.CrystallineDiscItem;
import com.itred.tgcshenanigans.item.custom.CrystallineDiscItem.CrystallineDiscSong;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class TGCSDataComponents {

    @SuppressWarnings("removal")
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(ThisGCsShenanigans.MODID);

    // Component used to store the crystalline disc's current song
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CrystallineDiscSong>> CRYSTALLINE_DISC_SONG_COMPONENT = registerComponent(
            "crystalline_disc_song",
            song -> song.persistent(CrystallineDiscSong.CODEC).networkSynchronized(CrystallineDiscItem.CrystallineDiscSong.STREAM_CODEC));

    // Component used to store the crystalline disc's transformation progress
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CRYSTALLINE_DISC_PROGRESS = registerComponent(
            "crystalline_disc_progress",
            builder -> builder.persistent(Codec.INT)
    );

    // Component used to store the crystalline disc's check progress (gives me something to reset when conditions arent fulfilled without rolling back transform progress)
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CRYSTALLINE_DISC_LISTEN_COUNTER = registerComponent(
            "crystalline_disc_listen_counter",
            builder -> builder.persistent(Codec.INT)
    );

    // Component used to store the song in the crystalline disc's current biome
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CrystallineDiscSong>> CRYSTALLINE_DISC_FLOATINGSONG = registerComponent(
            "crystalline_disc_floatingsong",
            song -> song.persistent(CrystallineDiscSong.CODEC).networkSynchronized(CrystallineDiscItem.CrystallineDiscSong.STREAM_CODEC));


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> DEEP_BREATH_STACKS = registerComponent(
            "deepbreath_damage_stacks",
            builder ->
                    builder.networkSynchronized(ByteBufCodecs.FLOAT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> DEEP_BREATH_LAST_DEALT_DAMAGE = registerComponent(
            "deepbreath_last_dealt_damage",
            builder ->
                    builder.networkSynchronized(ByteBufCodecs.VAR_LONG)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> DEEP_BREATH_STACK_CAP = registerComponent(
            "deepbreath_stack_cap",
            builder -> builder.persistent(Codec.INT)
    );

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerAll(IEventBus bus) {
        DATA_COMPONENT_TYPES.register(bus);
    }

}

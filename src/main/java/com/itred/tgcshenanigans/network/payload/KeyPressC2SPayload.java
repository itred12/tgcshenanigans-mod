package com.itred.tgcshenanigans.network.payload;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record KeyPressC2SPayload(String key, boolean state) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<KeyPressC2SPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "key_press_c2s"));
    public static final StreamCodec<RegistryFriendlyByteBuf, KeyPressC2SPayload> STREAM_CODEC;

    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    static {
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, KeyPressC2SPayload::key,
                ByteBufCodecs.BOOL, KeyPressC2SPayload::state,
                KeyPressC2SPayload::new);
    }
}
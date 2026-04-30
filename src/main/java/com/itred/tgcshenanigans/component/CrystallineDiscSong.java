package com.itred.tgcshenanigans.component;

import com.itred.tgcshenanigans.datagen.TGCSBiomeTagProvider;
import com.itred.tgcshenanigans.item.TGCSItems;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

// Mostly referenced from net.minecraft.world.item.Rarity
public enum CrystallineDiscSong implements StringRepresentable, IExtensibleEnum {

    AIZO(0, "aizo", TGCSBiomeTagProvider.AIZO_BIOMES, TGCSItems.DISC_AIZO);

    private final int id;
    private final String name;
    private final TagKey<Biome> biomeTargets;
    private final DeferredItem<Item> itemTarget;

    public static final Codec<CrystallineDiscSong> CODEC = StringRepresentable.fromValues(CrystallineDiscSong::values);
    public static final IntFunction<CrystallineDiscSong> BY_ID = ByIdMap.continuous((component) -> component.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, CrystallineDiscSong> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, (component) -> component.id);


    CrystallineDiscSong(int id, String name, TagKey<Biome> biomeTag, DeferredItem<Item> transformsInto) {
        this.id = id;
        this.name = name;
        this.biomeTargets = biomeTag;
        this.itemTarget = transformsInto;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public TagKey<Biome> getBiomeTag() {
        return biomeTargets;
    }

    public DeferredItem<Item> getItemTarget() {
        return itemTarget;
    }



}

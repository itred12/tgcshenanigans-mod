package com.itred.tgcshenanigans.tag;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class TGCSBiomeTags {

    public static final TagKey<Biome> CRYSTALLINE_DISC_ALLBIOMES = createBiomeTag("crystalline_disc_allbiomes");

    public static final TagKey<Biome> AIRWAVES_BIOMES = createBiomeTag("airwaves_biomes");
    public static final TagKey<Biome> CATSWING_BIOMES = createBiomeTag("catswing_biomes");
    public static final TagKey<Biome> FIREPLACE_BIOMES = createBiomeTag("fireplace_biomes");
    public static final TagKey<Biome> AIZO_BIOMES = createBiomeTag("aizo_biomes");

    private static TagKey<Biome> createBiomeTag(String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name));
    }
}

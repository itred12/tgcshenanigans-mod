package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TGCSBiomeTagProvider extends BiomeTagsProvider {

    public TGCSBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, ThisGCsShenanigans.MODID, existingFileHelper);
    }

    // Keys
    public static final TagKey<Biome> CRYSTALLINE_DISC_ALLBIOMES = createBiomeTag("crystalline_disc_allbiomes");

    public static final TagKey<Biome> AIRWAVES_BIOMES = createBiomeTag("airwaves_biomes");
    public static final TagKey<Biome> CATSWING_BIOMES = createBiomeTag("catswing_biomes");
    public static final TagKey<Biome> FIREPLACE_BIOMES = createBiomeTag("fireplace_biomes");
    public static final TagKey<Biome> AIZO_BIOMES = createBiomeTag("aizo_biomes");


    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {


        // "Air and waves"
        tag(AIRWAVES_BIOMES)
                .add(Biomes.BEACH)
                .add(Biomes.SNOWY_BEACH);

        // "Field of pink and gold"
        tag(CATSWING_BIOMES)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.PLAINS)
                .add(Biomes.MEADOW)
                .add(Biomes.FLOWER_FOREST);

        // Fits the vibes
        tag(FIREPLACE_BIOMES)
                .add(Biomes.TAIGA)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);

        // Kinda like the noir cityscape of the s3 opening
        tag(AIZO_BIOMES)
                .add(Biomes.BASALT_DELTAS)
                .add(Biomes.SOUL_SAND_VALLEY);

        // A tag for all of these
        tag(CRYSTALLINE_DISC_ALLBIOMES)
                .addTag(AIRWAVES_BIOMES)
                .addTag(CATSWING_BIOMES)
                .addTag(FIREPLACE_BIOMES)
                .addTag(AIZO_BIOMES);
    }

    private static TagKey<Biome> createBiomeTag(String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name));
    }


}

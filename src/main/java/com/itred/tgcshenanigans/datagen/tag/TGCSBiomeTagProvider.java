package com.itred.tgcshenanigans.datagen.tag;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.tag.TGCSBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class    TGCSBiomeTagProvider extends BiomeTagsProvider {

    public TGCSBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, ThisGCsShenanigans.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {


        // "Air and waves"
        tag(TGCSBiomeTags.AIRWAVES_BIOMES)
                .add(Biomes.BEACH)
                .add(Biomes.SNOWY_BEACH);

        // "Field of pink and gold"
        tag(TGCSBiomeTags.CATSWING_BIOMES)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.MEADOW)
                .add(Biomes.FLOWER_FOREST);

        // Fits the vibes
        tag(TGCSBiomeTags.FIREPLACE_BIOMES)
                .add(Biomes.TAIGA)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);

        // Kinda like the noir cityscape of the s3 opening
        tag(TGCSBiomeTags.AIZO_BIOMES)
                .add(Biomes.BASALT_DELTAS)
                .add(Biomes.SOUL_SAND_VALLEY);

        // The ocean makes me think of the dark sanctuaries
        tag(TGCSBiomeTags.FROMNOWON_BIOMES)
                .add(Biomes.COLD_OCEAN)
                .add(Biomes.DEEP_COLD_OCEAN);

        // Waves of the starless sea
        tag(TGCSBiomeTags.DEATHODYSSEY_BIOMES)
                .add(Biomes.LUKEWARM_OCEAN)
                .add(Biomes.DEEP_LUKEWARM_OCEAN);

        // Where they fought the death paintings in S1
        tag(TGCSBiomeTags.REMEMBER_BIOMES)
                .add(Biomes.FOREST)
                .add(Biomes.BIRCH_FOREST)
                .add(Biomes.RIVER);


        // A tag for all of these
        tag(TGCSBiomeTags.CRYSTALLINE_DISC_ALLBIOMES)
                .addTag(TGCSBiomeTags.AIRWAVES_BIOMES)
                .addTag(TGCSBiomeTags.CATSWING_BIOMES)
                .addTag(TGCSBiomeTags.FIREPLACE_BIOMES)
                .addTag(TGCSBiomeTags.AIZO_BIOMES)
                .addTag(TGCSBiomeTags.FROMNOWON_BIOMES)
                .addTag(TGCSBiomeTags.DEATHODYSSEY_BIOMES)
                .addTag(TGCSBiomeTags.REMEMBER_BIOMES);
    }




}

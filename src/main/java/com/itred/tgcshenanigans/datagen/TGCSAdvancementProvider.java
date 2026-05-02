package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TGCSAdvancementProvider extends AdvancementProvider {
    /**
     * Constructs an advancement provider using the generators to write the
     * advancements to a file.
     *
     * @param output             the target directory of the data generator
     * @param registries         a future of a lookup for registries and their objects
     * @param existingFileHelper a helper used to find whether a file exists
     *        the generators used to create the advancements
     */
    public TGCSAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new TGCSAdvancementGenerator()));
    }

    private static final class TGCSAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {

        @Override
        public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<AdvancementHolder> saver, @NotNull ExistingFileHelper existingFileHelper) {

            Advancement.Builder craftedCustomDisc = Advancement.Builder.advancement()
                    .parent(AdvancementSubProvider.createPlaceholder("minecraft:adventure/root"))
                    .display(
                            new ItemStack(TGCSItems.CRYSTALLINE_DISC_VOICELESS.get()),
                            Component.translatable("advancements.tgcshenanigans.craft_custom_disc.title"),
                            Component.translatable("advancements.tgcshenanigans.craft_custom_disc.description"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            true
                    )
                    .rewards(new AdvancementRewards.Builder().addExperience(100))
                    .addCriterion(
                            "craft_disc_aizo",
                            RecipeCraftedTrigger.TriggerInstance.craftedItem(
                                    ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "crystalline_disc_label_smithing_aizo")
                            )
                    )

                    .requirements(AdvancementRequirements.anyOf(
                            List.of("craft_disc_aizo")
                    ));

            craftedCustomDisc.save(saver, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "craft_custom_disc"), existingFileHelper);


        }
    }
}

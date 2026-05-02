package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.component.TGCSDataComponents;
import com.itred.tgcshenanigans.datagen.TGCSBiomeTagProvider;
import com.itred.tgcshenanigans.tag.TGCSBiomeTags;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;

public class CrystallineDiscItem extends Item {

    // Interval, in ticks, that the item checks for being in a specific biome
    private static final int CHECK_INTERVAL = 100;
    private int tickTimer = 0;

    public CrystallineDiscItem(Properties properties) {
        super(properties);
        properties.component(TGCSDataComponents.CRYSTALLINE_DISC_COUNTER_COMPONENT, 0);
    }


    // Returns the Crystalline Disc biome tag the entity holding this item is in, if any.
    private static Optional<CrystallineDiscSong> findTagFromCurrentBiome(Holder<Biome> biome) {

        // Technically more performant by only going through every tag if we know one of them is applicable??
        if (!biome.is(TGCSBiomeTags.CRYSTALLINE_DISC_ALLBIOMES)) {
            return Optional.empty();
        }
        for (CrystallineDiscSong validSong : CrystallineDiscSong.values()) {
            if (biome.is(validSong.getBiomeTag())) return Optional.of(validSong);
        }
        return Optional.empty();

    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {

        if (stack.has(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT) && stack.getOrDefault(TGCSDataComponents.CRYSTALLINE_DISC_COUNTER_COMPONENT, 0) >= 2) {

            CrystallineDiscSong song = stack.getOrDefault(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT, CrystallineDiscSong.AIZO);
            tooltipComponents.add(Component.translatable(song.description)
                    .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));

        } else  {

            tooltipComponents.add(Component.translatable("item.tgcshenanigans.crystalline_disc_voiceless.description")
                    .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
        return slotChanged; // Bandaid fix to make it not constantly reequip while held and the timer is incrementing
    }

    // Check biome while held to transform
    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);


        // The check is kinda expensive, so only do it every once-in-a-while
        this.tickTimer++;
        if (tickTimer > CHECK_INTERVAL) {
            this.tickTimer = 0;

            if (level.isClientSide()) {
                return;
            }

            // Mainhand or offhand
            if (isSelected || (slotId == 40) && entity instanceof Player) {

                Player player = (Player) entity;

                // Get the biome and find which of the target tags its in
                Holder<Biome> currentBiome = level.getBiome(player.blockPosition());
                Optional<CrystallineDiscSong> foundSong = findTagFromCurrentBiome(currentBiome);

                if (foundSong.isPresent()) {

                    CrystallineDiscSong targetSong = foundSong.get();
                    int currentCounter = stack.getOrDefault(TGCSDataComponents.CRYSTALLINE_DISC_COUNTER_COMPONENT, 0);
                    CrystallineDiscSong listeningTo = stack.get(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT);

                    // If no biome is set, we can start listening.
                    if (listeningTo == null) {
                        stack.set(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT, targetSong);
                    }

                    // If the disc is held in its stored biome, increment the timer.
                    if (listeningTo == targetSong) {
                        stack.set(TGCSDataComponents.CRYSTALLINE_DISC_COUNTER_COMPONENT, currentCounter + 1);

                        switch (currentCounter + 1) {
                            // 10 seconds
                            case 2:
                                player.sendSystemMessage(
                                        Component.translatable("message.tgcshenanigans.crystalline_disc.start")
                                                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                );
                                break;
                            // 30 seconds
                            case 6:
                                player.sendSystemMessage(
                                        Component.translatable("message.tgcshenanigans.crystalline_disc.listen_1")
                                                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                );
                                break;

                            // 50 seconds
                            case 10:
                                player.sendSystemMessage(
                                        Component.translatable("message.tgcshenanigans.crystalline_disc.listen_2")
                                                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                );
                                break;

                            // 70 seconds
                            case 14:
                                player.sendSystemMessage(
                                        Component.translatable("message.tgcshenanigans.crystalline_disc.listen_3")
                                                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                );
                                ItemStack copy = stack.copyAndClear();
                                ItemStack newDisc = new ItemStack(listeningTo.getItemTarget().get(), 1);
                                player.getInventory().setItem(slotId, newDisc);
                                break;
                        }
                    }


                }

            }
        }

    }


    // Mostly referenced from net.minecraft.world.item.Rarity
    public enum CrystallineDiscSong implements StringRepresentable, IExtensibleEnum {

        AIZO(0, "aizo", TGCSBiomeTags.AIZO_BIOMES, TGCSItems.CRYSTALLINE_DISC_AIZO, "item.tgcshenanigans.crystalline_disc_voiceless.description_aizo");

        public static final Codec<CrystallineDiscSong> CODEC = StringRepresentable.fromValues(CrystallineDiscSong::values);
        public static final IntFunction<CrystallineDiscSong> BY_ID = ByIdMap.continuous((component) -> component.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, CrystallineDiscSong> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, (component) -> component.id);
        private final int id;
        private final String name;
        private final TagKey<Biome> biomeTargets;
        private final DeferredItem<Item> itemTarget;
        private final String description;


        CrystallineDiscSong(int id, String name, TagKey<Biome> biomeTag, DeferredItem<Item> transformsInto, String description) {
            this.id = id;
            this.name = name;
            this.biomeTargets = biomeTag;
            this.itemTarget = transformsInto;
            this.description = description;
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

        public String getDescription() {return description;}



    }




}

package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.component.TGCSDataComponents;
import com.itred.tgcshenanigans.tag.TGCSBiomeTags;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
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
    private static final int CHECK_INTERVAL = 80;
    private int tickTimer = 0;
    private int incrementTimer = 0;
    private CrystallineDiscSong floatingSong;
    private double storedX = 0.0;
    private double storedY = 0.0;
    private double storedZ = 0.0;


    public CrystallineDiscItem(Properties properties) {
        super(properties);
        properties.component(TGCSDataComponents.CRYSTALLINE_DISC_PROGRESS, 0);
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

    private boolean checkPlayerMovement(Player player) {
        boolean hasStayedStill = false;

        if (player.distanceToSqr(this.storedX, this.storedY, this.storedZ) < 0.010000000000000002) {
            hasStayedStill = true;
        } else {
            this.storedX = player.position().x;
            this.storedY = player.position().y;
            this.storedZ = player.position().z;
        }

        return hasStayedStill;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {

        if (stack.has(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT)) {

            CrystallineDiscSong song = stack.getOrDefault(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT, CrystallineDiscSong.AIZO);
            tooltipComponents.add(Component.translatable("item.tgcshenanigans.crystalline_disc_voiceless.description_" + song.name)
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
                Optional<CrystallineDiscSong> biomeSong = findTagFromCurrentBiome(currentBiome);

                CrystallineDiscSong currentSong = stack.get(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT);
                int currentProgress = stack.getOrDefault(TGCSDataComponents.CRYSTALLINE_DISC_PROGRESS, 0);

                // If the player enters a biome for a disc and doesnt have any song currently tied to the disc,
                    // Store the biome temporarily, start counting up to 20 (4 cycles).
                        // Reset this counter if the player exits the biome, stops holding the disc, or moves.
                    // If no biome is stored or tied to the disc, hint the player that the biome is a disc biome.

                // If the 20 second counter reaches the end, lock the disc in, increment its counter if already locked. Continue as normal from there.

                if (biomeSong.isPresent()) {
                    CrystallineDiscSong foundSong = biomeSong.get();

                    // If the biome song is the equal to the stored song
                    // OR there is a floating song and the floating song is equal to the biome song
                    // AND for any of those the player hasn't moved,
                    // Increment the timer.
                    if ((foundSong == currentSong || (floatingSong != null && floatingSong == foundSong)) && checkPlayerMovement(player)) {
                        incrementTimer++;
                    } else {
                        incrementTimer = 0;
                        // Only give the hint if the player doesnt currently have a song on the disc
                        if (currentSong == null && (floatingSong == null || floatingSong != foundSong)) {
                            player.sendSystemMessage(
                                    Component.translatable("message.tgcshenanigans.crystalline_disc.hint").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                            );
                        }
                        floatingSong = foundSong;

                    }

                    if (incrementTimer > 4) {
                        incrementTimer = 0;

                        if (currentSong == null) {
                            stack.set(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT, foundSong);
                            stack.set(TGCSDataComponents.CRYSTALLINE_DISC_PROGRESS, 0);
                            player.sendSystemMessage(
                                    Component.translatable("message.tgcshenanigans.crystalline_disc.start").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                            );
                        } else {
                            stack.set(TGCSDataComponents.CRYSTALLINE_DISC_PROGRESS, currentProgress + 1);

                            switch (currentProgress + 1) {
                                case 1:
                                    player.sendSystemMessage(
                                            Component.translatable("message.tgcshenanigans.crystalline_disc.listen_1").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                    );
                                break;
                                case 2:
                                player.sendSystemMessage(
                                        Component.translatable("message.tgcshenanigans.crystalline_disc.listen_2").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                );
                                break;
                                case 3:
                                    player.sendSystemMessage(
                                            Component.translatable("message.tgcshenanigans.crystalline_disc.listen_3").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                    );
                                    ItemStack copy = stack.copyAndClear();
                                    ItemStack newDisc = new ItemStack(currentSong.getItemTarget().get(), 1);
                                    player.getInventory().setItem(slotId, newDisc);
                                break;

                            }
                        }

                    }



                } else {
                    incrementTimer = 0;
                    floatingSong = null;
                }


            } else {
                incrementTimer = 0;
                floatingSong = null;
            }
        }

    }


    // Mostly referenced from net.minecraft.world.item.Rarity
    public enum CrystallineDiscSong implements StringRepresentable, IExtensibleEnum {

        AIZO(0, "aizo", TGCSBiomeTags.AIZO_BIOMES, TGCSItems.CRYSTALLINE_DISC_AIZO),
        FIREPLACE(1, "fireplace", TGCSBiomeTags.FIREPLACE_BIOMES, TGCSItems.CRYSTALLINE_DISC_FIREPLACE),
        CATSWING(2, "catswing", TGCSBiomeTags.CATSWING_BIOMES, TGCSItems.CRYSTALLINE_DISC_CATSWING),
        FROMNOWON(3, "fromnowon", TGCSBiomeTags.FROMNOWON_BIOMES, TGCSItems.CRYSTALLINE_DISC_FROMNOWON);

        public static final Codec<CrystallineDiscSong> CODEC = StringRepresentable.fromValues(CrystallineDiscSong::values);
        public static final IntFunction<CrystallineDiscSong> BY_ID = ByIdMap.continuous((component) -> component.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, CrystallineDiscSong> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, (component) -> component.id);
        private final int id;
        private final String name;
        private final TagKey<Biome> biomeTargets;
        private final DeferredItem<Item> itemTarget;


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




}

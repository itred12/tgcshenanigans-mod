package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.component.CrystallineDiscSong;
import com.itred.tgcshenanigans.component.TGCSDataComponents;
import com.itred.tgcshenanigans.datagen.TGCSBiomeTagProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class CrystallineDiscItem extends Item {

    // Interval, in ticks, that the item checks for being in a specific biome
    private static final int CHECK_INTERVAL = 100;
    private int tickTimer = 0;
    // TODO: change to component so it saves when leaving/rejoining
    private CrystallineDiscSong listeningTo;



    public CrystallineDiscItem(Properties properties) {
        super(properties);
        properties.component(TGCSDataComponents.CRYSTALLINE_DISC_COUNTER_COMPONENT, 0);
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {


        if (stack.has(TGCSDataComponents.CRYSTALLINE_DISC_COUNTER_COMPONENT)) {
            tooltipComponents.add(Component.translatable("item.tgcshenanigans.crystalline_disc.description_ready")
                    .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        } else  {
            tooltipComponents.add(Component.translatable("item.tgcshenanigans.crystalline_disc.description")
                    .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
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

                // Get the biome and find which of the target tags its in
                Holder<Biome> currentBiome = level.getBiome(entity.blockPosition());
                Optional<CrystallineDiscSong> foundSong = findTagFromCurrentBiome(currentBiome);

                if (foundSong.isPresent()) {

                    CrystallineDiscSong targetSong = foundSong.get();
                    int currentCounter = stack.getOrDefault(TGCSDataComponents.CRYSTALLINE_DISC_COUNTER_COMPONENT, 0);

                    // If no biome is set, we can start listening.
                    if (listeningTo == null) {
                        listeningTo = targetSong;
                    }

                    // If the disc is held in its stored biome, increment the timer.
                    if (listeningTo == targetSong) {
                        stack.set(TGCSDataComponents.CRYSTALLINE_DISC_COUNTER_COMPONENT, currentCounter + 1);

                        switch (currentCounter + 1) {
                            // 10 seconds
                            case 2:
                                entity.sendSystemMessage(
                                        Component.translatable("message.tgcshenanigans.crystalline_disc.start")
                                                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                );
                                break;
                            // 30 seconds
                            case 6:
                                entity.sendSystemMessage(
                                        Component.translatable("message.tgcshenanigans.crystalline_disc.listen_1")
                                                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                );
                                break;

                            // 50 seconds
                            case 10:
                                entity.sendSystemMessage(
                                        Component.translatable("message.tgcshenanigans.crystalline_disc.listen_2")
                                                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                );
                                break;

                            // 70 seconds
                            case 14:
                                entity.sendSystemMessage(
                                        Component.translatable("message.tgcshenanigans.crystalline_disc.listen_3")
                                                .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                                );
                                // Add enchantment glint and song
                                stack.applyComponents(DataComponentPatch.builder()
                                        .set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                                        .set(TGCSDataComponents.CRYSTALLINE_DISC_SONG_COMPONENT.get(), targetSong)
                                        .build());

                                break;
                        }
                    }


                }

            }
        }

    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
        return slotChanged; // Bandaid fix to make it not constantly reequip while held and the timer is incrementing
    }

    // Returns the Crystalline Disc biome tag the entity holding this item is in, if any.
    private static Optional<CrystallineDiscSong> findTagFromCurrentBiome(Holder<Biome> biome) {

        // Technically more performant by only going through every tag if we know one of them is applicable??
        if (!biome.is(TGCSBiomeTagProvider.CRYSTALLINE_DISC_ALLBIOMES)) {
            return Optional.empty();
        }

        for (CrystallineDiscSong validSong : CrystallineDiscSong.values()) {
            if (biome.is(validSong.getBiomeTag())) return Optional.of(validSong);
        }

        return Optional.empty();

    }




}

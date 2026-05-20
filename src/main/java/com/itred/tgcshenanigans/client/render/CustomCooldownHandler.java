package com.itred.tgcshenanigans.client.render;

import com.itred.tgcshenanigans.data.TGCSDataComponents;
import com.itred.tgcshenanigans.event.common.DeepBreathEnchantmentHitEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomCooldownHandler {

    public static void drawFauxCooldownBar(ItemStack stack, int x, int y, GuiGraphics guiGraphics, @NotNull LocalPlayer player) {

        if (stack.has(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE) && stack.has(TGCSDataComponents.DEEP_BREATH_STACK_CAP)) {

            int damageCap = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_STACK_CAP, 10);

            // Using the player's tick counter since we dont have server access
            // Seems to be tracked... Decently equally between both sides???
            // At worst I can switch Deep Breath to using this counter serverside to keep things aligned

            // Future itred here. No it wasnt that simple. thanks for asking
            long time = player.level().getGameTime();
            long lastDamageTime = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE, 0L);


            float cooldownProgress = 1 - (DeepBreathEnchantmentHitEvent.getDeepBreathStacks(player.level(), stack, damageCap) / damageCap);

            // ThisGCsShenanigans.LOGGER.info("Progress: " + String.valueOf(cooldownProgress));
            // ThisGCsShenanigans.LOGGER.info("Current time: " + String.valueOf(time));
            // ThisGCsShenanigans.LOGGER.info("Last damage time: " + String.valueOf(lastDamageTime));


            if (cooldownProgress > 0) {

                int i1 = y + Mth.floor(15.5F * (1.0F - cooldownProgress));
                int j1 = i1 + Mth.ceil(15.5F * cooldownProgress);
                guiGraphics.fill(RenderType.guiOverlay(), x, i1, x + 16, j1, FastColor.ARGB32.color(100, 0, 0, 255));
            }

        }

    }
}

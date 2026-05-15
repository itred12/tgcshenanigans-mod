package com.itred.tgcshenanigans.mixin.client;

import com.itred.tgcshenanigans.component.TGCSDataComponents;
import com.itred.tgcshenanigans.event.common.DeepBreathEnchantmentHitEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


// Not "DrawContext", but "GuiGraphics"??? "renderItemDecoractions"?? WHy are Mojang mappings whimsical????
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int color) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V")
    )
    private void overrideItemCooldownVisuals(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {


        // Only do any of this on reviver items. May eventually update this to work on an item tag.
        if (stack.has(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE) && stack.has(TGCSDataComponents.DEEP_BREATH_STACK_CAP)) {

            int deepBreathStackCap = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_STACK_CAP, 10);

            LocalPlayer player = this.minecraft.player;
            if (player == null) {
                return;
            }

            // Using the player's tick counter since we dont have server access
            // Seems to be tracked... Decently equally between both sides???
            // At worst I can switch Deep Breath to using this counter serverside to keep things aligned
            int time = player.tickCount;
            int lastDamageTime = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE, 0);
            float differenceInSeconds = (float) (time - lastDamageTime) / 20;

            float currentDamageStacks = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_STACKS, (float) deepBreathStackCap);

            if (time > lastDamageTime && differenceInSeconds >= 1) {

                float rechargeTime = differenceInSeconds - 1;
                float giveBack = Math.min(1, rechargeTime / DeepBreathEnchantmentHitEvent.MAX_RECHARGE_TIME);
                currentDamageStacks = Math.min(
                        deepBreathStackCap,
                        currentDamageStacks + (deepBreathStackCap * giveBack)
                );
            }

            float cooldownProgress = 1 - (currentDamageStacks / deepBreathStackCap);

            // ThisGCsShenanigans.LOGGER.info(String.valueOf(currentDamageStacks));
            if (cooldownProgress > 1) {
                return;
            }

            // Below is an excerpt from what Minecraft does to render the overlay for item cooldowns.
            // I can guess that the 16.0f refers to the length/width of the item slot, and this is slowly filling/unfilling it.
            // I'll probably fully decipher it eventually, but for now, all that we need to know is that "f" (reinterpreted as cooldownProgress) is used as the "cooldown level" here.

            if (cooldownProgress > 0) {
                int i1 = y + Mth.floor(15.5F * (1.0F - cooldownProgress));
                int j1 = i1 + Mth.ceil(15.5F * cooldownProgress);
                this.fill(RenderType.guiOverlay(), x, i1, x + 16, j1, FastColor.ARGB32.color(100, 0, 0, 255));
            }

            }
        }
}

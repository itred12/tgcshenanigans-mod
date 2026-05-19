package com.itred.tgcshenanigans.mixin.client;

import com.itred.tgcshenanigans.client.render.CustomCooldownHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
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
            at = @At(value = "HEAD")
    )
    private void tgcshenanigans$overrideItemCooldownVisuals(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
        LocalPlayer player = this.minecraft.player;
        if (player != null) {
            CustomCooldownHandler.drawFauxCooldownBar(stack, x, y, guiGraphics, player);
        }


    }
}

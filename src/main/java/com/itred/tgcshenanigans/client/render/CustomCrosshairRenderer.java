package com.itred.tgcshenanigans.client.render;

import com.itred.tgcshenanigans.TGCSUtils;
import com.itred.tgcshenanigans.data.TGCSAttachments;
import com.itred.tgcshenanigans.item.custom.BluntCleaverItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Matrix4fStack;

public class CustomCrosshairRenderer {


    public static final ResourceLocation LINE_LOCATION_WITH_MARKER = TGCSUtils.modLocation("textures/gui/ratiocrosshairtex.png");
    public static final ResourceLocation LINE_LOCATION = TGCSUtils.modLocation("textures/gui/ratiocrosshairtexnored.png");

    protected static boolean isVanillaCrosshairDisabled = false;

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Options options = mc.options;
        LocalPlayer player = mc.player;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true);

        if (
                player == null
                        || !options.getCameraType().isFirstPerson()
                        || mc.gameMode == null
                        || (mc.gameMode.getPlayerMode() == GameType.SPECTATOR && !canRenderCrosshairForSpectator(mc))
                        || options.hideGui
        ) {
            return;
        }

        if (player.getData(TGCSAttachments.NANAMI_CROSSHAIR_ATTACHMENT)) {


            int offset = Mth.floor((double)mc.getWindow().getGuiScaledHeight() / (double)10.0F);

            Camera camera = mc.gameRenderer.getMainCamera();
            Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushMatrix();
            modelViewStack.translate((float)(screenWidth / 2), (float)(screenHeight / 2), 0.0F);
            modelViewStack.rotate(Axis.XN.rotationDegrees(camera.getXRot()));
            modelViewStack.rotate(Axis.YP.rotationDegrees(camera.getYRot()));
            modelViewStack.scale(-1.0F, -1.0F, -1.0F);
            RenderSystem.renderCrosshair(10);

            int centreOriginX = (mc.getWindow().getGuiScaledWidth() - 2) / 2;
            int centreOriginY = (mc.getWindow().getGuiScaledHeight() - 1) / 2;

            int width = 256;
            int height = 128;

            guiGraphics.blit(LINE_LOCATION, centreOriginX - (width / 2), centreOriginY - (height / 2) + 2, 0f, 0f, width, height, width, height);

            int xOffset = (int) BluntCleaverItem.getRatioOffset(player);

            int color = BluntCleaverItem.canTriggerRatio(player) ?
                    FastColor.ARGB32.color(255, 255, 0, 0)
                    : FastColor.ARGB32.color(255, 255, 255, 255);


            // Draw the ring
            /*
            drawHollowSquare(guiGraphics,
                    centreOriginX + xOffset + 1,
                    centreOriginY,
                    4,
                    1,
                    color
                    );

             */

            drawVerticalLine(
                    guiGraphics,
                    centreOriginY + 7,
                    centreOriginY - 5,
                    centreOriginX + xOffset + 1,
                    0,
                    color
            );

            //

            modelViewStack.popMatrix();
        }

    }

    public static boolean isVanillaCrosshairDisabled() {
        return isVanillaCrosshairDisabled;
    }

    private static boolean canRenderCrosshairForSpectator(Minecraft mc) {
        HitResult hitResult = mc.hitResult;
        if (hitResult == null) {
            return false;
        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult)hitResult).getEntity() instanceof MenuProvider;
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)hitResult).getBlockPos();
            Level level = mc.level;
            if (level != null) {
                return level.getBlockState(blockpos).getMenuProvider(level, blockpos) != null;
            }
            return false;
        } else {
            return false;
        }
    }

    private static void drawHorizontalLine(GuiGraphics guiGraphics, int startX, int endX, int yLocation, int thickness, int color) {
        guiGraphics.fill(RenderType.GUI, startX, yLocation - thickness, endX, yLocation + thickness, color);
    }

    private static void drawVerticalLine(GuiGraphics guiGraphics, int startY, int endY, int xLocation, int thickness, int color) {
        guiGraphics.fill(RenderType.GUI, xLocation - thickness + 1, startY, xLocation + thickness, endY, color);
    }

    private static void drawHollowSquare(GuiGraphics guiGraphics, int startX, int startY, int endX, int endY, int lineThickness, int color) {

        // Top side
        drawHorizontalLine(
                guiGraphics,
                startX + 1,
                endX - 1,
                startY,
                lineThickness,
                color
        );

        // Bottom side
        drawHorizontalLine(
                guiGraphics,
                startX + 1,
                endX - 1,
                endY,
                lineThickness,
                color
        );

        // Left side
        drawVerticalLine(
                guiGraphics,
                startY - 1,
                endY + 1,
                startX,
                lineThickness,
                color
        );

        // Right side
        drawVerticalLine(
                guiGraphics,
                startY - 1,
                endY + 1,
                endX,
                lineThickness,
                color
        );


    }

    private static void drawHollowSquare(GuiGraphics guiGraphics, int centerX, int centerY, int size, int lineThickness, int color) {
        drawHollowSquare(guiGraphics, centerX - size, centerY - size, centerX + size + 1, centerY + size + 1, lineThickness, color);
    }

}

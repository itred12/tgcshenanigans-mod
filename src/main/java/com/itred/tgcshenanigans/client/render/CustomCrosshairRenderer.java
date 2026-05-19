package com.itred.tgcshenanigans.client.render;

import com.itred.tgcshenanigans.Util;
import com.itred.tgcshenanigans.data.TGCSAttachments;
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


    public static final ResourceLocation LINE_LOCATION_WITH_MARKER = Util.modLocation("textures/gui/ratiocrosshairtex.png");
    public static final ResourceLocation LINE_LOCATION = Util.modLocation("textures/gui/ratiocrosshairtexnored.png");
    public static final ResourceLocation TEST = Util.modLocation("textures/gui/img.png");

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


            int centreOriginX = (mc.getWindow().getGuiScaledWidth() - 3) / 2;
            int centreOriginY = (mc.getWindow().getGuiScaledHeight() - 3) / 2;


            int width = 256;
            int height = 128;

            guiGraphics.blit(LINE_LOCATION_WITH_MARKER, centreOriginX - (width / 2), centreOriginY - (height / 2) + 2, 0f, 0f, width, height, width, height);


            float storedRotation = player.getData(TGCSAttachments.NANAMI_CROSSHAIR_STARTANGLE);
            float currentRotation = player.getYRot();

            int XOffset = (int) ((storedRotation - currentRotation) * 2);

            // Draw the ring
            drawHollowSquare(guiGraphics,
                    centreOriginX + XOffset,
                    centreOriginY,
                    4,
                    1,
                    FastColor.ARGB32.color(255, 255, 0, 0)
                    );

            //

            /*
            guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX, centreOriginY, 4, 4, 3, 3);
            guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX + 1, centreOriginY - 4 - offset, 5, 0, 1, 4);
            guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX + 1, centreOriginY + 3 + offset, 5, 7, 1, 4);
            guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX - 4 - offset, centreOriginY + 1, 0, 5, 4, 1);
            guiGraphics.blit(HudCrosshair.CROSSHAIR_TEXTURES, centreOriginX + 3 + offset, centreOriginY + 1, 0, 5, 4, 1);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);


             */
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
            return level.getBlockState(blockpos).getMenuProvider(level, blockpos) != null;
        } else {
            return false;
        }
    }

    private static void drawHorizontalLine(GuiGraphics guiGraphics, int startX, int endX, int yLocation, int thickness, int color) {
        guiGraphics.fill(RenderType.GUI, startX, yLocation - thickness, endX, yLocation + thickness, color);
    }

    private static void drawVerticalLine(GuiGraphics guiGraphics, int startY, int endY, int xLocation, int thickness, int color) {
        guiGraphics.fill(RenderType.GUI, xLocation - thickness, startY, xLocation + thickness, endY, color);
    }

    private static void drawHollowSquare(GuiGraphics guiGraphics, int startX, int startY, int endX, int endY, int lineThickness, int color) {

        // Top side
        drawHorizontalLine(
                guiGraphics,
                startX - 1,
                endX + 1,
                startY,
                lineThickness,
                color
        );

        // Bottom side
        drawHorizontalLine(
                guiGraphics,
                startX - 1,
                endX + 1,
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
        drawHollowSquare(guiGraphics, centerX - size, centerY - size, centerX + size, centerY + size, lineThickness, color);
    }

}

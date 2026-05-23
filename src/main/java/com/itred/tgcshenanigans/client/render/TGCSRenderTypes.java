package com.itred.tgcshenanigans.client.render;

import com.itred.tgcshenanigans.block.entity.renderer.ProphecyPanelBlockEntityRenderer;
import com.itred.tgcshenanigans.event.client.TGCSShaders;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;


public class TGCSRenderTypes {

    public static final RenderStateShard.TexturingStateShard DEPTHS_TEXTURING = new RenderStateShard.TexturingStateShard(
            "depths_texturing", () -> setupGlintTexturing(8.0F), () -> RenderSystem.resetTextureMatrix()
    );


    public static final RenderType RENDERTYPE_DEPTHS = RenderType.create(
            "depths",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_SHADER)

                    .setTextureState(
                            RenderStateShard.MultiTextureStateShard.builder()
                                    .add(ProphecyPanelBlockEntityRenderer.DEPTHS, false, false)
                                    //.add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false)
                                    .build()
                    )



                    .setTexturingState(DEPTHS_TEXTURING)

                    .createCompositeState(false)
    );


    public static void setupGlintTexturing(float scale) {

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        Camera camera = minecraft.gameRenderer.getMainCamera();
        ShaderInstance shaderInstance = RenderSystem.getShader();
        if (shaderInstance == null) {
            return;
        }


        float speed = 0;
        long i = (long)((double) Util.getMillis());
        float f = (float)(i % 110000L) / 110000.0F;
        float f1 = (float)(i % 30000L) / 30000.0F;
        Matrix4f matrix4f = new Matrix4f().translation(-f * speed, f1 * speed, 0).scale(scale / 2);
        RenderSystem.setTextureMatrix(matrix4f);

        Matrix4f key = new Matrix4f().rotation(camera.rotation().conjugate());


        if (shaderInstance.getUniform("RotMat") != null ) {
            shaderInstance.getUniform("RotMat").set(key);
        }


        //ThisGCsShenanigans.LOGGER.info(String.valueOf(player));




        //RenderSystem.polygonOffset(-1.0F, -1.0F);


        /*
        for(int i2 = 0; i2 < 6; ++i2) {
            RenderSystem.setShaderTexture(i2, TGCSUtils.modLocation("textures/entity/amethyst_plate.png"));
        }

         */




        /*
        ShaderInstance shaderInstance = RenderSystem.getShader();
        if (shaderInstance == null) {
            return;
        }
        Camera camera = minecraft.gameRenderer.getMainCamera();

        Quaternionf quaternionf = camera.rotation().conjugate(new Quaternionf());
        Matrix4f matrix4f1 = new Matrix4f().rotation(quaternionf);

        VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);



        PoseStack posestack = new PoseStack();
        posestack.mulPose(matrix4f1);
        posestack.pushPose();
        //RenderSystem.setTextureMatrix(matrix4f);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

        shaderInstance.CHUNK_OFFSET.set((float) player.getX(), (float) player.getY(), (float) player.getZ());

        ThisGCsShenanigans.LOGGER.info(String.valueOf(shaderInstance.CHUNK_OFFSET.getFloatBuffer()));



        Matrix4f matrix4f2 = posestack.last().pose();
        bufferbuilder.addVertex(RenderSystem.getTextureMatrix().translation(0, 0, 1), 0.1f, 0.1f, 0f).setColor(255, 255, 255, 255);
        bufferbuilder.addVertex(RenderSystem.getTextureMatrix().translation(0, 0, 1), 0.1f, -0.1f, 0f).setColor(255, 255, 255, 255);
        bufferbuilder.addVertex(RenderSystem.getTextureMatrix().translation(0, 0, 1), -0.1f, -0.1f, 0.1f).setColor(255, 255, 255, 255);
        bufferbuilder.addVertex(RenderSystem.getTextureMatrix().translation(0, 0, 1), -0.1f, 0.1f, 0.1f).setColor(255, 255, 255, 255);
        vertexBuffer.bind();
        vertexBuffer.upload(bufferbuilder.buildOrThrow());
        VertexBuffer.unbind();

        vertexBuffer.bind();
        vertexBuffer.drawWithShader(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), shaderInstance);
        VertexBuffer.unbind();

        //RenderSystem.resetTextureMatrix();


        posestack.pushPose();
        posestack.mulPose(Axis.XP.rotationDegrees(90.0F));
        posestack.popPose();

        BufferBuilder bufferbuilder2 = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f3 = posestack.last().pose();
        bufferbuilder2.addVertex(matrix4f2, 1f, 1f, 1f).setColor(255, 255, 255, 255);

        BufferUploader.drawWithShader(bufferbuilder2.buildOrThrow());


         */

    }

    private static MeshData buildSkyDisc(Tesselator tesselator, float y) {
        float f = Math.signum(y) * 512.0F;
        float f1 = 512.0F;
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
        bufferbuilder.addVertex(0.0F, y, 0.0F);

        for (int i = -180; i <= 180; i += 45) {
            bufferbuilder.addVertex(f * Mth.cos((float)i * (float) (Math.PI / 180.0)), y, 512.0F * Mth.sin((float)i * (float) (Math.PI / 180.0)));
        }

        return bufferbuilder.buildOrThrow();
    }

}

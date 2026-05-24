package com.itred.tgcshenanigans.client.render;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.block.entity.renderer.ProphecyPanelBlockEntityRenderer;
import com.itred.tgcshenanigans.event.client.TGCSShaders;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;


public class TGCSRenderTypes {

    public static final RenderStateShard.TexturingStateShard DEPTHS_TEXTURING = new RenderStateShard.TexturingStateShard(
            "depths_texturing", () -> setupGlintTexturing(8.0F), () -> RenderSystem.resetTextureMatrix()
    );


    public static final RenderType RENDERTYPE_DEPTHS = RenderType.create(
            "depths",
            DefaultVertexFormat.POSITION_TEX_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_SHADER)
                    .setTextureState(
                            RenderStateShard.MultiTextureStateShard.builder()
                                    .add(ProphecyPanelBlockEntityRenderer.DEPTHS, false, false)
                                    .build()
                    )
                    .setTexturingState(DEPTHS_TEXTURING)
                    .createCompositeState(false)
    );


    // TODO: try to figure out how to use Minecraft.getInstance().getTimer() to use deltatime to prevent tiny animation stuttering
    public static void setupGlintTexturing(float scale) {

        float speed = (float) (5 * Config.BLOCK_ANIMATION_SPEED.get());
        long i = (long)((double) Util.getMillis());
        float f = (float)(i % 110000L) / 110000.0F;
        float f1 = (float)(i % 30000L) / 30000.0F;

        Matrix4f matrix4f = new Matrix4f().translation(-f * speed, f1 * speed, 0).scale(scale / 2);
        RenderSystem.setTextureMatrix(matrix4f);

    }

}

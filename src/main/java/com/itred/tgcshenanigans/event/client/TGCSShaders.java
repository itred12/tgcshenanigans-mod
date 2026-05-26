package com.itred.tgcshenanigans.event.client;

import com.itred.tgcshenanigans.TGCSUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import javax.annotation.Nullable;
import java.io.IOException;

public class TGCSShaders {

    @Nullable
    private static ShaderInstance depthsShader;
    @Nullable
    private static ShaderInstance depthsGlintShader;
    @Nullable
    private static ShaderInstance depthsArmorGlintShader;

    // Just kinda winging this one
    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        TGCSUtils.modLocation("rendertype_depths"),
                        DefaultVertexFormat.POSITION_TEX
                ),
                shaderInstance -> depthsShader = shaderInstance

        );

        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        TGCSUtils.modLocation("rendertype_depths_glint"),
                        DefaultVertexFormat.POSITION_TEX
                ),
                shaderInstance -> depthsGlintShader = shaderInstance

        );

        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        TGCSUtils.modLocation("rendertype_depths_armor_glint"),
                        DefaultVertexFormat.POSITION_TEX
                ),
                shaderInstance -> depthsArmorGlintShader = shaderInstance

        );
    }

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_DEPTHS_SHADER = new RenderStateShard.ShaderStateShard(
            TGCSShaders::getDepthsShader
    );

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_DEPTHS_GLINT_SHADER = new RenderStateShard.ShaderStateShard(
            TGCSShaders::getDepthsGlintShader
    );

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_DEPTHS_ARMOR_GLINT_SHADER = new RenderStateShard.ShaderStateShard(
            TGCSShaders::getDepthsGlintShader
    );


    @Nullable
    public static ShaderInstance getDepthsShader() {
        return depthsShader;
    }
    @Nullable
    public static ShaderInstance getDepthsGlintShader() {
        return depthsGlintShader;
    }
    @Nullable
    public static ShaderInstance getDepthsArmorGlintShader() {
        return depthsArmorGlintShader;
    }


}

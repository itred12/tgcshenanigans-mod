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
    }

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_DEPTHS_SHADER = new RenderStateShard.ShaderStateShard(
            TGCSShaders::getDepthsShader
    );


    @Nullable
    public static ShaderInstance getDepthsShader() {
        return depthsShader;
    }


}

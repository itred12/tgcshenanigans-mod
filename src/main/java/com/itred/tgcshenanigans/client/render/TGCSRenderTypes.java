package com.itred.tgcshenanigans.client.render;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.TGCSUtils;
import com.itred.tgcshenanigans.block.entity.renderer.ProphecyPanelBlockEntityRenderer;
import com.itred.tgcshenanigans.event.client.TGCSShaders;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.Map;


// Everyone say "Thank you Quark 1.21.1"
// The RENDERTYPE_DEPTHS stuff I did on my own before adding the enchant glint, though
// This is the second time this mod has come in super handy for teaching me how to do things like this!
public class TGCSRenderTypes {

    public static final RenderStateShard.TexturingStateShard DEPTHS_TEXTURING = new RenderStateShard.TexturingStateShard(
            "depths_texturing", () -> setupGlintTexturing(8.0F), RenderSystem::resetTextureMatrix
    );

    public static final RenderStateShard.TexturingStateShard DEPTHS_ENTITY_GLINT_TEXTURING = new RenderStateShard.TexturingStateShard(
            "depths_entity_glint_texturing", () -> setupGlintTexturing(0.16F), RenderSystem::resetTextureMatrix
    );

    public static final RenderType RENDERTYPE_DEPTHS = buildCustomRenderType("depths", ProphecyPanelBlockEntityRenderer.DEPTHS, DEPTHS_TEXTURING);
    public static final RenderType DEPTHS_GLINT = buildGlintRenderType("depths", TGCSUtils.modLocation("textures/misc/shadow_glint_ourple.png"));
    public static final RenderType DEPTHS_GLINT_TRANSLUCENT = buildGlintTranslucentRenderType("depths", TGCSUtils.modLocation("textures/misc/shadow_glint_ourple.png"));
    public static final RenderType DEPTHS_ENTITY_GLINT = buildArmorEntityGlintRenderType("depths", TGCSUtils.modLocation("textures/misc/shadow_glint_ourple.png"));
    public static final RenderType DEPTHS_GLINT_DIRECT = buildGlintDirectRenderType("depths", TGCSUtils.modLocation("textures/misc/shadow_glint_ourple.png"));
    public static final RenderType DEPTHS_ENTITY_GLINT_DIRECT = buildEntityGlintDriectRenderType("depths", TGCSUtils.modLocation("textures/misc/shadow_glint_ourple.png"));
    public static final RenderType DEPTHS_ARMOR_GLINT = buildArmorGlintRenderType("depths", TGCSUtils.modLocation("textures/misc/shadow_glint_ourple.png"));
    public static final RenderType DEPTHS_ARMOR_ENTITY_GLINT = buildArmorEntityGlintRenderType("depths", TGCSUtils.modLocation("textures/misc/shadow_glint_ourple.png"));

    // Throw together a map of the shaders and bytebuffers to register them
    public static Map<RenderType, ByteBufferBuilder> getGlintTypesAndBuffers() {
        Map<RenderType, ByteBufferBuilder> map = new Object2ObjectOpenHashMap<>();

        map.put(DEPTHS_GLINT, new ByteBufferBuilder(DEPTHS_GLINT.bufferSize()));
        map.put(DEPTHS_GLINT_TRANSLUCENT, new ByteBufferBuilder(DEPTHS_GLINT_TRANSLUCENT.bufferSize()));
        map.put(DEPTHS_ENTITY_GLINT, new ByteBufferBuilder(DEPTHS_ENTITY_GLINT.bufferSize()));
        map.put(DEPTHS_GLINT_DIRECT, new ByteBufferBuilder(DEPTHS_GLINT_DIRECT.bufferSize()));
        map.put(DEPTHS_ENTITY_GLINT_DIRECT, new ByteBufferBuilder(DEPTHS_ENTITY_GLINT_DIRECT.bufferSize()));
        map.put(DEPTHS_ARMOR_GLINT, new ByteBufferBuilder(DEPTHS_ARMOR_GLINT.bufferSize()));
        map.put(DEPTHS_ARMOR_ENTITY_GLINT, new ByteBufferBuilder(DEPTHS_ARMOR_ENTITY_GLINT.bufferSize()));

        return map;
    }





    // Block shader, uses our custom shader state
    private static RenderType buildCustomRenderType(String name, ResourceLocation texturePath, RenderStateShard.TexturingStateShard texturing) {
        return RenderType.create(
                name,
                DefaultVertexFormat.POSITION_TEX_COLOR,
                VertexFormat.Mode.QUADS,
                256,
                false,
                false,
                RenderType.CompositeState.builder()
                        .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_SHADER)
                        .setTextureState(
                                RenderStateShard.MultiTextureStateShard.builder()
                                        .add(texturePath, false, false)
                                        .build()
                        )
                        .setTexturingState(texturing)
                        .createCompositeState(false)
        );

    }

    private static RenderType buildGlintRenderType(String path, ResourceLocation texture) {
        return RenderType.create("glint_" + path, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()

                .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_GLINT_SHADER)

                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))

                .setWriteMaskState(RenderStateShard.COLOR_WRITE)

                .setCullState(RenderStateShard.NO_CULL)

                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)

                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)

                .setOutputState(RenderStateShard.MAIN_TARGET)

                .setTexturingState(TGCSRenderTypes.DEPTHS_TEXTURING)

                .setOverlayState(RenderStateShard.OVERLAY)
                .setLightmapState(RenderStateShard.LIGHTMAP)

                .createCompositeState(false));
    }

    private static RenderType buildGlintTranslucentRenderType(String path, ResourceLocation texture) {
        return RenderType.create("glint_translucent_" + path, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
                .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .setTexturingState(TGCSRenderTypes.DEPTHS_TEXTURING)
                .createCompositeState(false));
    }

    private static RenderType buildEntityGlintRenderType(String path, ResourceLocation texture) {
        return RenderType.create("entity_glint_" + path, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
                .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .setTexturingState(TGCSRenderTypes.DEPTHS_TEXTURING)
                .createCompositeState(false));
    }

    private static RenderType buildGlintDirectRenderType(String path, ResourceLocation texture) {
        return RenderType.create("glint_direct_" + path, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
                .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setTexturingState(TGCSRenderTypes.DEPTHS_TEXTURING)
                .createCompositeState(false));
    }

    private static RenderType buildEntityGlintDriectRenderType(String path, ResourceLocation texture) {
        return RenderType.create("entity_glint_direct_" + path, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
                .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setTexturingState(TGCSRenderTypes.DEPTHS_TEXTURING)
                .createCompositeState(false));
    }

    private static RenderType buildArmorGlintRenderType(String path, ResourceLocation texture) {
        return RenderType.create("armor_glint_" + path, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
                .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_ARMOR_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setTexturingState(TGCSRenderTypes.DEPTHS_ENTITY_GLINT_TEXTURING)
                .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                .createCompositeState(false));
    }

    private static RenderType buildArmorEntityGlintRenderType(String path, ResourceLocation texture) {
        return RenderType.create("armor_entity_glint_" + path, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
                .setShaderState(TGCSShaders.RENDERTYPE_DEPTHS_ARMOR_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setTexturingState(TGCSRenderTypes.DEPTHS_TEXTURING)
                .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                .createCompositeState(false));
    }


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

package com.itred.tgcshenanigans.item.custom;

import com.itred.tgcshenanigans.client.render.TGCSRenderTypes;
import com.itred.tgcshenanigans.item.ICustomGlintItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class ShadowArmorItem extends ArmorItem implements ICustomGlintItem {

    public ShadowArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public RenderType getGlint() {
        return TGCSRenderTypes.DEPTHS_GLINT;
    }

    @Override
    public RenderType getEntityGlint() {
        return TGCSRenderTypes.DEPTHS_ENTITY_GLINT;
    }

    @Override
    public RenderType getArmorGlint() {
        return TGCSRenderTypes.DEPTHS_ARMOR_GLINT;
    }
}

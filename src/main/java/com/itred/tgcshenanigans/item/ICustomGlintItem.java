package com.itred.tgcshenanigans.item;

import net.minecraft.client.renderer.RenderType;

// Items that want to have custom enchantment glint implement this
public interface ICustomGlintItem {

    RenderType getGlint();

    // Not sure if this'll just work, but it'll save me some effort if it does
    default RenderType getGlintTranslucent() {
        return this.getGlint();
    }

    default RenderType getDirectGlint() {
        return this.getGlint();
    }

    default RenderType getEntityGlint() {
        return this.getGlint();
    }

    default RenderType getArmorGlint() {
        return this.getGlint();
    }

    default RenderType getEntityGlintDirect() {
        return this.getEntityGlint();
    }

    default RenderType getArmorEntityGlint() {
        return this.getArmorGlint();
    }

}

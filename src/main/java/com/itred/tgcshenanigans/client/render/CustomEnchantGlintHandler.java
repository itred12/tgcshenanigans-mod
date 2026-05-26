package com.itred.tgcshenanigans.client.render;

import com.itred.tgcshenanigans.item.ICustomGlintItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class CustomEnchantGlintHandler {

    private static final ThreadLocal<ItemStack> currentItem = new ThreadLocal<>();

    public static void setTargetStack(ItemStack stack) {
        currentItem.set(getStackIfCustomGlint(stack));
    }

    @Nullable
    public static ItemStack getTargetStack() {
        return currentItem.get();
    }

    @Nullable
    public static ItemStack getStackIfCustomGlint(ItemStack target) {
        if(target == null || !(target.getItem() instanceof ICustomGlintItem) )
            return null;

        return target;

    }


    @Nullable
    public static RenderType getGlint() {
        ItemStack stack = getTargetStack();

        if (stack != null && stack.getItem() instanceof ICustomGlintItem glintItem) {
            return glintItem.getGlint();
        }
        return null;

    }

    @Nullable
    public static RenderType getGlintTranslucent() {
        ItemStack stack = getTargetStack();

        if (stack != null && stack.getItem() instanceof ICustomGlintItem glintItem) {
            return glintItem.getGlintTranslucent();
        }
        return null;

    }

    @Nullable
    public static RenderType getEntityGlint() {
        ItemStack stack = getTargetStack();

        if (stack != null && stack.getItem() instanceof ICustomGlintItem glintItem) {
            return glintItem.getEntityGlint();
        }
        return null;

    }


    @Nullable
    public static RenderType getGlintDirect() {
        ItemStack stack = getTargetStack();

        if (stack != null && stack.getItem() instanceof ICustomGlintItem glintItem) {
            return glintItem.getDirectGlint();
        }
        return null;

    }

    @Nullable
    public static RenderType getEntityGlintDirect() {
        ItemStack stack = getTargetStack();

        if (stack != null && stack.getItem() instanceof ICustomGlintItem glintItem) {
            return glintItem.getEntityGlintDirect();
        }
        return null;

    }

    @Nullable
    public static RenderType getArmorGlint() {
        ItemStack stack = getTargetStack();

        if (stack != null && stack.getItem() instanceof ICustomGlintItem glintItem) {
            return glintItem.getArmorGlint();
        }
        return null;

    }

    @Nullable
    public static RenderType getArmorEntityGlint() {
        ItemStack stack = getTargetStack();

        if (stack != null && stack.getItem() instanceof ICustomGlintItem glintItem) {
            return glintItem.getArmorEntityGlint();
        }
        return null;

    }
}

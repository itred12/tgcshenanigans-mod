package com.itred.tgcshenanigans.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpottedTieItem extends Item {


    public SpottedTieItem(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable EquipmentSlot getEquipmentSlot(@NotNull ItemStack stack) {
        return EquipmentSlot.CHEST;
    }
}

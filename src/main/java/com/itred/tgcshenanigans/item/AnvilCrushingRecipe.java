package com.itred.tgcshenanigans.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import oshi.util.tuples.Pair;

import java.util.Map;

public class AnvilCrushingRecipe {

    // Key-value pairs, where the key is an item that, when destroyed by the "falling anvil" damage type,
    // will spawn the right of the value, with the cost being the left of the value
    private static final Map<Item, Pair<Integer, Item>> ANVIL_CRUSHING_RECIPES = Map.of(
            Items.AMETHYST_SHARD, new Pair<>(4, TGCSItems.AMETHYST_PLATE.get())
    );

    // Fired via mixin when an item is squished by a falling anvil
    public static void processAndDestroy(ItemEntity itemEntity) {

        Level level = itemEntity.level();
        ItemStack stack = itemEntity.getItem();
        Item item = stack.getItem();

        if (ANVIL_CRUSHING_RECIPES.containsKey(item)) {

            Pair<Integer, Item> recipe = ANVIL_CRUSHING_RECIPES.get(item);

            int operations = stack.getCount() / recipe.getA();
            if (operations > 0) {
                itemEntity.spawnAtLocation(new ItemStack(recipe.getB(), operations), 1);
                level.playSound(itemEntity,
                        itemEntity.blockPosition(),
                        SoundEvents.ANVIL_PLACE,
                        SoundSource.BLOCKS,
                        0.5f,
                        2f
                );
                stack.shrink(operations * recipe.getA());
            }



        }


    }

}

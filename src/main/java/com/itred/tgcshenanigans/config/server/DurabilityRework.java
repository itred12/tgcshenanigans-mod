package com.itred.tgcshenanigans.config.server;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.config.IConfiguredEventHandler;
import com.itred.tgcshenanigans.tag.TGCSItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.ArrayList;
import java.util.List;

public class DurabilityRework implements IConfiguredEventHandler {


    private static final List<ResourceKey<Enchantment>> DURABILITY_ENCHANTMENTS = List.of(
            Enchantments.UNBREAKING,
            Enchantments.MENDING
    );

    private static final List<Item> UNBREAKABLE_ITEMS = new ArrayList<>();
    private static final List<Item> UNBREAKABLE_MATERIALS = new ArrayList<>();

    // Check this shit
    private static final List<Item> CACHED_UNBREAKABLE = new ArrayList<>();


    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        refresh(event.getServer().registryAccess());
    }

    // Repopulate these lists from datapack tags, so adding and removing from them is super easy
    @SubscribeEvent
    public void onTagReload(TagsUpdatedEvent event) {
        refresh(event.getRegistryAccess());
    }

    private static void refresh(RegistryAccess registryAccess) {

        CACHED_UNBREAKABLE.clear();
        UNBREAKABLE_ITEMS.clear();
        UNBREAKABLE_MATERIALS.clear();

        HolderLookup.RegistryLookup<Item> itemRegistryLookup = registryAccess.lookupOrThrow(Registries.ITEM);
        HolderSet.Named<Item> unbreakableItems = itemRegistryLookup.getOrThrow(TGCSItemTags.UNBREAKABLE_ITEMS);



        unbreakableItems.forEach(itemHolder -> {

            if (!itemHolder.value().isDamageable(itemHolder.value().getDefaultInstance())) {
                ThisGCsShenanigans.LOGGER.warn(
                        "[Durability Rework]: Item \"{}\" found in unbreakable_items tag is possibly not affected by durability in the first place?? Adding to list anyways and ignoring...",
                        itemHolder.getRegisteredName()
                );

            }
            UNBREAKABLE_ITEMS.add(itemHolder.value());

        });

        HolderSet.Named<Item> unbreakableMaterials = itemRegistryLookup.getOrThrow(TGCSItemTags.UNBREAKABLE_MATERIALS);
        unbreakableMaterials.forEach(itemHolder -> {
            UNBREAKABLE_MATERIALS.add(itemHolder.value());
        });

        ThisGCsShenanigans.LOGGER.info(UNBREAKABLE_ITEMS.toString());
        ThisGCsShenanigans.LOGGER.info(UNBREAKABLE_MATERIALS.toString());

    }

    public static boolean shouldBeUnbreakable(ItemStack stack) {

        Item item = stack.getItem();

        // Search by listed item
        if (UNBREAKABLE_ITEMS.contains(item)) {
            return true;
        }

        // If that fails, search the cached unbreakable items
        if (CACHED_UNBREAKABLE.contains(item)) {
            return true;
        }

        // If that fails, search by material. Marginally more expensive, so we cache any unbreakable items we find to directly look up later.

        if (item instanceof ArmorItem armorItem) {

            Ingredient repairIngredients = armorItem.getMaterial().value().repairIngredient().get();
            for (ItemStack repairItem : repairIngredients.getItems()) {
                if (UNBREAKABLE_MATERIALS.contains(repairItem.getItem())) {
                    CACHED_UNBREAKABLE.add(item);
                    return true;
                }
            }

        }

        if (item instanceof TieredItem tool) {

            Ingredient repairIngredients = tool.getTier().getRepairIngredient();
            for (ItemStack repairItem : repairIngredients.getItems()) {
                if (UNBREAKABLE_MATERIALS.contains(repairItem.getItem())) {
                    CACHED_UNBREAKABLE.add(item);
                    return true;
                }
            }

        }

        return false;

    }

    public static boolean preventEnchantment(Holder<Enchantment> enchantment, ItemStack stack) {
        return DURABILITY_ENCHANTMENTS.contains(enchantment.getKey()) && shouldBeUnbreakable(stack);
    }


    @Override
    public boolean shouldEnable() {
        return Config.DURABILITY_REWORK.get();
    }

    @Override
    public void enable(IEventBus bus, IConfiguredEventHandler instance) {
        NeoForge.EVENT_BUS.register(instance);
    }

    @Override
    public void disable(IEventBus bus, IConfiguredEventHandler instance) {
        NeoForge.EVENT_BUS.unregister(instance);
    }


}

package com.itred.tgcshenanigans.tag;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TGCSItemTags {

    public static final TagKey<Item> PHANTASMAL_ITEMS = createItemTag("phantasmal_items");
    public static final TagKey<Item> CRYSTALLINE_DISC_ITEMS = createItemTag("crystalline_disc_items");

    private static TagKey<Item> createItemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name));
    }
}

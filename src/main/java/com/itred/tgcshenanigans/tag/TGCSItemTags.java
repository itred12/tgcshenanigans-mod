package com.itred.tgcshenanigans.tag;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TGCSItemTags {

    public static final TagKey<Item> PHANTASMAL_ITEMS = createItemTag("phantasmal_items");
    public static final TagKey<Item> CRYSTALLINE_DISC_ITEMS = createItemTag("crystalline_disc_items");
    public static final TagKey<Item> UNBREAKABLE_ITEMS = createItemTag("unbreakable_items");
    public static final TagKey<Item> UNBREAKABLE_MATERIALS = createItemTag("unbreakable_materials");




    private static TagKey<Item> createItemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name));
    }
}

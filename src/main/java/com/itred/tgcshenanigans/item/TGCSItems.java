package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.sound.TGCSSounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TGCSItems {

    public static final DeferredRegister.Items ITEMS_REGISTRY = DeferredRegister.createItems(ThisGCsShenanigans.MODID);

    public static final DeferredItem<Item> DISC_FIREPLACE = ITEMS_REGISTRY.register("music_disc_fireplace",
            () -> new Item(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.RARE)
                            .fireResistant()
                            // .jukeboxPlayable()
            ));

    public static final DeferredItem<Item> DISC_AIZO = ITEMS_REGISTRY.register("music_disc_aizo",
            () -> new Item(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.RARE)
                            .fireResistant()
                            .jukeboxPlayable(TGCSSounds.MUSIC_DISC_AIZO_KEY)
            ));

    public static final DeferredItem<Item> AMETHYST_PLATE = ITEMS_REGISTRY.register("amethyst_plate",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CRYSTALLINE_DISC = ITEMS_REGISTRY.register("crystalline_disc",
            () -> new CrystallineDiscItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
            ));

    public static void registerAll(IEventBus bus) {
        ITEMS_REGISTRY.register(bus);
    }
}

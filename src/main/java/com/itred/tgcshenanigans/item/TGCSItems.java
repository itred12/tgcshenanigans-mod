package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TGCSItems {

    public static final DeferredRegister.Items MODITEMS = DeferredRegister.createItems(ThisGCsShenanigans.MODID);

    public static final DeferredItem<Item> DISC_FIREPLACE = MODITEMS.register("music_disc_fireplace",
            () -> new Item(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
                            .fireResistant()
                            // .jukeboxPlayable()
            ));

    public static void registerAll(IEventBus bus) {
        MODITEMS.register(bus);
    }
}

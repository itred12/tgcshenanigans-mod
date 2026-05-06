package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.sound.TGCSSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TGCSItems {

    public static final DeferredRegister.Items ITEMS_REGISTRY = DeferredRegister.createItems(ThisGCsShenanigans.MODID);

    // Materials
    public static final DeferredItem<Item> AMETHYST_PLATE = ITEMS_REGISTRY.register("amethyst_plate", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DISC_LABEL = ITEMS_REGISTRY.register("disc_label", () -> new Item(new Item.Properties()));

    // Music discs
    public static final DeferredItem<Item> DISC_FIREPLACE = newMusicDisc("music_disc_fireplace", TGCSSounds.MUSIC_DISC_FIREPLACE_KEY);
    public static final DeferredItem<Item> DISC_AIZO = newMusicDisc("music_disc_aizo", TGCSSounds.MUSIC_DISC_AIZO_KEY);
    public static final DeferredItem<Item> DISC_CATSWING = newMusicDisc("music_disc_catswing", TGCSSounds.MUSIC_DISC_CATSWING_KEY);
    public static final DeferredItem<Item> DISC_FROMNOWON = newMusicDisc("music_disc_fromnowon", TGCSSounds.MUSIC_DISC_FROMNOWON_KEY);


    public static final DeferredItem<Item> CRYSTALLINE_DISC_VOICELESS = ITEMS_REGISTRY.register("crystalline_disc_voiceless",
            () -> new CrystallineDiscItem(
                    new Item.Properties()
                            .stacksTo(1)
            ));

    public static final DeferredItem<Item> CRYSTALLINE_DISC_AIZO = newCrystallineDiscOutput("crystalline_disc_aizo");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_FIREPLACE = newCrystallineDiscOutput("crystalline_disc_fireplace");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_CATSWING = newCrystallineDiscOutput("crystalline_disc_catswing");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_FROMNOWON = newCrystallineDiscOutput("crystalline_disc_fromnowon");


    public static DeferredItem<Item> newMusicDisc(String name, ResourceKey<JukeboxSong> key) {
        return ITEMS_REGISTRY.register(name,
                () -> new Item(
                        new Item.Properties()
                                .stacksTo(1)
                                .rarity(Rarity.RARE)
                                .fireResistant()
                                .jukeboxPlayable(key)
                ));
    }

    public static DeferredItem<Item> newCrystallineDiscOutput(String name) {
        return ITEMS_REGISTRY.register(
                name,
                () -> new Item(
                        new Item.Properties()
                                .stacksTo(1)
                                .rarity(Rarity.UNCOMMON)
                                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                ) {
                    @Override
                    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
                        tooltipComponents.add(Component.translatable("item.tgcshenanigans." + name + ".description").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
                        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                    }
                }
        );
    }


    public static void registerAll(IEventBus bus) {
        ITEMS_REGISTRY.register(bus);
    }
}

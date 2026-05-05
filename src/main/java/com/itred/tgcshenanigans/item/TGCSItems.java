package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.sound.TGCSSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
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
    public static final DeferredItem<Item> DISC_FIREPLACE = ITEMS_REGISTRY.register("music_disc_fireplace",
            () -> new Item(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.RARE)
                            .fireResistant()
                            .jukeboxPlayable(TGCSSounds.MUSIC_DISC_FIREPLACE_KEY)
            ));

    public static final DeferredItem<Item> DISC_AIZO = ITEMS_REGISTRY.register("music_disc_aizo",
            () -> new Item(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.RARE)
                            .fireResistant()
                            .jukeboxPlayable(TGCSSounds.MUSIC_DISC_AIZO_KEY)
            ));

    public static final DeferredItem<Item> CRYSTALLINE_DISC_VOICELESS = ITEMS_REGISTRY.register("crystalline_disc_voiceless",
            () -> new CrystallineDiscItem(
                    new Item.Properties()
                            .stacksTo(1)
            ));

    public static final DeferredItem<Item> CRYSTALLINE_DISC_AIZO = newCrystallineDiscOutput("crystalline_disc_aizo");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_FIREPLACE = newCrystallineDiscOutput("crystalline_disc_fireplace");



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

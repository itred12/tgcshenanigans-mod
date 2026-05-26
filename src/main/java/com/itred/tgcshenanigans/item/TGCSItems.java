package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.item.custom.BluntCleaverItem;
import com.itred.tgcshenanigans.item.custom.CrystallineDiscItem;
import com.itred.tgcshenanigans.item.custom.ShadowArmorItem;
import com.itred.tgcshenanigans.item.custom.SpottedTieItem;
import com.itred.tgcshenanigans.sound.TGCSSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TGCSItems {

    public static final DeferredRegister.Items ITEMS_REGISTRY = DeferredRegister.createItems(ThisGCsShenanigans.MODID);

    // Weapons
    public static final DeferredItem<Item> BLUNT_CLEAVER = ITEMS_REGISTRY.register("blunt_cleaver", () -> new BluntCleaverItem(Tiers.IRON ,
            new Item.Properties().attributes(ItemAttributeModifiers.builder()
                    .add(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(ResourceLocation.withDefaultNamespace("base_attack_damage"), 7, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            Attributes.ATTACK_SPEED,
                            new AttributeModifier(ResourceLocation.withDefaultNamespace("base_attack_speed"), -3.0D, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                        )
                    .build())
    ));

    // ""Armor""
    public static final DeferredItem<Item> SPOTTED_TIE = ITEMS_REGISTRY.register("spotted_tie", () -> new SpottedTieItem(new Item.Properties()));
    public static final DeferredItem<ArmorItem> SHADOW_HELM = ITEMS_REGISTRY.register("shadow_helmet",
            () -> new ShadowArmorItem(TGCSArmorMaterials.SHADOW_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(
                            ArmorItem.Type.HELMET.getDurability(19)
                    )
            )
    );
    public static final DeferredItem<ArmorItem> SHADOW_CHESTPLATE = ITEMS_REGISTRY.register("shadow_chestplate",
            () -> new ShadowArmorItem(TGCSArmorMaterials.SHADOW_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(
                            ArmorItem.Type.CHESTPLATE.getDurability(19)
                    )
            )
    );
    public static final DeferredItem<ArmorItem> SHADOW_LEGGINGS = ITEMS_REGISTRY.register("shadow_leggings",
            () -> new ShadowArmorItem(TGCSArmorMaterials.SHADOW_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(
                            ArmorItem.Type.LEGGINGS.getDurability(19)
                    )
            )
    );
    public static final DeferredItem<ArmorItem> SHADOW_BOOTS = ITEMS_REGISTRY.register("shadow_boots",
            () -> new ShadowArmorItem(TGCSArmorMaterials.SHADOW_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(
                            ArmorItem.Type.BOOTS.getDurability(19)
                    )
            )
    );



    // Materials
    public static final DeferredItem<Item> AMETHYST_PLATE = ITEMS_REGISTRY.register("amethyst_plate", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DISC_LABEL = ITEMS_REGISTRY.register("disc_label", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHADOW_CRYSTAL = ITEMS_REGISTRY.register("shadow_crystal", () -> new Item(new Item.Properties()));

    // Music discs
    public static final DeferredItem<Item> DISC_FIREPLACE = newMusicDisc("music_disc_fireplace", TGCSSounds.MUSIC_DISC_FIREPLACE_KEY);
    public static final DeferredItem<Item> DISC_AIZO = newMusicDisc("music_disc_aizo", TGCSSounds.MUSIC_DISC_AIZO_KEY);
    public static final DeferredItem<Item> DISC_CATSWING = newMusicDisc("music_disc_catswing", TGCSSounds.MUSIC_DISC_CATSWING_KEY);
    public static final DeferredItem<Item> DISC_FROMNOWON = newMusicDisc("music_disc_fromnowon", TGCSSounds.MUSIC_DISC_FROMNOWON_KEY);
    public static final DeferredItem<Item> DISC_DEATHODYSSEY = newMusicDisc("music_disc_deathodyssey", TGCSSounds.MUSIC_DISC_DEATHODYSSEY_KEY);
    public static final DeferredItem<Item> DISC_DAUGHTEROFHALLOWNEST = newMusicDisc("music_disc_daughterofhallownest", TGCSSounds.MUSIC_DISC_DAUGHTEROFHALLOWNEST_KEY);
    public static final DeferredItem<Item> DISC_REMEMBER = newMusicDisc("music_disc_remember", TGCSSounds.MUSIC_DISC_REMEMBER_KEY);


    public static final DeferredItem<Item> CRYSTALLINE_DISC_VOICELESS = ITEMS_REGISTRY.register("crystalline_disc_voiceless",
            () -> new CrystallineDiscItem(
                    new Item.Properties()
                            .stacksTo(1)
            ));

    public static final DeferredItem<Item> CRYSTALLINE_DISC_AIZO = newCrystallineDiscOutput("crystalline_disc_aizo");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_FIREPLACE = newCrystallineDiscOutput("crystalline_disc_fireplace");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_CATSWING = newCrystallineDiscOutput("crystalline_disc_catswing");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_FROMNOWON = newCrystallineDiscOutput("crystalline_disc_fromnowon");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_DEATHODYSSEY = newCrystallineDiscOutput("crystalline_disc_deathodyssey");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_DAUGHTEROFHALLOWNEST = newCrystallineDiscOutput("crystalline_disc_daughterofhallownest");
    public static final DeferredItem<Item> CRYSTALLINE_DISC_REMEMBER = newCrystallineDiscOutput("crystalline_disc_remember");


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

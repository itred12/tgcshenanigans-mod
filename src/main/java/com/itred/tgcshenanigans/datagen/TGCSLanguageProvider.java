package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.enchantment.TGCSEnchantments;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TGCSLanguageProvider extends LanguageProvider {

    private static final String ENCHANTMENT_PREFIX = "enchantment";
    private static final String ITEM_PREFIX = "item";
    private static final String ADVANCEMENT_PREVIX = "advancement." + ThisGCsShenanigans.MODID;

    public TGCSLanguageProvider(PackOutput output) {
        super(output, ThisGCsShenanigans.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        addEnchantment(TGCSEnchantments.DEEP_BREATH, "Deep Breath");

        add(TGCSItems.AMETHYST_PLATE.get(), "Amethyst Plate");
        addWithTooltip(TGCSItems.BLUNT_CLEAVER,
                "Blunt Cleaver", "Cannot critical hit"
        );
        add(TGCSItems.SPOTTED_TIE.get(), "Spotted Tie");

        addWithComplexTooltips(
                TGCSItems.CRYSTALLINE_DISC_VOICELESS, "Amethyst Disc",
                Map.of(
                        "", "It seeks inspiration...",
                        "aizo", "It's fixated on a powerful song.",
                        "fireplace", "It's fixated on a thoughtful song.",
                        "catswing", "It's fixated on a friendly song.",
                        "fromnowon", "It's fixated on a resolute song.",
                        "deathodyssey", "It's fixated on a swashbuckling song.",
                        "daughterofhallownest", "It's fixated on a meticulous song.",
                        "remember", "It's fixated on a determined song."
                )
        );

        addMusicDisc(TGCSItems.DISC_AIZO, "§4Music Disc§r", "King Gnu – AIZO");
        addMusicDisc(TGCSItems.DISC_FIREPLACE, "Toby Fox – Fireplace");
        /*
        addMusicDisc(TGCSItems.DISC_ICYSANCTUM, "Drazorleaf – Icy Sanctum: Rearrangement",
        List.of("From Pokemon Mystery Dungeon: Gates to Infinity", "Originally composed by Keisuke Ito & Yasuhiro Kawagoe")
        );
         */
        addMusicDisc(TGCSItems.DISC_CATSWING, "Toby Fox – Catswing");
        addMusicDisc(TGCSItems.DISC_FROMNOWON, "Toby Fox – From Now On");
        addMusicDisc(TGCSItems.DISC_DEATHODYSSEY, "Heaven Pierce Her – Death Odyssey / Aftermath");
        addMusicDisc(TGCSItems.DISC_DAUGHTEROFHALLOWNEST, "Christopher Larkin – Daughter of Hallownest");
        addMusicDisc(TGCSItems.DISC_REMEMBER, "Masato (coldrain) & Hiroaki Tsutsumi – REMEMBER");





    }

    private void addEnchantment(ResourceKey<Enchantment> enchantment, String translation) {
        this.add(ENCHANTMENT_PREFIX + "." + enchantment.location().toLanguageKey(), translation);
    }

    /// Adds an item with several translation keys. Every key after the first will be \<item_name\>.tooltip_\<N\>,
    ///  unless if there's only one tooltip, in which case it will just be \<item_name\>.tooltip
    private void addWithTooltip(DeferredItem<?> item, String... translations) {

        // LET IT BE MUTABLE
        List<String> translationList = new ArrayList<>(List.of(translations));
        this.add(item.get(), translationList.removeFirst());

        int tooltipCount = 1; // Silly Java and their index-0 lists (im joking please dont ki-)
        for (String tooltip : translationList) {
            this.add(ITEM_PREFIX + "." + item.getId().toLanguageKey() + (translationList.size() == 1 ? ".tooltip" : ".tooltip_" + tooltipCount), tooltip);
            tooltipCount++;
        }

    }

    /// Adds an item with many tooltips. Every key in the map will become \<item_name\>.tooltip_\<key\>.
    /// A single blank string key in the map is permitted, which will just become \<item_name\>.tooltip.
    private void addWithComplexTooltips(DeferredItem<?> item, String translatedName, Map<String, String> tooltipTranslations) {

        this.add(item.get(), translatedName);
        String tooltip_key_prefix = ITEM_PREFIX + "." + item.getId().toLanguageKey() + ".tooltip";

        for (Map.Entry<String, String> translation : tooltipTranslations.entrySet()) {
            if (translation.getKey().isBlank()) {
                this.add(tooltip_key_prefix, translation.getValue());
            } else {
                this.add(tooltip_key_prefix + "_" + translation.getKey(), translation.getValue());
            }
        }

    }

    private void addMusicDisc(DeferredItem<?> item, String translatedName, String credit, List<String> otherTooltips) {

        this.add(item.get(), translatedName);
        String key_prefix = ITEM_PREFIX + "." + item.getId().toLanguageKey();
        this.add(key_prefix + ".credit_splash", credit);

        if (!otherTooltips.isEmpty()) {

            int tooltipCount = 1;
            for (String tooltip : otherTooltips) {
                this.add(ITEM_PREFIX + "." + item.getId().toLanguageKey() + (otherTooltips.size() == 1 ? ".tooltip" : ".tooltip_" + tooltipCount), tooltip);
                tooltipCount++;
            }

        }

    }

    private void addMusicDisc(DeferredItem<?> item, String translatedName, String credit) {
        addMusicDisc(item, "Music Disc", credit, List.of());
    }

    private void addMusicDisc(DeferredItem<?> item, String credit) {
        addMusicDisc(item, "Music Disc", credit, List.of());
    }

    private void addAdvancement(String name, String title, String description) {
        String key = ADVANCEMENT_PREVIX;
    }


}

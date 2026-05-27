package com.itred.tgcshenanigans.datagen;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.datagen.registry.TGCSEnchantmentRegistryProvider;
import com.itred.tgcshenanigans.datagen.registry.TGCSJukeboxSongRegistryProvider;
import com.itred.tgcshenanigans.item.TGCSCreativeModeTabs;
import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TGCSLanguageProvider extends LanguageProvider {

    private static final String ENCHANTMENT_PREFIX = "enchantment";
    private static final String ITEM_PREFIX = "item";
    private static final String ADVANCEMENT_PREFIX = "advancement." + ThisGCsShenanigans.MODID + ".";
    private static final String CONFIG_PREFIX = ThisGCsShenanigans.MODID + ".configuration.";
    private static final String SOUND_PREFIX = "sounds." + ThisGCsShenanigans.MODID + ".";
    private static final String MESSAGE_PREFIX = "message." + ThisGCsShenanigans.MODID + ".";


    private static final String JEI_INFO_PREFIX = "gui." + ThisGCsShenanigans.MODID + ".";
    private static final String ORIGINS_ORIGIN_PREFIX = "origin." + ThisGCsShenanigans.MODID + ".";
    private static final String ORIGINS_POWER_PREFIX = "power." + ThisGCsShenanigans.MODID + ".";

    public TGCSLanguageProvider(PackOutput output) {
        super(output, ThisGCsShenanigans.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        addCreativeTab(TGCSCreativeModeTabs.GENERAL_ITEMS_TAB, "This GC's Shenanigans");

        addEnchantment(TGCSEnchantmentRegistryProvider.DEEP_BREATH, "Deep Breath");

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

        addWithTooltip(TGCSItems.CRYSTALLINE_DISC_AIZO, "Amethyst Disc", "It hums with a powerful song.");
        addWithTooltip(TGCSItems.CRYSTALLINE_DISC_FIREPLACE, "Amethyst Disc", "It hums with a thoughtful song.");
        addWithTooltip(TGCSItems.CRYSTALLINE_DISC_CATSWING, "Amethyst Disc", "It hums with a friendly song.");
        addWithTooltip(TGCSItems.CRYSTALLINE_DISC_FROMNOWON, "Amethyst Disc", "It hums with a resolute song.");
        addWithTooltip(TGCSItems.CRYSTALLINE_DISC_DEATHODYSSEY, "Amethyst Disc", "It hums with a swashbuckling song.");
        addWithTooltip(TGCSItems.CRYSTALLINE_DISC_DAUGHTEROFHALLOWNEST, "Amethyst Disc", "It hums with a meticulous song.");
        addWithTooltip(TGCSItems.CRYSTALLINE_DISC_REMEMBER, "Amethyst Disc", "It hums with a determined song.");

        addMusicDisc(TGCSItems.DISC_AIZO, TGCSJukeboxSongRegistryProvider.JUKEBOX_SONG_AIZO,
                "§4Music Disc§r", "King Gnu – AIZO");
        addMusicDisc(TGCSItems.DISC_FIREPLACE, TGCSJukeboxSongRegistryProvider.JUKEBOX_SONG_FIREPLACE,
                "Toby Fox – Fireplace");
        /*
        addMusicDisc(TGCSItems.DISC_ICYSANCTUM, "Drazorleaf – Icy Sanctum: Rearrangement",
        List.of("From Pokemon Mystery Dungeon: Gates to Infinity", "Originally composed by Keisuke Ito & Yasuhiro Kawagoe")
        );
         */
        addMusicDisc(TGCSItems.DISC_CATSWING, TGCSJukeboxSongRegistryProvider.JUKEBOX_SONG_CATSWING,
                "Toby Fox – Catswing");
        addMusicDisc(TGCSItems.DISC_FROMNOWON, TGCSJukeboxSongRegistryProvider.JUKEBOX_SONG_FROMNOWON,
                "Toby Fox – From Now On");
        addMusicDisc(TGCSItems.DISC_DEATHODYSSEY, TGCSJukeboxSongRegistryProvider.JUKEBOX_SONG_DEATHODYSSEY,
                "Heaven Pierce Her – Death Odyssey / Aftermath");
        addMusicDisc(TGCSItems.DISC_DAUGHTEROFHALLOWNEST, TGCSJukeboxSongRegistryProvider.JUKEBOX_SONG_DAUGHTEROFHALLOWNEST,
                "Christopher Larkin – Daughter of Hallownest");
        addMusicDisc(TGCSItems.DISC_REMEMBER, TGCSJukeboxSongRegistryProvider.JUKEBOX_SONG_REMEMBER,
                "Masato (coldrain) & Hiroaki Tsutsumi – REMEMBER");

        addAdvancement("craft_custom_disc",
                "Soundsmith",
                "Use an amethyst disc to pluck a song out of the air and print it to a record."
        );

        // Server config
        addConfigSection("blueaxolottweaks", "Blue Axolotl Tweaks");
            addConfig(Config.ENABLE_BLUE_AXOLOTL_PING, "Blue Axolotl Ping");
            addConfig(Config.ALLOW_BLUE_AXOLOTLS_SPAWN_NATURALLY, "Naturally Spawning Blue Axolotls");
            addConfig(Config.BLUE_AXOLOTL_SPAWNCHANCE, "Natural Spawn Chance");
            addConfig(Config.BLUE_AXOLOTL_SPAWNCHANCE_OFFSPRING, "Offspring Chance");

        addConfig(Config.DISABLED_ENTITIES_LIST, "Disabled Entities");

        addConfigSection("itembalancing", "Item Balance Tweaks");
            addConfig(Config.DURABILITY_REWORK, "Durability Rework");
            addConfig(Config.BOW_DAMAGE_MULTIPLIER, "Bow Damage Multiplier");
            addConfig(Config.MULTISHOT_NO_IFRAMES, "Multishot Semi-Rework");
            addConfig(Config.MULTISHOT_EXTRA_ARROW_DAMAGE_MULTIPLIER, "Multishot Multihit Damage Multiplier");

        // Client config
        addConfig(Config.BLUE_AXOLOTL_PING_SOUND, "Blue Axolotl Sound Effect");
        addConfig(Config.BLOCK_ANIMATION_SPEED, "Block Animation Speed");

        // Startup config
        addConfigSection("mobstattweaks", "Mob Stat Tweaks");
            addConfig(Config.ENDER_DRAGON_HEALTH, "Ender Dragon Max Health");
            addConfig(Config.WITHER_HEALTH, "Wither Max Health");
            addConfig(Config.WARDEN_HEALTH, "Warden Max Health");

        // Music disc subtitles
        addSubtitle("disc", "kinggnu_aizo", "AIZO by King Gnu plays");
        addSubtitle("disc", "tobyfox_fireplace", "Fireplace by Toby Fox plays");
        addSubtitle("disc", "drazorleaf_icysanctum", "Icy Sanctum rearranged by Drazorleaf plays");
        addSubtitle("disc", "tobyfox_fromnowon", "From Now On by Toby Fox plays");
        addSubtitle("disc", "tobyfox_catswing", "Catswing by Toby Fox plays");
        addSubtitle("disc", "heavenpierceher_deathodyssey", "Death Odyssey / Aftermath by Heaven Pierce Her plays");
        addSubtitle("disc", "christopherlarkin_daughterofhallownest", "Daughter of Hallownest by Christopher Larkin plays");
        addSubtitle("disc", "coldrain_remember", "REMEMBER by Masato (coldrain) & Hiroaki Tsutsumi plays");

        // Ambient music subtitles
        addSubtitle("ambient", "tobyfox_12am", "12am by Toby Fox plays");
        addSubtitle("ambient", "tobyfox_alt_church_lobby", "alt_church_lobby by Toby Fox plays");
        // Technically menu-only, but it's good to be thorough
        addSubtitle("ambient", "tobyfox_with_hope_crossed_on_our_hearts", "With Hope Crossed On Our Hearts by Toby Fox plays");

        // Misc subtitles
        addSubtitle("effect", "blueaxolotlspawn", "Blue axolotl twinkles");

        // System messages (i.e., Crystalline Disc hints)
        addSystemMessage("crystalline_disc", "hint", "The disc picks up on a song...");
        addSystemMessage("crystalline_disc", "start", "The disc is listening...");
        addSystemMessage("crystalline_disc", "listen_1", "The disc takes in its surroundings...");
        addSystemMessage("crystalline_disc", "listen_2", "The disc hums with the terrain...");
        addSystemMessage("crystalline_disc", "listen_3", "The disc is ready.");


        // JEI details
        addJEIDescription("amethyst_plate_anvilcrafting_details", "Can also be crafted by crushing four amethyst shards with a falling anvil.");

        // Origins translations
        addOriginsOrigin("dove",
                "Colombe d'Marie",
                "A holy, rarely-seen distant cousin of the Avian. Attunement to that above has allowed them to keep their wings, but has consequently made them a target of the eldritch and decaying."
                );

        addOriginsPower("dove", "dove_lightbody",
                "Lightweight",
                "Your body is much lighter, designed for flight. You don't take any fall damage, and exhaust slower from moving around."
        );

        addOriginsPower("dove", "dove_airefficiency",
                "Catch the Winds",
                "You can move just as fast in the air as you can on the ground– any movement speed bonuses will also apply to air speed."
        );
    }

    private void addCreativeTab(Supplier<CreativeModeTab> tab, String tabTranslatedName) {
        add(tab.get().getDisplayName().getString(), tabTranslatedName);
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

    private void addMusicDisc(DeferredItem<?> item, JukeboxSong song, String translatedName, String credit, List<String> otherTooltips) {

        this.add(item.get(), translatedName);
        String key_prefix = ITEM_PREFIX + "." + item.getId().toLanguageKey();
        this.add(song.description().getString(), credit);

        if (!otherTooltips.isEmpty()) {

            int tooltipCount = 1;
            for (String tooltip : otherTooltips) {
                this.add(ITEM_PREFIX + "." + item.getId().toLanguageKey() + (otherTooltips.size() == 1 ? ".tooltip" : ".tooltip_" + tooltipCount), tooltip);
                tooltipCount++;
            }

        }

    }

    private void addMusicDisc(DeferredItem<?> item, JukeboxSong song, String translatedName, String credit) {
        addMusicDisc(item, song, translatedName, credit, List.of());
    }

    private void addMusicDisc(DeferredItem<?> item, JukeboxSong song, String credit) {
        addMusicDisc(item, song, "Music Disc", credit, List.of());
    }

    private void addAdvancement(String translationkey, String titleTranslation, String descriptionTranslation) {
        String key = ADVANCEMENT_PREFIX;
        add(ADVANCEMENT_PREFIX + translationkey + ".title", titleTranslation);
        add(ADVANCEMENT_PREFIX + translationkey + ".description", descriptionTranslation);
    }

    private void addConfig(ModConfigSpec.ConfigValue<?> configValue, String nameTranslation) {
        // Hasnt ever given me issues?? Technically this wont ever *be* null I dont think??
        // May as well check I suppose
        if (configValue.getSpec().getTranslationKey() != null) {
            add(configValue.getSpec().getTranslationKey(), nameTranslation);
        }
    }

    private void addConfigSection(String sectionPath, String sectionTranslation) {
        add(CONFIG_PREFIX + sectionPath, sectionTranslation);
    }

    private void addSubtitle(String section, String subtitleKey, String subtitleTranslation) {
        add(SOUND_PREFIX + section + "." + subtitleKey, subtitleTranslation);
    }

    private void addSystemMessage(String messageKeyName, String translation) {
        add(MESSAGE_PREFIX + messageKeyName, translation);
    }

    private void addSystemMessage(String sectionName, String messageKeyName, String translation) {
        add(MESSAGE_PREFIX + sectionName + "." + messageKeyName, translation);
    }

    // JEI compat
    private void addJEIDescription(String keyName, String translation) {
        add(JEI_INFO_PREFIX + keyName, translation);
    }

    // Origins compat

    private void addOriginsOrigin(String internalName, String translatedName, String translatedDescription) {
        add(ORIGINS_ORIGIN_PREFIX + internalName + ".name", translatedName);
        add(ORIGINS_ORIGIN_PREFIX + internalName + ".description", translatedDescription);
    }

    private void addOriginsPower(String internalName, String translatedName, String translatedDescription) {
        add(ORIGINS_POWER_PREFIX + internalName + ".name", translatedName);
        add(ORIGINS_POWER_PREFIX + internalName + ".description", translatedDescription);
    }

    /// Used when an origins power is in a sub-folder. The path is the /-separated file path from the powers folder to the folder containing the power, not including the / between the two at the end.
    private void addOriginsPower(String path, String internalName, String translatedName, String translatedDescription) {
        add(ORIGINS_ORIGIN_PREFIX + path + "/" + internalName + ".name", translatedName);
        add(ORIGINS_ORIGIN_PREFIX + path + "/" + internalName + ".description", translatedDescription);
    }







}

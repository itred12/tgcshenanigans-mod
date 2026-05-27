package com.itred.tgcshenanigans;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.itred.tgcshenanigans.config.server.BlueAxolotlPing;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {


    private static final ModConfigSpec.Builder COMMON_CONFIG_BUILDER;
    private static final ModConfigSpec.Builder SERVER_CONFIG_BUILDER;
    private static final ModConfigSpec.Builder CLIENT_CONFIG_BUILDER;
    private static final ModConfigSpec.Builder STARTUP_CONFIG_BUILDER; // Registered as registries are, may have desync between server and client. Do NOT use to enable/disable features.


    public static final ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec SERVER_CONFIG;
    public static final ModConfigSpec CLIENT_CONFIG;
    public static final ModConfigSpec STARTUP_CONFIG;


    // Template
        public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK;
        public static final ModConfigSpec.IntValue MAGIC_NUMBER;
        public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION;

    // Server
        // Blue Axolotl
        public static final ModConfigSpec.BooleanValue ENABLE_BLUE_AXOLOTL_PING;
        public static final ModConfigSpec.BooleanValue ALLOW_BLUE_AXOLOTLS_SPAWN_NATURALLY;
        public static final ModConfigSpec.IntValue BLUE_AXOLOTL_SPAWNCHANCE;
        public static final ModConfigSpec.IntValue BLUE_AXOLOTL_SPAWNCHANCE_OFFSPRING;

        // Other
        public static final ModConfigSpec.ConfigValue<List<? extends String>> DISABLED_ENTITIES_LIST;

        // Durability Rework
        public static final ModConfigSpec.BooleanValue DURABILITY_REWORK;

        // Balancing
        public static final ModConfigSpec.DoubleValue BOW_DAMAGE_MULTIPLIER;
        public static final ModConfigSpec.BooleanValue MULTISHOT_NO_IFRAMES;
        public static final ModConfigSpec.DoubleValue MULTISHOT_EXTRA_ARROW_DAMAGE_MULTIPLIER;

        // Mod item config
        public static final ModConfigSpec.DoubleValue DEEP_BREATH_DAMAGE_PER_LEVEL;
        public static final ModConfigSpec.DoubleValue DEEP_BREATH_STACK_COUNT_PER_LEVEL;
        public static final ModConfigSpec.DoubleValue DEEP_BREATH_MINIMUM_EXTRA_DAMAGE;
        public static final ModConfigSpec.DoubleValue DEEP_BREATH_COOLDOWN_TIME;
        public static final ModConfigSpec.DoubleValue DEEP_BREATH_COOLDOWN_TIME_DELAY;



    // Client
        // Blue Axolotl
        public static final ModConfigSpec.EnumValue<BlueAxolotlPing.BlueAxolotlSpawnSfx> BLUE_AXOLOTL_PING_SOUND;
        // Animation speed
        public static final ModConfigSpec.DoubleValue BLOCK_ANIMATION_SPEED;

    // Startup
        //Stat changes
        public static final ModConfigSpec.DoubleValue ENDER_DRAGON_HEALTH;
        public static final ModConfigSpec.DoubleValue WITHER_HEALTH;
        public static final ModConfigSpec.DoubleValue WARDEN_HEALTH;


    static {

        // ======= COMMON CONFIG =======
        COMMON_CONFIG_BUILDER = new ModConfigSpec.Builder();

        // TEMPLATE
            LOG_DIRT_BLOCK = COMMON_CONFIG_BUILDER
                    .comment("Whether to log the dirt block on common setup")
                    .define("logDirtBlock", true);

            MAGIC_NUMBER = COMMON_CONFIG_BUILDER
                    .comment("A magic number")
                    .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

            MAGIC_NUMBER_INTRODUCTION = COMMON_CONFIG_BUILDER
                    .comment("What you want the introduction message to be for the magic number")
                    .define("magicNumberIntroduction", "The magic number is... ");


        COMMON_CONFIG = COMMON_CONFIG_BUILDER.build();

        // ======= SERVER CONFIG =======
        SERVER_CONFIG_BUILDER = new ModConfigSpec.Builder();

        // Blue Axolotl

        // Translation key is "modid.configuration.sectionpath"
        SERVER_CONFIG_BUILDER.push("blueaxolottweaks");

            ENABLE_BLUE_AXOLOTL_PING = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.blue_axolotl_ping_masterswitch")
                    .comment("Enable or disable playing a sound effect when a player goes near a blue axolotl.",
                            "",
                            "The sound effect itself can also be enabled or disabled on the client's end, but it will always respect this option.")
                    .worldRestart()
                    .define("blueAxolotlPingMasterswitch", true);

            ALLOW_BLUE_AXOLOTLS_SPAWN_NATURALLY = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.blue_axolotl_spawn_naturally")
                    .comment("Enable or disable blue axolotls spawning naturally.")
                    .comment("")
                    .comment("When disabled, blue axolotls will only spawn as offspring from other Axolotls (Vanilla behavior).")
                    .comment("")
                    .comment("When enabled, blue axolotls can spawn out in the wild.")
                    .define("blueAxolotNaturalSpawn", true);

            BLUE_AXOLOTL_SPAWNCHANCE = SERVER_CONFIG_BUILDER
                .translation("tgcshenanigans.config.server.blue_axolotl_spawn_chance")
                .comment("Modify the chance that a blue axolotl can spawn naturally. Default is 1 in 1200.")
                .comment("")
                .comment("Only takes effect if Naturally Spawning Blue Axolotls is enabled.")
                .defineInRange("blueAxolotlNaturalSpawnChance", 1200, 1, 8192);

            BLUE_AXOLOTL_SPAWNCHANCE_OFFSPRING = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.blue_axolotl_spawn_chance_offspring")
                    .comment("Modify the chance that a blue axolotl can spawn as offspring from two other (non-blue) axolotl.")
                    .comment("")
                    .comment("Default is 1 in 1200 (Vanilla chance)")
                    .defineInRange("blueAxolotlNaturalSpawnChanceOffspring", 1200, 1, 8192);

        SERVER_CONFIG_BUILDER.pop();
        // Other

            DISABLED_ENTITIES_LIST = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.disabled_entities")
                    .comment("Prevents entities in this list from spawning, effectively disabling them.",
                            "",
                            "May have some performance cost if many types of entities are disabled.",
                            "",
                            "Entities must be listed as \"<namespace>:<id>\", i.e., \"minecraft:chicken\".",
                            "",
                            "Commands can be used to find out the names and namespaces of entities to disable them.")
                    .worldRestart()
                    .defineListAllowEmpty("disabledEntitiesList", List.of(), () -> "", Config::validateEntityName);

        // Durability rework

        SERVER_CONFIG_BUILDER.push("itembalancing");

            DURABILITY_REWORK = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.enable_durability_rework")
                    .comment("Enable or disable the durability rework, which makes certain high-tier items unbreakable.",
                            "",
                            "You can add or remove entries from this using a datapack.",
                            "",
                            "unbreakable_item and unbreakable_material tags can be placed in your datapack at data/" + ThisGCsShenanigans.MODID + "/tags/item/.",
                            "",
                            "All items in the unbreakable_item tag, and all armor and equipment repaired by items in the unbreakable_materials tag, will become unbreakable with this option enabled.",
                            "",
                            "By default, this affects Tridents, Elytra, the Mace, and all Netherite gear.")
                    .worldRestart()
                    .define("durabilityRework", false);

            BOW_DAMAGE_MULTIPLIER = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.bow_damage_multiplier")
                    .comment("Multiplies the damage of arrows fired specifically from bows or bow-adjacent items by this amount.",
                            "Can go into decimal values to reduce the damage of these weapons as needed.")
                    .defineInRange("bowDamageMultiplier", 1.0, 0.01, 5);

            MULTISHOT_NO_IFRAMES = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.multishot_no_iframes")
                    .comment("If enabled, arrows fired out of multishot crossbows wont give invulnerability frames on hit, allowing every arrow to connect at once on a single entity.")
                    .define("multishotNoIFrames", false);

            MULTISHOT_EXTRA_ARROW_DAMAGE_MULTIPLIER = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.multishot_extra_arrow_damage_multiplier")
                    .comment("If Multishot Semi-Rework is enabled, the damage of every arrow after the first (from a single shot) that hits an entity is multiplied by this amount.",
                            "Can go into decimal values to reduce the damage of these weapons as needed.",
                            "",
                            "RECOMMENDED: 0.5")
                    .defineInRange("multishotExtraArrowDamageMultiplier", 0.5, 0.01, 5);

        SERVER_CONFIG_BUILDER.pop();

        SERVER_CONFIG_BUILDER.push("moditemconfig");

            SERVER_CONFIG_BUILDER.push("deepbreathconfig");

            DEEP_BREATH_DAMAGE_PER_LEVEL = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.deep_breath_damage_per_level")
                    .comment("The amount of added damage the Deep Breath enchantment gives to weapons per-level.",
                            "",
                            "For reference, the vanilla Smite enchantment gives +2.5 extra damage per-level. The vanilla Sharpness enchantment gives +0.5 extra damage per-level.")
                    .worldRestart()
                    .defineInRange("deepBreathDamagePerLevel", 2.0, 0.5, 5);

            DEEP_BREATH_STACK_COUNT_PER_LEVEL = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.deep_breath_stack_count_per_level")
                    .comment("The \"max damage stack\" count added by each level of the Deep Breath enchantment.",
                            "",
                            "This is the maximum amount of damage you can deal with this enchantment before its bonus goes to the minimum.")
                    .worldRestart()
                    .defineInRange("deepBreathStackCountPerLevel", 20.0, 1.0, 50.0);

            DEEP_BREATH_MINIMUM_EXTRA_DAMAGE = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.deep_breath_minimum_extra_damage")
                    .comment("A multiplier for the damage bonus of the Deep Breath enchantment once it runs out of stacks.",
                            "",
                            "At 0.5, it will give half of its damage bonus at a minimum. At 0, it will give none of its damage bonus at a minimum.",
                            "",
                            "I recommend keeping this value below ~0.2, to avoid making Sharpness obsolete.")
                    .worldRestart()
                    .defineInRange("deepBreathMinimumExtraDamage", 0.1, 0.0, 1.0);

            DEEP_BREATH_COOLDOWN_TIME = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.deep_breath_cooldown_time")
                    .comment("The amount of time it takes, in seconds, from 0 stacks, for the Deep Breath enchantment to fully recharge its damage bonus.")
                    .worldRestart()
                    .defineInRange("deepBreathCooldownTime", 25.0, 1.0, 240.0);

            DEEP_BREATH_COOLDOWN_TIME_DELAY = SERVER_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.server.deep_breath_cooldown_time_delay")
                    .comment("The amount of time it takes, in seconds, for the Deep Breath enchantment to start recharging stacks after the weapon it's on was last used to hit something.")
                    .worldRestart()
                    .defineInRange("deepBreathCooldownTimeDelay", 1, 0.0, 10.0);

            SERVER_CONFIG_BUILDER.pop();
        SERVER_CONFIG_BUILDER.pop();



        SERVER_CONFIG  = SERVER_CONFIG_BUILDER.build();

        // ======= CLIENT CONFIG =======
        CLIENT_CONFIG_BUILDER = new ModConfigSpec.Builder();

        // Blue Axolotl

            // If I wasn't getting copyright claimed before, I definitely will be now...
            BLUE_AXOLOTL_PING_SOUND = CLIENT_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.client.blue_axolotl_ping_client")
                    .comment(
                            "Plays a fitting sound effect when you're near a blue axolotl, or when one spawns in near you.",
                            "",
                            "Blue Axolotl Ping must be enabled in the server config for this to take effect.",
                            "",
                            "Values:",
                            "",
                            "  NONE - No sound effect plays.",
                            "",
                            "  BW - Plays the Shiny Pokemon sound effect from Pokemon Black and White.",
                            "",
                            "  PLA - Plays the Shiny Pokemon sound effect from Pokemon Legends: Arceus and Pokemon Legends: ZA.",
                            ""
                    )
                    .defineEnum(
                            "shinyPingForBlueAxolotl",
                            BlueAxolotlPing.BlueAxolotlSpawnSfx.PLA,
                            EnumGetMethod.ORDINAL_OR_NAME_IGNORECASE,
                            BlueAxolotlPing.BlueAxolotlSpawnSfx.NONE,
                            BlueAxolotlPing.BlueAxolotlSpawnSfx.BW,
                            BlueAxolotlPing.BlueAxolotlSpawnSfx.PLA
                    );

            BLOCK_ANIMATION_SPEED = CLIENT_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.client.block_animation_speed")
                    .comment("Multiplies the speed of certain flowing block animations. Set to 0 to disable these animations entirely.",
                            "",
                            "WARNING: Some passive animations may cause eyestrain at higher speeds. Increase this value at your own risk.")
                    .defineInRange("blockAnimationSpeed", 1.0, 0, 10.0);

        CLIENT_CONFIG = CLIENT_CONFIG_BUILDER.build();

        // ======= STARTUP CONFIG =======

        // DO NOT USE TO ENABLE/DISABLE FEATURES, EASILY DESYNCED
        STARTUP_CONFIG_BUILDER = new ModConfigSpec.Builder();

        STARTUP_CONFIG_BUILDER.push("mobstattweaks");

        // Stat changes
            ENDER_DRAGON_HEALTH = STARTUP_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.startup.enderdragon_health")
                    .comment("Changes the ender dragon's max health to this amount.",
                            "",
                            "Default: 200. Will be ignored if left at its default.",
                            "",
                            "For technical reasons, cannot exceed 1024.",
                            "",
                            "NOTE: has no effect on already-spawned ender dragons.")
                    .defineInRange("enderDragonHealthModifier", 200.0, 1.0, 1024.0);

            WITHER_HEALTH = STARTUP_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.startup.wither_health")
                    .comment("Changes the wither's max health to this amount.",
                            "",
                            "Default: 300. Will be ignored if left at its default.",
                            "",
                            "For technical reasons, cannot exceed 1024.",
                            "",
                            "NOTE: has no effect on already-spawned withers.")
                    .defineInRange("witherHealthModifier", 300, 1.0, 1024.);

            WARDEN_HEALTH = STARTUP_CONFIG_BUILDER
                    .translation("tgcshenanigans.config.startup.warden_health")
                    .comment("Changes the warden's max health to this amount.",
                            "",
                            "Default: 500. Will be ignored if left at its default.",
                            "",
                            "For technical reasons, cannot exceed 1024.",
                            "",
                            "NOTE: has no effect on already-spawned wardens.")
                    .defineInRange("wardenHealthModifier", 500, 1.0, 1024.0);

        STARTUP_CONFIG_BUILDER.pop();

        STARTUP_CONFIG = STARTUP_CONFIG_BUILDER.build();

    }

    private static boolean validateEntityName(final Object obj) {

        if (obj instanceof String string) {

            ResourceLocation location = ResourceLocation.tryParse(string);

            if (location != null) {
                return BuiltInRegistries.ENTITY_TYPE.containsKey(location);
            }

        }

        return false;

    }


}

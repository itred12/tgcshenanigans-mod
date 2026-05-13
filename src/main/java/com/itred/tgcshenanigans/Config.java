package com.itred.tgcshenanigans;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.itred.tgcshenanigans.config.BlueAxolotlSpawnSfx;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    private static final ModConfigSpec.Builder SERVER_CONFIG_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_BLUE_AXOLOTL_PING = SERVER_CONFIG_BUILDER
            .translation("tgcshenanigans.config.server.blue_axolotl_ping_masterswitch")
            .comment("Enable or disable the sound effect that plays whenever a player is near a blue axolotl.")
            .worldRestart()
            .define("blueAxolotlPingMasterswitch", true);

    public static final ModConfigSpec.BooleanValue ALLOW_BLUE_AXOLOTLS_SPAWN_NATURALLY = SERVER_CONFIG_BUILDER
            .translation("tgcshenanigans.config.server.blue_axolotl_spawn_naturally")
            .comment("Enable or disable blue axolotls spawning naturally.")
            .comment("")
            .comment("When disabled, blue axolotls will only spawn as offspring from other Axolotls (Vanilla behavior).")
            .comment("")
            .comment("When enabled, blue axolotls can spawn out in the wild.")
            .define("blueAxolotNaturalSpawn", true);

    public static final ModConfigSpec.IntValue BLUE_AXOLOTL_SPAWNCHANCE = SERVER_CONFIG_BUILDER
            .translation("tgcshenanigans.config.server.blue_axolotl_spawn_chance")
            .comment("Modify the chance that a blue axolotl can spawn naturally. Default is 1 in 1200.")
            .comment("")
            .comment("Only takes effect if Blue Axolotl Natural Spawn is enabled.")
            .defineInRange("blueAxolotlNaturalSpawnChance", 1200, 1, 8192);

    public static final ModConfigSpec.IntValue BLUE_AXOLOTL_SPAWNCHANCE_OFFSPRING = SERVER_CONFIG_BUILDER
            .translation("tgcshenanigans.config.server.blue_axolotl_spawn_chance_offspring")
            .comment("Modify the chance that a blue axolotl can spawn naturally as offspring from two other axolotl.")
            .comment("")
            .comment("Default is 1 in 1200 (Vanilla chance)")
            .defineInRange("blueAxolotlNaturalSpawnChanceOffspring", 1200, 1, 8192);

    public static final ModConfigSpec.BooleanValue DISABLE_CRAB = SERVER_CONFIG_BUILDER
            .translation("tgcshenanigans.config.server.disable_crab")
            .comment("Ninni's Spawn mod: prevent Spider Crabs from spawning. One of my friends really disliked them.",
                    "",
                    "WILL IRREVERSABLY DELETE ALL SPIDER CRABS CURRENTLY IN THE WORLD UPON RELOADING. YOU HAVE BEEN WARNED.")
            .worldRestart()
            .define("disableCrab", false);





    static final ModConfigSpec SERVER_CONFIG = SERVER_CONFIG_BUILDER.build();

    private static final ModConfigSpec.Builder CLIENT_CONFIG_BUILDER = new ModConfigSpec.Builder();

    // If I wasn't getting copyright claimed before, I definitely will be now...
    public static final ModConfigSpec.EnumValue<BlueAxolotlSpawnSfx> BLUE_AXOLOTL_PING_SOUND = CLIENT_CONFIG_BUILDER
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
            BlueAxolotlSpawnSfx.PLA,
            EnumGetMethod.ORDINAL_OR_NAME_IGNORECASE,
            BlueAxolotlSpawnSfx.NONE,
            BlueAxolotlSpawnSfx.BW,
            BlueAxolotlSpawnSfx.PLA
    );

    static final ModConfigSpec CLIENT_CONFIG = CLIENT_CONFIG_BUILDER.build();


    // Registered as registries are, may have desync between server and client. Do NOT use to enable/disable features.
    private static final ModConfigSpec.Builder STARTUP_CONFIG_BUILDER = new ModConfigSpec.Builder();


    public static final ModConfigSpec.DoubleValue ENDER_DRAGON_HEALTH = STARTUP_CONFIG_BUILDER
            .translation("tgcshenanigans.config.server.enderdragon_health")
            .comment("Changes the ender dragon's max health to this amount.",
                    "",
                    "Default: 200. Will be ignored if left at its default.",
                    "",
                    "NOTE: has no effect on already-spawned ender dragons.")
            .defineInRange("enderDragonHealthModifier", 200.0, 1.0, 1024.0);

    public static final ModConfigSpec.DoubleValue WITHER_HEALTH = STARTUP_CONFIG_BUILDER
            .translation("tgcshenanigans.config.server.wither_health")
            .comment("Changes the wither's max health to this amount.",
                    "",
                    "Default: 300. Will be ignored if left at its default.",
                    "",
                    "NOTE: has no effect on already-spawned withers.")
            .defineInRange("witherHealthModifier", 300, 1.0, 1024.);


    public static final ModConfigSpec.DoubleValue WARDEN_HEALTH = STARTUP_CONFIG_BUILDER
            .translation("tgcshenanigans.config.server.warden_health")
            .comment("Changes the warden's max health to this amount.",
                    "",
                    "Default: 500. Will be ignored if left at its default.",
                    "",
                    "NOTE: has no effect on already-spawned wardens.")
            .defineInRange("wardenHealthModifier", 500, 1.0, 1024.0);


    static final ModConfigSpec STARTUP_CONFIG = STARTUP_CONFIG_BUILDER.build();

}

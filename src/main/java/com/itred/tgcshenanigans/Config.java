package com.itred.tgcshenanigans;

import java.util.List;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.itred.tgcshenanigans.config.BlueAxolotlSpawnSfx;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

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

    private static final ModConfigSpec.Builder CLIENT_CONFIG_BUILDER = new ModConfigSpec.Builder();

    // If I wasn't getting copyright claimed before, I definitely will be now...
    public static final ModConfigSpec.EnumValue<BlueAxolotlSpawnSfx> BLUE_AXOLOTL_PING = CLIENT_CONFIG_BUILDER
            .translation("tgcshenanigans.config.client.blue_axolotl_ping")
            .comment(
                    "Plays a fitting sound effect when you're near a blue axolotl, or when one spawns in near you.",
                    "",
                    "blueAxolotlPing must be enabled in the server config for this to take effect.",
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




}

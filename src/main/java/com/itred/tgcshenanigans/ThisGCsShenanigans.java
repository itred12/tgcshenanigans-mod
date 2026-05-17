package com.itred.tgcshenanigans;

import com.itred.tgcshenanigans.block.TGCSBlockEntities;
import com.itred.tgcshenanigans.block.TGCSBlocks;
import com.itred.tgcshenanigans.block.entity.renderer.ProphecyPanelBlockEntityRenderer;
import com.itred.tgcshenanigans.compat.originsneoforge.OriginsRegistries;
import com.itred.tgcshenanigans.component.TGCSDataComponents;
import com.itred.tgcshenanigans.enchantment.TGCSEnchantmentEffects;
import com.itred.tgcshenanigans.event.TGCSCommonEvents;
import com.itred.tgcshenanigans.item.TGCSCreativeModeTabs;
import com.itred.tgcshenanigans.item.TGCSItems;
import com.itred.tgcshenanigans.loot.TGCSLootTables;
import com.itred.tgcshenanigans.sound.TGCSSounds;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.Map;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ThisGCsShenanigans.MODID)
public class ThisGCsShenanigans {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "tgcshenanigans";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "tgcshenanigans" namespace
    //public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "tgcshenanigans" namespace

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "tgcshenanigans" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "tgcshenanigans:example_block", combining the namespace and path
    //public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "tgcshenanigans:example_block", combining the namespace and path
    //public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);


    // Creates a creative tab with the id "tgcshenanigans:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TGCS_TAB = CREATIVE_MODE_TABS.register("tgcs_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tgcshenanigans")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(Items.CAMPFIRE::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.accept(TGCSItems.DISC_FIREPLACE);
            }).build());

    // Recipe serializer
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ThisGCsShenanigans.MODID);


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ThisGCsShenanigans(IEventBus modEventBus, ModContainer modContainer) {
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        modContainer.registerConfig(ModConfig.Type.STARTUP, Config.STARTUP_CONFIG);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onBossAttributesLoaded);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ThisGCsShenanigans) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register items
        TGCSItems.registerAll(modEventBus);

        // Blocks
        TGCSBlocks.registerAll(modEventBus);

        // Register creative mode tabs
        TGCSCreativeModeTabs.registerAll(modEventBus);

        // Register sounds
        TGCSSounds.registerAll(modEventBus);

        // Register data components
        TGCSDataComponents.registerAll(modEventBus);

        // Register recipe serializers
        SERIALIZER_REGISTRY.register(modEventBus);

        // Enchantments
        TGCSEnchantmentEffects.registerAll(modEventBus);

        // Events
        TGCSCommonEvents.onBoot(modEventBus);

        modEventBus.addListener(ThisGCsShenanigans::registerRenderers);

        TGCSBlockEntities.register(modEventBus);


        // Origins stuff
        if (ModList.get().isLoaded("origins")) {
            ThisGCsShenanigans.LOGGER.info("Activating origins compat!");
            OriginsRegistries.ENTITY_ACTION_REGISTRY.register(modEventBus);
            OriginsRegistries.POWER_REGISTRY.register(modEventBus);
        }


    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code

        /*
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));

         */


    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
         //   event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }


    // TODO: fix damage reduction applying for old bow shots– try to interpret damage timestamp
    @SubscribeEvent
    public void crossbowModify(LivingIncomingDamageEvent event) {
        Entity entity = event.getEntity();

        DamageSource source = event.getSource();
        ItemStack stack = source.getWeaponItem();

        if (stack != null && stack.is(ItemTags.CROSSBOW_ENCHANTABLE) && testForEnchant(stack, Enchantments.MULTISHOT) && !source.isDirect()) {

            event.setInvulnerabilityTicks(0);


            if (entity instanceof LivingEntity livingEntity) {
                DamageSource lastSource = livingEntity.getLastDamageSource();

                if (lastSource != null) {
                    ItemStack lastWeapon = lastSource.getWeaponItem();

                    ThisGCsShenanigans.LOGGER.info(String.valueOf(livingEntity.getLastHurtByMobTimestamp()));

                    if (lastWeapon != null && lastWeapon.is(ItemTags.CROSSBOW_ENCHANTABLE) && testForEnchant(lastWeapon, Enchantments.MULTISHOT)) {

                        event.setAmount(event.getOriginalAmount() / 2);

                    }
                }
            }


        }
    }


    public boolean testForEnchant(ItemStack stack, ResourceKey<Enchantment> enchant) {

        for (Holder<Enchantment> itemEnchantment : stack.getTagEnchantments().keySet()) {
            if (itemEnchantment.is(enchant)) {
                return true;
            }
        }

        return false;

    }


    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer((BlockEntityType) TGCSBlockEntities.PROPHECY_PANEL.get(), ProphecyPanelBlockEntityRenderer::new);
    }

    // Easiest way I could find to add items to existing loot tables
    // TODO: CLEAN THIS UP
    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();
        LootTable table = event.getTable();

        if (name.equals(BuiltInLootTables.BASTION_TREASURE.location())) {
            table.addPool(generateLootPool(TGCSLootTables.INJECT_BASTION_TREASURE));
        }

        if (name.equals(BuiltInLootTables.ANCIENT_CITY.location())) {
            table.addPool(generateLootPool(TGCSLootTables.INJECT_ANCIENT_CITY));
        }

        if (name.equals(BuiltInLootTables.BURIED_TREASURE.location())) {
            table.addPool(generateLootPool(TGCSLootTables.INJECT_BURIED_TREASURE));
        }
    }

    private static LootPool generateLootPool(ResourceKey<LootTable> lootKey) {
        return LootPool.lootPool().add(generateLootEntry(lootKey))
                .setBonusRolls(UniformGenerator.between(0, 1))
                .name(ThisGCsShenanigans.MODID + "_inject")
                .build();
    }

    private static LootPoolEntryContainer.Builder<?> generateLootEntry(ResourceKey<LootTable> tableKey) {
        return NestedLootTable.lootTableReference(tableKey).setWeight(1);
    }


    // Key-value pairs, where the key is an entity type, and the value is a double-config that holds a health modifier for that entity.
    private static final Map<EntityType<? extends LivingEntity>, ModConfigSpec.DoubleValue> HEALTH_MODIFIERS = Map.of(
            EntityType.ENDER_DRAGON, Config.ENDER_DRAGON_HEALTH,
            EntityType.WITHER, Config.WITHER_HEALTH,
            EntityType.WARDEN, Config.WARDEN_HEALTH
    );

    // Modify the health of entities in the above table
    public void onBossAttributesLoaded(EntityAttributeModificationEvent event) {

        for (Map.Entry<EntityType<? extends LivingEntity>, ModConfigSpec.DoubleValue> entry : HEALTH_MODIFIERS.entrySet()) {

            EntityType<? extends LivingEntity> entity = entry.getKey();
            ModConfigSpec.DoubleValue health = entry.getValue();

            if (!health.get().equals(health.getDefault())) {
                event.add(entity, Attributes.MAX_HEALTH, health.get());
            }



        }

    }






}

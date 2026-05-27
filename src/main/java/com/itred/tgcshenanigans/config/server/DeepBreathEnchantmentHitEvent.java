package com.itred.tgcshenanigans.config.server;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.config.IConfiguredEventHandler;
import com.itred.tgcshenanigans.data.TGCSDataComponents;
import com.itred.tgcshenanigans.datagen.registry.TGCSEnchantmentRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class DeepBreathEnchantmentHitEvent implements IConfiguredEventHandler {


    // Defaults, to be safe
    private static double DAMAGE_PER_LEVEL = 2.0;
    private static double STACK_COUNT_PER_LEVEL = 20.0;
    private static double MINIMUM_EXTRA_DAMAGE = 0.1;
    private static double MAX_RECHARGE_TIME = 25.0;
    private static double RECHARGE_DELAY = 1.0;

    @Override
    public void enable(IEventBus bus, IConfiguredEventHandler instance) {
        DAMAGE_PER_LEVEL = Config.DEEP_BREATH_DAMAGE_PER_LEVEL.get();
        STACK_COUNT_PER_LEVEL = Config.DEEP_BREATH_STACK_COUNT_PER_LEVEL.get();
        MINIMUM_EXTRA_DAMAGE = Config.DEEP_BREATH_MINIMUM_EXTRA_DAMAGE.get();
        MAX_RECHARGE_TIME = Config.DEEP_BREATH_COOLDOWN_TIME.get();
        RECHARGE_DELAY = Config.DEEP_BREATH_COOLDOWN_TIME_DELAY.get();
        NeoForge.EVENT_BUS.register(instance);
    }

    @Override
    public void disable(IEventBus bus, IConfiguredEventHandler instance) {
        NeoForge.EVENT_BUS.unregister(instance);
    }


    // TODO 2: Add indicator for current stack charge (maybe using the cooldown indicator w/ mixin nonsense?)
    @SubscribeEvent
    public void onLivingHit(LivingDamageEvent.Pre event) {

        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        Level level = entity.level();

        if (source.getWeaponItem() == null || source.getDirectEntity() == null) {
            return;
        }

        ItemStack stack = source.getWeaponItem();
        Entity owner = source.getDirectEntity();

        if (level.getServer() == null) {
            return;
        }

        EnchantmentHelper.runIterationOnItem(stack, (enchantmentHolder, i) -> {

            if (!enchantmentHolder.is(TGCSEnchantmentRegistryProvider.DEEP_BREATH)) {
                return;
            }

            event.setNewDamage(event.getNewDamage() + deepBreathOnDamage(level, level.getServer(), i, stack, entity, owner, event.getNewDamage()));

        });

    }




    // Quick fix to reset the data components of the item, so it doesnt get stuck with the overlay at a certain amount when disenchanting
    @SubscribeEvent
    private void onEnchantmentQuery(GetEnchantmentLevelEvent event) {

        HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = event.getLookup();
        ItemStack stack = event.getStack();

        if (!stack.has(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE)) {
            return;
        }

        // Sometimes this event is called seeking a single enchant (i.e., looting when a mob is killed)
        // Getting the level of other enchantments from the event wont provide the full list of enchants on the item in question,
        // So we first make sure that this enchantment is (at least a part of) the target for the event
        // (I think??)
        if (!event.isTargetting(TGCSEnchantmentRegistryProvider.DEEP_BREATH)) {
            return;
        }

        int enchLevel = event.getEnchantments().getLevel(enchantmentRegistryLookup.getOrThrow(TGCSEnchantmentRegistryProvider.DEEP_BREATH));

        if (enchLevel == 0) {
            ThisGCsShenanigans.LOGGER.info("DEBUG: Removed no-longer-used data from an item that formerly had the Deep Breath enchantment!");
            stack.remove(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE);
            stack.remove(TGCSDataComponents.DEEP_BREATH_STACKS);
            stack.remove(TGCSDataComponents.DEEP_BREATH_STACK_CAP);
        }

    }



    // TODO: Particle effects on hit? Mono-wielding by making it only activate on crit???
    // TODO 2: see about modifying the tooltip to reflect current damage only client-side?
    private float deepBreathOnDamage(Level level, MinecraftServer server, int enchantmentLevel, ItemStack stack, Entity target, Entity owner, float currentDamage) {

        double damageBonus = enchantmentLevel * DAMAGE_PER_LEVEL;


        // Makes the damage-per-level a bit less extreme
        // 20 at level 1,
        // 35 at level 2,
        // 50 at level 3,
        // 65 at level 4,
        // 80 at level 5
        double damageCap = enchantmentLevel * STACK_COUNT_PER_LEVEL;
        stack.set(TGCSDataComponents.DEEP_BREATH_STACK_CAP, damageCap);
        double currentDamageStacks = getDeepBreathStacks(level, stack, damageCap);

        // ThisGCsShenanigans.LOGGER.debug(String.valueOf(currentDamageStacks));


        // The range [0,1] of the damage multiplier, given by currentDamageStacks / damageCap, is multiplied by 1 - the minimum
        // The minimum is then added to that.
        // By default, the this makes the stack calculation only contribute to 90% of the extra damage, with the remaining 10% always being there.
        // This is modifiable by the config, of course.
        double actualDamage = (MINIMUM_EXTRA_DAMAGE + ((1 - MINIMUM_EXTRA_DAMAGE) * (currentDamageStacks / damageCap))) * damageBonus;

        // Subtract the amount from the damage stacks and reapply it on the item– we can only modify components here, since durability is being modified anyways (probably)
        stack.set(TGCSDataComponents.DEEP_BREATH_STACKS,
                Math.max(0.0, (float)
                        (currentDamageStacks -
                                // try and prevent overkill
                                (target instanceof LivingEntity livingEntity ?
                                        Math.min(currentDamage + actualDamage, livingEntity.getHealth())
                                        : currentDamage + actualDamage))
                )
        );
        stack.set(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE, level.getGameTime());

        // Now we can *actually* deal damage.
        return (float) actualDamage;

        // DEBUG
            /*
            ThisGCsShenanigans.LOGGER.info("Current damage stacks: " + String.valueOf(currentDamageStacks));
            ThisGCsShenanigans.LOGGER.info("Last dealt damage (seconds): " + String.valueOf(secondsSinceLastStrike));
            ThisGCsShenanigans.LOGGER.info("Tick of last damage: " + String.valueOf(lastDamage));
            ThisGCsShenanigans.LOGGER.info("Current tick: " + String.valueOf(currentTime));
            ThisGCsShenanigans.LOGGER.info("Extra damage: " + String.valueOf(actualDamage));
            ThisGCsShenanigans.LOGGER.info("Total damage: " + String.valueOf(entityDamage + actualDamage));
            ThisGCsShenanigans.LOGGER.info("Min total damage: " + String.valueOf( entityDamage ));
            ThisGCsShenanigans.LOGGER.info("Max total damage: " + String.valueOf( entityDamage + damageBonus));
            ThisGCsShenanigans.LOGGER.info("Max damage stacks: " + String.valueOf(damageCap));
            */

    }





    public static double getDeepBreathStacks(Level level, ItemStack stack, double deepBreathStackCap) {

        long time = level.getGameTime();
        long lastDamageTime = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE, 0L);

        // "Refill" the player's damage stacks by taking the difference of the current server time and when they last dealt damage with the weapon
        double differenceInSeconds = (time - lastDamageTime) / 20.0;

        double currentDamageStacks = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_STACKS, deepBreathStackCap);

        // Start recharging 1 second after the last strike
        if (differenceInSeconds >= RECHARGE_DELAY) {

            // "Give back" stacks to the weapon based on this math.
            // If the recharge time is 15, and they last used the weapon 10 seconds ago,
            // then they'll have "recharged" 2/3 of the Deep Breath stacks
            double rechargeTime = differenceInSeconds - RECHARGE_DELAY;
            double giveBack = Math.min(1.0, rechargeTime / MAX_RECHARGE_TIME);
            currentDamageStacks = Math.min(
                    deepBreathStackCap,
                    currentDamageStacks + (deepBreathStackCap * giveBack)
            );
        }

        return currentDamageStacks;

    }


}

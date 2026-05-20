package com.itred.tgcshenanigans.event.common;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.data.TGCSDataComponents;
import com.itred.tgcshenanigans.enchantment.TGCSEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class DeepBreathEnchantmentHitEvent {


    public static final int MAX_RECHARGE_TIME = 20;
    public static final int DAMAGE_CAP_PER_LEVEL = 20;
    public static final float DAMAGE_PER_LEVEL = 2f;
    public static final float RECHARGE_DELAY = 0;


    // TODO 2: Add indicator for current stack charge (maybe using the cooldown indicator w/ mixin nonsense?)
    @SubscribeEvent
    public static void onLivingHit(LivingDamageEvent.Pre event) {

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

            if (!enchantmentHolder.is(TGCSEnchantments.DEEP_BREATH)) {
                return;
            }

            event.setNewDamage(event.getNewDamage() + deepBreathOnDamage(level, level.getServer(), i, stack, entity, owner, event.getNewDamage()));

        });

    }




    // Quick fix to reset the data components of the item, so it doesnt get stuck with the overlay at a certain amount when disenchanting
    @SubscribeEvent
    private static void onEnchantmentQuery(GetEnchantmentLevelEvent event) {

        HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = event.getLookup();
        ItemStack stack = event.getStack();

        if (!stack.has(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE)) {
            return;
        }

        // Sometimes this event is called seeking a single enchant (i.e., looting when a mob is killed)
        // Getting the level of other enchantments from the event wont provide the full list of enchants on the item in question,
        // So we first make sure that this enchantment is (at least a part of) the target for the event
        // (I think??)
        if (!event.isTargetting(TGCSEnchantments.DEEP_BREATH)) {
            return;
        }

        int enchLevel = event.getEnchantments().getLevel(enchantmentRegistryLookup.getOrThrow(TGCSEnchantments.DEEP_BREATH));

        if (enchLevel == 0) {
            ThisGCsShenanigans.LOGGER.info("DEBUG: Removed no-longer-used data from an item that formerly had the Deep Breath enchantment!");
            stack.remove(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE);
            stack.remove(TGCSDataComponents.DEEP_BREATH_STACKS);
            stack.remove(TGCSDataComponents.DEEP_BREATH_STACK_CAP);
        }

    }



    // TODO: Particle effects on hit? Mono-wielding by making it only activate on crit???
    // TODO 2: see about modifying the tooltip to reflect current damage only client-side?
    private static float deepBreathOnDamage(Level level, MinecraftServer server, int enchantmentLevel, ItemStack stack, Entity target, Entity owner, float currentDamage) {

        double damageBonus = enchantmentLevel * DAMAGE_PER_LEVEL;


        // Makes the damage-per-level a bit less extreme
        // 20 at level 1,
        // 35 at level 2,
        // 50 at level 3,
        // 65 at level 4,
        // 80 at level 5
        int damageCap = enchantmentLevel * DAMAGE_CAP_PER_LEVEL + ((1 - enchantmentLevel) * 5);
        stack.set(TGCSDataComponents.DEEP_BREATH_STACK_CAP, damageCap);
        float currentDamageStacks = getDeepBreathStacks(level, stack, damageCap);

        ThisGCsShenanigans.LOGGER.debug(String.valueOf(currentDamageStacks));


        // Now all we need to do is take the percentage of stacks from the maximum and use that as a multiplier to the maximum damage bonus.
        // The weapon will get the full bonus if it hasn't attacked in 15 seconds,
        // The bonus diminishes as the weapon deals damage according to the damage dealt,
        // At level 5, the bonus recharges at a rate of 2 damage per second.
        // Does that make sense? I hope it makes sense.
        double actualDamage = (currentDamageStacks / damageCap) * damageBonus;

        // Subtract the amount from the damage stacks and reapply it on the item– we can only modify components here, since durability is being modified anyways (probably)
        stack.set(TGCSDataComponents.DEEP_BREATH_STACKS,
                Math.max(0, (float)
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





    public static float getDeepBreathStacks(Level level, ItemStack stack, int deepBreathStackCap) {

        long time = level.getGameTime();
        long lastDamageTime = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE, 0L);

        // "Refill" the player's damage stacks by taking the difference of the current server time and when they last dealt damage with the weapon
        float differenceInSeconds = (float) (time - lastDamageTime) / 20;

        float currentDamageStacks = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_STACKS, (float) deepBreathStackCap);

        // Start recharging 1 second after the last strike
        if (differenceInSeconds >= RECHARGE_DELAY) {

            // "Give back" stacks to the weapon based on this math.
            // If the recharge time is 15, and they last used the weapon 10 seconds ago,
            // then they'll have "recharged" 2/3 of the Deep Breath stacks
            float rechargeTime = differenceInSeconds - RECHARGE_DELAY;
            float giveBack = Math.min(1, rechargeTime / DeepBreathEnchantmentHitEvent.MAX_RECHARGE_TIME);
            currentDamageStacks = Math.min(
                    deepBreathStackCap,
                    currentDamageStacks + (deepBreathStackCap * giveBack)
            );
        }



        return currentDamageStacks;

    }
}

package com.itred.tgcshenanigans.event.common;

import com.itred.tgcshenanigans.component.TGCSDataComponents;
import com.itred.tgcshenanigans.enchantment.TGCSEnchantments;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class DeepBreathEnchantmentHitEvent {

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

            event.setNewDamage(event.getOriginalDamage() + deepBreathOnDamage(level, level.getServer(), i, stack, entity, owner));

        });

    }


    private static final int MAX_RECHARGE_TIME = 15;
    private static final int DAMAGE_FALLOFF_TO_0_PER_LEVEL = 10;
    private static final float DAMAGE_PER_LEVEL = 2.5f;

    private static float deepBreathOnDamage(Level level, MinecraftServer server, int enchantmentLevel, ItemStack stack, Entity target, Entity owner) {

        double damageBonus = enchantmentLevel * DAMAGE_PER_LEVEL;

        // Makes the damage-per-level a bit less extreme
        // 10 at level 1,
        // 20 - 5 = 15 at level 2,
        // 30 - 10 = 20 at level 3,
        // 40 - 15 = 25 at level 4,
        // 50 - 20 = 30 at level 5
        float damageCap = enchantmentLevel * DAMAGE_FALLOFF_TO_0_PER_LEVEL + ((1 - enchantmentLevel) * 5);

        float currentDamageStacks = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_STACKS, damageCap);

        // ThisGCsShenanigans.LOGGER.info("Starting stacks: " + String.valueOf(currentDamageStacks));

        // "Refill" the player's damage stacks by taking the difference of the current server time and when they last dealt damage with the weapon
        int lastDamage = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE, 0);
        int currentTime = server.getTickCount();

        // The last damage will only ever be greater than the server's current time on relog.
        // No real way to address this, just assume full effectiveness.
        if (lastDamage > currentTime) {
            lastDamage = 0;
        }

        float secondsSinceLastStrike = (float) (currentTime - lastDamage) / 20;

        // Start recharging 2 seconds after the last strike
        if (secondsSinceLastStrike > 2) {

            float rechargeTime = secondsSinceLastStrike - 2;
            // "Give back" stacks to the weapon based on this math.
            // If the recharge time is 15, and they last used the weapon 10 seconds ago,
            // then they'll have "recharged" 2/3 of the Deep Breath stacks
            float giveBack = Math.min(1, rechargeTime / MAX_RECHARGE_TIME);
            currentDamageStacks = Math.min(
                    damageCap,
                    currentDamageStacks + (damageCap * giveBack)
            );

            // Also debug
            // ThisGCsShenanigans.LOGGER.info("Stacks given back: " + String.valueOf((damageCap * giveBack)));

        }

        // Now all we need to do is take the percentage of stacks from the maximum and use that as a multiplier to the maximum damage bonus.
        // The weapon will get the full bonus if it hasn't attacked in 15 seconds,
        // The bonus diminishes as the weapon deals damage according to the damage dealt,
        // At level 5, the bonus recharges at a rate of 2 damage per second.
        // Does that make sense? I hope it makes sense.
        double actualDamage = (currentDamageStacks / damageCap) * damageBonus;

        // Subtract the amount from the damage stacks and reapply it on the item– we can only modify components here, since durability is being modified anyways (probably)

        // This accounts for the damage of the sword as well (minus other enchantments that potentially boost damage)
        double entityDamage = (owner instanceof LivingEntity) ? ((LivingEntity) owner).getAttributeValue(Attributes.ATTACK_DAMAGE) : 0;

        stack.set(TGCSDataComponents.DEEP_BREATH_STACKS, Math.max(0, (float) (currentDamageStacks - (entityDamage + actualDamage))));
        stack.set(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE, currentTime);

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

}

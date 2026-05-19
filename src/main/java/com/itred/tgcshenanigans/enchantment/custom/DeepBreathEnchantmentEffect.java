package com.itred.tgcshenanigans.enchantment.custom;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.data.TGCSDataComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record DeepBreathEnchantmentEffect(Holder<DamageType> damageType) implements EnchantmentEntityEffect {
    public static final MapCodec<DeepBreathEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(objectInstance ->
            objectInstance.group(
                    DamageType.CODEC.fieldOf("damage_type").forGetter(DeepBreathEnchantmentEffect::damageType)
            ).apply(objectInstance, DeepBreathEnchantmentEffect::new));


    public static final int MAX_RECHARGE_TIME = 15;
    public static final int DAMAGE_FALLOFF_TO_0_PER_LEVEL = 10;
    public static final float DAMAGE_PER_LEVEL = 2.5f;

    // Unused now. Problems with Entity.hurt means I had to move this to an event. It eees what it ees.
    @Override
    public void apply(@NotNull ServerLevel serverLevel, int i, @NotNull EnchantedItemInUse enchantedItemInUse, @NotNull Entity entity, @NotNull Vec3 vec3) {

        ItemStack stack = enchantedItemInUse.itemStack();
        LivingEntity owner = enchantedItemInUse.owner();

        if (owner == null) {
            return;
        }

        double damageBonus = i * DAMAGE_PER_LEVEL;

        // Makes the damage-per-level a bit less extreme
        // 10 at level 1,
        // 20 - 5 = 15 at level 2,
        // 30 - 10 = 20 at level 3,
        // 40 - 15 = 25 at level 4,
        // 50 - 20 = 30 at level 5
        float damageCap = i * DAMAGE_FALLOFF_TO_0_PER_LEVEL + ((1 - i) * 5);

        float currentDamageStacks = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_STACKS, damageCap);

        ThisGCsShenanigans.LOGGER.info("Starting stacks: " + String.valueOf(currentDamageStacks));

        // "Refill" the player's damage stacks by taking the difference of the current server time and when they last dealt damage with the weapon
        int lastDamage = stack.getOrDefault(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE, 0);
        int currentTime = serverLevel.getServer().getTickCount();

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

            ThisGCsShenanigans.LOGGER.info("Stacks given back: " + String.valueOf((damageCap * giveBack)));

        }


        // Now all we need to do is take the percentage of stacks from the maximum and use that as a multiplier to the maximum damage bonus.
        // The weapon will get the full bonus if it hasn't attacked in 15 seconds,
        // The bonus diminishes as the weapon deals damage according to the damage dealt,
        // At level 5, the bonus recharges at a rate of 2 damage per second.
        // Does that make sense? I hope it makes sense.
        double actualDamage = (currentDamageStacks / damageCap) * damageBonus;

        // Now we can *actually* deal damage.
        DamageSource damageSource = owner instanceof Player ? entity.damageSources().playerAttack((Player) owner) : entity.damageSources().mobAttack(owner);

        boolean did = entity.hurt(damageSource, (float) actualDamage);
        ThisGCsShenanigans.LOGGER.info(String.valueOf((float) actualDamage));

        // Subtract the amount from the damage stacks and reapply it on the item– we can only modify components here, since durability is being modified anyways (probably)

        // This accounts for the damage of the sword as well (minus other enchantments that potentially boost damage)
        double entityDamage = owner.getAttributeValue(Attributes.ATTACK_DAMAGE);

        stack.set(TGCSDataComponents.DEEP_BREATH_STACKS, Math.max(0, (float) (currentDamageStacks - (entityDamage + actualDamage))));
        stack.set(TGCSDataComponents.DEEP_BREATH_LAST_DEALT_DAMAGE, currentTime);

        ThisGCsShenanigans.LOGGER.info("Current damage stacks: " + String.valueOf(currentDamageStacks));
        ThisGCsShenanigans.LOGGER.info("Last dealt damage (seconds): " + String.valueOf(secondsSinceLastStrike));
        ThisGCsShenanigans.LOGGER.info("Tick of last damage: " + String.valueOf(lastDamage));
        ThisGCsShenanigans.LOGGER.info("Current tick: " + String.valueOf(currentTime));
        ThisGCsShenanigans.LOGGER.info("Extra damage: " + String.valueOf(actualDamage));
        ThisGCsShenanigans.LOGGER.info("Total damage: " + String.valueOf(entityDamage + actualDamage));
        ThisGCsShenanigans.LOGGER.info("Min total damage: " + String.valueOf( entityDamage ));
        ThisGCsShenanigans.LOGGER.info("Max total damage: " + String.valueOf( entityDamage + damageBonus));
        ThisGCsShenanigans.LOGGER.info("Max damage stacks: " + String.valueOf(damageCap));

    }



    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}

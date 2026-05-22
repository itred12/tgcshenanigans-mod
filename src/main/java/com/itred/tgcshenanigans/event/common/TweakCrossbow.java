package com.itred.tgcshenanigans.event.common;

import com.itred.tgcshenanigans.TGCSUtils;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class TweakCrossbow {

    // TODO: fix damage reduction applying for old bow shots– try to interpret damage timestamp
    @SubscribeEvent
    public static void crossbowModify(LivingIncomingDamageEvent event) {
        Entity entity = event.getEntity();

        DamageSource source = event.getSource();
        ItemStack stack = source.getWeaponItem();

        if (stack != null && stack.is(ItemTags.CROSSBOW_ENCHANTABLE) && TGCSUtils.testForEnchant(stack, Enchantments.MULTISHOT) && !source.isDirect()) {

            event.setInvulnerabilityTicks(0);


            if (entity instanceof LivingEntity livingEntity) {
                DamageSource lastSource = livingEntity.getLastDamageSource();

                if (lastSource != null) {
                    ItemStack lastWeapon = lastSource.getWeaponItem();

                    ThisGCsShenanigans.LOGGER.info(String.valueOf(livingEntity.getLastHurtByMobTimestamp()));

                    if (lastWeapon != null && lastWeapon.is(ItemTags.CROSSBOW_ENCHANTABLE) && TGCSUtils.testForEnchant(lastWeapon, Enchantments.MULTISHOT)) {

                        event.setAmount(event.getOriginalAmount() / 2);

                    }
                }
            }


        }
    }


}

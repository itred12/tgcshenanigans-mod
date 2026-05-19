package com.itred.tgcshenanigans.event.common;

import com.itred.tgcshenanigans.item.TGCSItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.SweepAttackEvent;

public class BluntCleaverUtils {

    @SubscribeEvent
    public static void onPlayerSweep(SweepAttackEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getWeaponItem();

        if (stack.is(TGCSItems.BLUNT_CLEAVER)) {
            event.setSweeping(false);
        }
    }

    @SubscribeEvent
    public static void onPlayerCrit(CriticalHitEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getWeaponItem();

        if (stack.is(TGCSItems.BLUNT_CLEAVER)) {
            event.setCriticalHit(false);
        }
    }


}

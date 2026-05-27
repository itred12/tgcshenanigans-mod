package com.itred.tgcshenanigans.config.server;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.TGCSUtils;
import com.itred.tgcshenanigans.config.IConfiguredEventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class TweakCrossbow implements IConfiguredEventHandler {


    // TODO: fix damage reduction applying for old bow shots– try to interpret damage timestamp

    // Multiplies the damage of arrows fired from a bow by this amount
    private static final ModConfigSpec.DoubleValue BOW_DAMAGE_MULT = Config.BOW_DAMAGE_MULTIPLIER;
    // Multiplies the damage of subsequent multishot arrows (every arrow that hits an entity after the first from a single batch) by this amount
    private static final ModConfigSpec.DoubleValue MULTISHOT_EXTRA_ARROW_DAMAGE_MULT = Config.MULTISHOT_EXTRA_ARROW_DAMAGE_MULTIPLIER;
    // Enables/disables multishot ignoring iframes (well, not granting them)
    private static final ModConfigSpec.BooleanValue MULTISHOT_NO_IFRAMES = Config.MULTISHOT_NO_IFRAMES;

    @SubscribeEvent
    private void modifyRangedWeaponry(LivingIncomingDamageEvent event) {

        DamageSource source = event.getSource();
        Entity attackingEntity = source.getDirectEntity();

        if (attackingEntity instanceof Arrow arrowEntity) {

            ItemStack weapon = arrowEntity.getWeaponItem();
            // This isnt marked as nullable, but in both Arrow and AbstractArrow the property it grabs *is*???
            // And there doesn't appear to be any other safety?? So I check it just to be safe.
            if (weapon == null) {
                return;
            }

            // Reduce the damage of arrows fired from a bow (my goodness it's ridiculous in the vanilla game)
            if (weapon.getItem() instanceof BowItem) {
                // I use double values for the config and manually cast them to a float here because for some reason the game had a hissy fit when loading from the config???
                // And kept loading them as doubles even when I told it to store it as a "float" config value???
                // Alright, sure. We can do it this way
                event.setAmount((float) (event.getAmount() * BOW_DAMAGE_MULT.get()));
            }

            // Make multishot arrows not grant invulnerability on hit (so all arrows can hit at once), but every consequtive arrow after the first does a pinch less damage.
            if (weapon.getItem() instanceof CrossbowItem) {

                if (MULTISHOT_NO_IFRAMES.get() && TGCSUtils.testForEnchant(weapon, Enchantments.MULTISHOT)) {
                    event.setInvulnerabilityTicks(0);

                    LivingEntity target = event.getEntity();
                    DamageSource lastDamageSource = target.getLastDamageSource();

                    // Now this is a bit cheeky... Comparing the last weapon and this one doesnt seem to work, I imagine that's due to the ammo technically being different or something?
                    // Not sure, but I dont want to check if the last weapon also had the multishot enchantment too, since checking enchantments this way is a bit expensive.
                    // But if they're hit by a crossbow of any other type, they'll have invulnerability, meaning the next check wont pass anyways,
                    // so technically I dont need to check the enchantments at all (technically I dont even need to check the weapon, do I? I'll keep that just in case, though)

                    if (lastDamageSource != null && lastDamageSource.getWeaponItem() != null && lastDamageSource.getWeaponItem().getItem() instanceof CrossbowItem) {

                        int currentTicksExisted = target.tickCount;
                        if (Math.abs(currentTicksExisted - target.getLastHurtByMobTimestamp()) < 4) {

                            event.setAmount((float) (event.getAmount() * MULTISHOT_EXTRA_ARROW_DAMAGE_MULT.get()));
                        }

                    }


                }

            }


        }
    }


    // Don't enable if everything is disabled/at its default, so a user can still disable this event if its messing with another mod
    @Override
    public boolean shouldEnable() {
        return
                !BOW_DAMAGE_MULT.get().equals(BOW_DAMAGE_MULT.getDefault())
                || !MULTISHOT_EXTRA_ARROW_DAMAGE_MULT.get().equals(MULTISHOT_EXTRA_ARROW_DAMAGE_MULT.getDefault())
                || MULTISHOT_NO_IFRAMES.get();
    }

    @Override
    public void enable(IEventBus bus, IConfiguredEventHandler instance) {
        NeoForge.EVENT_BUS.register(instance);
    }

    @Override
    public void disable(IEventBus bus, IConfiguredEventHandler instance) {
        NeoForge.EVENT_BUS.unregister(instance);
    }
}

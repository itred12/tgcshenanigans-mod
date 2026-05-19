package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.data.TGCSAttachments;
import com.itred.tgcshenanigans.particle.TGCSParticles;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BluntCleaverItem extends SwordItem {

    private static final int RATIO_TRIGGER_POINT_DEGREES = 40;
    private static final int RATIO_TRIGGER_LENIENCY = 4;


    public BluntCleaverItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {


        if (usedHand == InteractionHand.MAIN_HAND) {
            player.setData(TGCSAttachments.NANAMI_CROSSHAIR_STARTANGLE, Mth.wrapDegrees(player.getYRot()));
            player.setData(TGCSAttachments.NANAMI_CROSSHAIR_ATTACHMENT, true);

        }
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Player player && canTriggerRatio(player)) {

        }

        entity.setData(TGCSAttachments.NANAMI_CROSSHAIR_ATTACHMENT, false);
        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public float getAttackDamageBonus(@NotNull Entity target, float damage, DamageSource damageSource) {

        Entity self = damageSource.getEntity();

        if (self instanceof Player player && canTriggerRatio(player)) {

            if (!player.level().isClientSide) {

                Vec3 angle = player.getLookAngle();
                float distance = player.distanceTo(target);

                Vec3 targetDist = player.getEyePosition().add(angle.scale((double) distance - 0.5));

                ThisGCsShenanigans.LOGGER.info(String.valueOf(targetDist));

                ((ServerLevel) player.level()).sendParticles(
                        TGCSParticles.RATIO_PARTICLE.get(),
                        targetDist.x,
                        targetDist.y,
                        targetDist.z,
                        1,
                        0,
                        0,
                        0,
                        3
                        );

                ThisGCsShenanigans.LOGGER.info("L +");
                // this is a bonus, so returning damage means it deals double
                return damage;
            }

        }
        return 0;
    }

    /// Gets the amount of rotation, positive or negative, the player currently has away from the ratio ability's starting point
    public static float getRatioOffset(Player player) {
        if (!player.getData(TGCSAttachments.NANAMI_CROSSHAIR_ATTACHMENT)) {
            return 0;
        }

        float storedRotation = player.getData(TGCSAttachments.NANAMI_CROSSHAIR_STARTANGLE);
        float currentRotation = Mth.wrapDegrees(player.getYRot());

        // The difference between the two rotation is "how far away, in degrees, have they rotated from the starting point"
        // Starting smack in the middle, at 0 degrees of difference as our 50%, they'd have to rotate 20% to the left or right to reach 30% or 70%
        // To make things easy on the code rendering the crosshair on the client, this is just "some multiple of 20 degrees"
        return Mth.wrapDegrees(storedRotation - currentRotation);
    }

    /// Returns true if the player's rotation lines up in a way that they can use this item's ratio strike, and false if not
    public static boolean canTriggerRatio(Player player) {
        // If the player isnt currently in Ratio state, then they obviously cant use it
        if (!player.getData(TGCSAttachments.NANAMI_CROSSHAIR_ATTACHMENT)) {
            return false;
        }

        float difference = getRatioOffset(player);

        // If it's greater than the minimum window and less than the maximum window, we have a winner
        return Math.abs(difference) > RATIO_TRIGGER_POINT_DEGREES - RATIO_TRIGGER_LENIENCY
                && Math.abs(difference) < RATIO_TRIGGER_POINT_DEGREES + RATIO_TRIGGER_LENIENCY;
    }


}

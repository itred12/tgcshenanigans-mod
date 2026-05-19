package com.itred.tgcshenanigans.item;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.data.TGCSAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BluntCleaverItem extends SwordItem {

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

            ThisGCsShenanigans.LOGGER.info(String.valueOf(player.getYRot()));
            player.setData(TGCSAttachments.NANAMI_CROSSHAIR_STARTANGLE, player.getYRot());
            player.setData(TGCSAttachments.NANAMI_CROSSHAIR_ATTACHMENT, true);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        entity.setData(TGCSAttachments.NANAMI_CROSSHAIR_ATTACHMENT, false);
        return super.onEntitySwing(stack, entity, hand);
    }
}

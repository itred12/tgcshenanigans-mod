package com.itred.tgcshenanigans.mixin;

import com.iafenvoy.origins.attachment.OriginDataHolder;
import com.iafenvoy.origins.data.power.builtin.regular.TogglePower;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;", ordinal = 0))
    private void modifyAirFriction(Vec3 travelVector, CallbackInfo ci, @Local(name = "d0") LocalDoubleRef d0, @Local(name = "flag") boolean flag) {

        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player) {

            OriginDataHolder data = OriginDataHolder.get(entity);

            if (data.hasActivePower(ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "dove/dove_winged_toggle"), TogglePower.class)) {
                // This flag is what checks to make sure the player is falling before reducing gravity, by simply checking if their Y velocity is <= 0.
                // This way only falling speed is affected, and the player doesnt have reduced gravity on the way up too
                // (Which is an issue this origin normally has)

                // Interestingly, this was as slow as I could push the falling before floating-point (double-point?) imprecision
                // got into the mix and made it so?? I couldn't stand on things at all????
                // For lack of a consistent solution I just made the slow falling twice as powerful instead of 10x.
                if (flag) {
                    d0.set(Math.min(d0.get(), 0.003));
                }




            }

        }


    }


}

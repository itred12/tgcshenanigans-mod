package com.itred.tgcshenanigans.mixin;

import com.itred.tgcshenanigans.Config;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Axolotl.class)
public class AxolotlMixin {

    @Inject(method = "useRareVariant", at = @At("HEAD"), cancellable = true)
    private static void tgcs$modifyOdds(RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (Config.BLUE_AXOLOTL_SPAWNCHANCE_OFFSPRING.get() != 1200) {
            cir.setReturnValue(random.nextInt(Config.BLUE_AXOLOTL_SPAWNCHANCE_OFFSPRING.get()) == 0);
        }


    }


}

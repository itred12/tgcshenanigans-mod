package com.itred.tgcshenanigans.mixin;

import com.itred.tgcshenanigans.Config;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Axolotl.Variant.class)
public abstract class AxolotlVariantMixin {


    @Shadow
    private static Axolotl.Variant getSpawnVariant(RandomSource random, boolean common) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    // This is... probably super reckless.
    @Inject(method = "getCommonSpawnVariant", at = @At(value = "HEAD"), cancellable = true)
    private static void tgcs$spawnRareAxolotlNaturally(RandomSource random, CallbackInfoReturnable<Axolotl.Variant> cir) {
        if (
                Config.ALLOW_BLUE_AXOLOTLS_SPAWN_NATURALLY.get() &&
                random.nextInt(Config.BLUE_AXOLOTL_SPAWNCHANCE.get()) == 0) {
            cir.setReturnValue(getSpawnVariant(random, false));
        }
    }




    // TODO: config opt for allowing blue axolotls to spawn naturally

}

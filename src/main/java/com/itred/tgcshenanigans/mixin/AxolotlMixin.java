package com.itred.tgcshenanigans.mixin;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(Axolotl.class)
public abstract class AxolotlMixin {

    @Shadow
    public abstract Axolotl.Variant getVariant();

    @Inject(method = "setVariant(Lnet/minecraft/world/entity/animal/axolotl/Axolotl$Variant;)V", at = @At("TAIL"))
    private void tgcs$playSoundIfBlueAxolotl(Axolotl.Variant variant, CallbackInfo ci) {

        if (variant == Axolotl.Variant.BLUE) {

            Axolotl axolotl = (Axolotl) (Object) this;

            // If a blue Axolotl spawns, and no player is around to hear it... Did it really spawn?
            ThisGCsShenanigans.LOGGER.info(String.valueOf(axolotl.isAddedToLevel()));

            List<Entity> nearbyPlayers = axolotl.level().getEntities(
                    axolotl,
                    AABB.ofSize(
                            axolotl.blockPosition().getCenter(),
                            16, 16, 16
                    )
            );



            ThisGCsShenanigans.LOGGER.info(nearbyPlayers.toString());

        }

    }


    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void tgcs$detectWhenAxolotlSpawns(CompoundTag compound, CallbackInfo ci) {

        Axolotl axolotl = (Axolotl) (Object) this;
    }


    // TODO: config opt for allowing blue axolotls to spawn naturally

}

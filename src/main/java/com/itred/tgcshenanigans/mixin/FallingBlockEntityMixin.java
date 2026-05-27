package com.itred.tgcshenanigans.mixin;


import com.itred.tgcshenanigans.item.AnvilCrushingRecipe;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {


    // I was at my house eating golden carrot when note block rang
    // "Item is kil"
    // "no"
    @Inject(method = "causeFallDamage", at = @At(value = "RETURN", ordinal = 2))
    private void onAnvilFallAndHurt(float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {

        FallingBlockEntity fallingBlock = (FallingBlockEntity) (Object) this;
        Predicate<Entity> itemEntityPredicate = EntitySelector.ENTITY_STILL_ALIVE.and(entity -> entity instanceof ItemEntity);

        fallingBlock.level().getEntities(fallingBlock, fallingBlock.getBoundingBox(), itemEntityPredicate).forEach(
                entity -> AnvilCrushingRecipe.processAndDestroy((ItemEntity) entity));

    }
}

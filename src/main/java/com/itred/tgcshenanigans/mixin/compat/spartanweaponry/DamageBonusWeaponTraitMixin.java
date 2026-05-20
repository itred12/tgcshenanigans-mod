package com.itred.tgcshenanigans.mixin.compat.spartanweaponry;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.xiyu.spartanweaponryunofficial.api.trait.DamageBonusWeaponTrait;

import java.util.List;

@Pseudo
@Mixin(DamageBonusWeaponTrait.class)
public class DamageBonusWeaponTraitMixin {

    // 47.23 damage from a smite 5 diamond rapier
    // Because these bonuses apply *after* enchantments and not before, whoops
    // Trying to fix that here

    @ModifyVariable(
            method = "modifyDamageDealt",
            at = @At("STORE"),
            name = "bonusDamage"
    )
    private float tgcshenanigans$tryFixDamageMultiplier(float bonusDamage, @Local(name = "attacker") LivingEntity attacker) {
        ItemStack weaponStack = attacker.getWeaponItem();
        if (!weaponStack.isEmpty()) {
            List<ItemAttributeModifiers.Entry> modifiers = weaponStack.getItem().getDefaultInstance().getAttributeModifiers().modifiers();

            for (ItemAttributeModifiers.Entry modifier : modifiers) {

                if (modifier.matches(Attributes.ATTACK_DAMAGE, SwordItem.BASE_ATTACK_DAMAGE_ID)) {

                    // +1 to account for player's innate unarmed attack damage, which *is* taken into account in tooltips
                    float actualBaseDamage = (float) modifier.modifier().amount() + 1;
                    DamageBonusWeaponTrait trait = (DamageBonusWeaponTrait) (Object) this;

                    return (trait.getMagnitude() - 1.0F) * actualBaseDamage;
                }

            }
        }

        return bonusDamage;


    }

}

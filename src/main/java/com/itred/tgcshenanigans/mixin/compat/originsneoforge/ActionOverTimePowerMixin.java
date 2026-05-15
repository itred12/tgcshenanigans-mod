package com.itred.tgcshenanigans.mixin.compat.originsneoforge;

import com.iafenvoy.origins.data.action.EntityAction;
import com.iafenvoy.origins.data.power.IntervalPower;
import com.iafenvoy.origins.data.power.builtin.action.ActionOverTimePower;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

@Pseudo
@Mixin(ActionOverTimePower.class)
public abstract class ActionOverTimePowerMixin extends IntervalPower {

    @Shadow
    @Final
    private EntityAction entityAction;

    @Shadow
    private boolean lastValue;

    @Shadow
    @Final
    private EntityAction risingAction;

    @Shadow
    @Final
    private EntityAction fallingAction;

    protected ActionOverTimePowerMixin(BaseSettings settings) {
        super(settings);
    }

    /**
     * @author itred12
     * @reason EntityAction is executed regardless of whether the condition is fulfilled or not??
     * I figured moving it down in this way would be the easiest way to do it without further shenanigans...
     */
    @Overwrite
    public void intervalTick(@NotNull Entity entity) {

        boolean value = this.getSettings().condition().test(entity);
        if (value) {
            this.entityAction.execute(entity);
        }

        if (value ^ this.lastValue) {
            this.lastValue = value;
            (value ? this.risingAction : this.fallingAction).execute(entity);
        }

    }

}

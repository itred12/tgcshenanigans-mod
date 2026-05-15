package com.itred.tgcshenanigans.compat.originsneoforge.power;

import com.iafenvoy.origins.attachment.OriginDataHolder;
import com.iafenvoy.origins.data.action.EntityAction;
import com.iafenvoy.origins.data.power.IntervalPower;
import com.iafenvoy.origins.data.power.Power;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class JaggedHoverPower extends IntervalPower {

    public static final MapCodec<JaggedHoverPower> CODEC = RecordCodecBuilder.mapCodec(
            (i) -> i.group(
                    BaseSettings.CODEC.forGetter(Power::getSettings),
                    Codec.INT.optionalFieldOf("interval", 20).forGetter(JaggedHoverPower::getInterval),
                    EntityAction.optionalCodec("entity_action").forGetter(JaggedHoverPower::getEntityAction)
            ).apply(i, JaggedHoverPower::new)
    );

    private final int interval;
    private final EntityAction entityAction;

    private boolean lastValue;
    private double startHeight;

    public JaggedHoverPower(Power.BaseSettings settings, int interval, EntityAction entityAction) {
        super(settings);
        this.interval = interval;
        this.entityAction = entityAction;
    }

    @Override
    public int getInterval() {
        return this.interval;
    }

    @Override
    public void intervalTick(@NotNull Entity entity) {

        boolean value = this.getSettings().condition().test(entity);

        if (value ^ this.lastValue) {
            this.lastValue = value;
            if (value) {
                this.startHeight = entity.getY();

            }
        }

        if (value) {

            if (entity instanceof LivingEntity livingEntity) {

                ThisGCsShenanigans.LOGGER.info(String.valueOf(this.startHeight));
                ThisGCsShenanigans.LOGGER.info(String.valueOf(Math.abs(this.startHeight - livingEntity.getY())));
                ThisGCsShenanigans.LOGGER.info(String.valueOf(livingEntity.getX()));

                if (livingEntity.getY() <= this.startHeight - 1) {


                    // Todo: modify gravity as well to get desired results?? fenagle around with the mixin for a while...
                    livingEntity.setDeltaMovement(
                            new Vec3(
                                    livingEntity.getDeltaMovement().x,
                                    Math.abs(this.startHeight - livingEntity.getY()) / 2,
                                    livingEntity.getDeltaMovement().z
                                    )
                    );






                }

            }

        }




    }

    public EntityAction getEntityAction() {
        return this.entityAction;
    }

    @Override
    public @NotNull MapCodec<? extends Power> codec() {
        return CODEC;
    }

    @Override
    public boolean isActive(OriginDataHolder holder) {
        return true;
    }
}

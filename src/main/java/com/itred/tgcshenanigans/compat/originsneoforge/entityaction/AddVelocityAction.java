package com.itred.tgcshenanigans.compat.originsneoforge.entityaction;


import com.iafenvoy.origins.data.action.EntityAction;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Locale;
import java.util.function.BiFunction;

// Mostly copied from the bi-entity add velocity action. I have *no* idea why there isnt an entity one by default.
public record AddVelocityAction(float x, float y, float z, AddVelocityAction.Reference reference, boolean client, boolean server, boolean set) implements EntityAction {
    public static final MapCodec<AddVelocityAction> CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(
            Codec.FLOAT.optionalFieldOf("x", 0.0F).forGetter(AddVelocityAction::x),
            Codec.FLOAT.optionalFieldOf("y", 0.0F).forGetter(AddVelocityAction::y),
            Codec.FLOAT.optionalFieldOf("z", 0.0F).forGetter(AddVelocityAction::z),
            AddVelocityAction.Reference.CODEC.optionalFieldOf("reference", AddVelocityAction.Reference.POSITION).forGetter(AddVelocityAction::reference),
            Codec.BOOL.optionalFieldOf("client", true).forGetter(AddVelocityAction::client),
            Codec.BOOL.optionalFieldOf("server", true).forGetter(AddVelocityAction::server),
            Codec.BOOL.optionalFieldOf("set", false).forGetter(AddVelocityAction::set))
            .apply(i, AddVelocityAction::new));

    public @NotNull MapCodec<? extends EntityAction> codec() {
        return CODEC;
    }

    public void execute(@NotNull Entity user) {
        boolean isClient = user.level().isClientSide;
        if ((!isClient || this.client) && (isClient || this.server)) {
            Vector3f velocity = new Vector3f(this.x, this.y, this.z);
            if (this.set) {
                user.setDeltaMovement(new Vec3(velocity));
            } else {
                user.addDeltaMovement(new Vec3(velocity));
            }
        }
    }

    public enum Reference implements StringRepresentable {
        POSITION((actor, target) -> target.position().subtract(actor.position())),
        ROTATION((actor, target) -> {
            float pitch = actor.getXRot();
            float yaw = actor.getYRot();
            float i = ((float)Math.PI / 180F);
            float j = -Mth.sin(yaw * i) * Mth.cos(pitch * i);
            float k = -Mth.sin(pitch * i);
            float l = Mth.cos(yaw * i) * Mth.cos(pitch * i);
            return new Vec3((double)j, (double)k, (double)l);
        });

        public static final Codec<AddVelocityAction.Reference> CODEC = StringRepresentable.fromEnum(AddVelocityAction.Reference::values);
        final BiFunction<Entity, Entity, Vec3> refFunction;

        Reference(BiFunction<Entity, Entity, Vec3> refFunction) {
            this.refFunction = refFunction;
        }

        public Vec3 apply(Entity actor, Entity target) {
            return (Vec3)this.refFunction.apply(actor, target);
        }

        public @NotNull String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
package com.itred.tgcshenanigans.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RatioParticle extends TextureSheetParticle {

    protected RatioParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 0.0f;
        this.lifetime = 40;
        this.setSpriteFromAge(spriteSet);
        this.quadSize = 1.5f;
        this.gravity = 0.1f;

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.roll = 1.1f;
        this.oRoll = 1f;

    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }


        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new RatioParticle(level, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}

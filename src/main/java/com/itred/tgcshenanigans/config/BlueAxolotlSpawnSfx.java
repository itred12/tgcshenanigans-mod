package com.itred.tgcshenanigans.config;

import com.itred.tgcshenanigans.item.CrystallineDiscItem;
import com.itred.tgcshenanigans.sound.TGCSSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public enum BlueAxolotlSpawnSfx {

    NONE(0, null),
    BW(1, TGCSSounds.BLUEAXOLOTL_BW),
    PLA(2, TGCSSounds.BLUEAXOLOTL_PLA);

    public static final IntFunction<BlueAxolotlSpawnSfx> BY_ID = ByIdMap.continuous((component) -> component.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    private final int id;
    private final Supplier<SoundEvent> soundEffect;

    BlueAxolotlSpawnSfx(int id, @Nullable Supplier<SoundEvent> soundEvent) {
        this.id = id;
        this.soundEffect = soundEvent;
    }

    public int getId() {
        return this.id;
    }

    public Optional<Supplier<SoundEvent>> getSoundEffect() {
        if (this.soundEffect != null) {
            return Optional.of(this.soundEffect);
        } else  {
            return Optional.empty();
        }
    }

}

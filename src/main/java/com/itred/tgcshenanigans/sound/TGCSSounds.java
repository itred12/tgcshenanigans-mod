package com.itred.tgcshenanigans.sound;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TGCSSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS_REGISTRY = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ThisGCsShenanigans.MODID);

    public static final Supplier<SoundEvent> BLUEAXOLOTL_PLA = registerLocalSoundEvent("sound.blueaxolotl_pla");
    public static final Supplier<SoundEvent> BLUEAXOLOTL_BW = registerLocalSoundEvent("sound.blueaxolotl_bw");

    public static final Supplier<SoundEvent> MUSIC_DISC_AIZO = registerLocalSoundEvent("sound.disc.aizo");
    public static final ResourceKey<JukeboxSong> MUSIC_DISC_AIZO_KEY = createSong("aizo");


    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name));
    }

    // Sound events registered this way tesselate as you move away from the source.
    // Anything non-game-music should use this.
    private static Supplier<SoundEvent> registerLocalSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name);
        return SOUND_EVENTS_REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerAll(IEventBus bus) {
        SOUND_EVENTS_REGISTRY.register(bus);
    }
}

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

    public static final Supplier<SoundEvent> BLUEAXOLOTL_PLA = registerLocalSoundEvent("sound.effect.blueaxolotl_pla");
    public static final Supplier<SoundEvent> BLUEAXOLOTL_BW = registerLocalSoundEvent("sound.effect.blueaxolotl_bw");

    public static final Supplier<SoundEvent> MUSIC_DISC_AIZO = registerLocalSoundEvent("sound.disc.kinggnu_aizo");
    public static final Supplier<SoundEvent> MUSIC_DISC_FIREPLACE = registerLocalSoundEvent("sound.disc.tobyfox_fireplace");
    public static final Supplier<SoundEvent> MUSIC_DISC_ICYSANCTUM = registerLocalSoundEvent("sound.disc.drazorleaf_icysanctum");
    public static final Supplier<SoundEvent> MUSIC_DISC_CATSWING = registerLocalSoundEvent("sound.disc.tobyfox_catswing");
    public static final Supplier<SoundEvent> MUSIC_DISC_FROMNOWON = registerLocalSoundEvent("sound.disc.tobyfox_from_now_on");

    public static final ResourceKey<JukeboxSong> MUSIC_DISC_AIZO_KEY = createSong("aizo");
    public static final ResourceKey<JukeboxSong> MUSIC_DISC_FIREPLACE_KEY = createSong("fireplace");
    public static final ResourceKey<JukeboxSong> MUSIC_DISC_ICYSANCTUM_KEY = createSong("icysanctum");
    public static final ResourceKey<JukeboxSong> MUSIC_DISC_CATSWING_KEY = createSong("catswing");
    public static final ResourceKey<JukeboxSong> MUSIC_DISC_FROMNOWON_KEY = createSong("fromnowon");







    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name));
    }

    // Sound events registered this way tesselate as you move away from the source (as long as they're encoded in mono!).
    // Anything non-game-music should use this.
    private static Supplier<SoundEvent> registerLocalSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, name);
        return SOUND_EVENTS_REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerAll(IEventBus bus) {
        SOUND_EVENTS_REGISTRY.register(bus);
    }
}

package com.itred.tgcshenanigans.datagen.registry;

import com.itred.tgcshenanigans.item.TGCSItems;
import com.itred.tgcshenanigans.sound.TGCSSounds;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

// Just kinda winging it here, not sure if this is possible but it'd definitely be nice??
public class TGCSJukeboxSongRegistryProvider {

    public static final String SONG_TRANSLATION_SUFFIX = "credit_splash";

    public static final JukeboxSong JUKEBOX_SONG_AIZO = newJukeboxSong(TGCSSounds.MUSIC_DISC_AIZO, TGCSItems.DISC_AIZO,
            215.0f,
            15
    );

    public static final JukeboxSong JUKEBOX_SONG_FIREPLACE = newJukeboxSong(TGCSSounds.MUSIC_DISC_FIREPLACE, TGCSItems.DISC_FIREPLACE,
            158.0f,
            15
    );

    public static final JukeboxSong JUKEBOX_SONG_CATSWING = newJukeboxSong(TGCSSounds.MUSIC_DISC_CATSWING, TGCSItems.DISC_CATSWING,
            121.0f,
            15
    );

    public static final JukeboxSong JUKEBOX_SONG_FROMNOWON = newJukeboxSong(TGCSSounds.MUSIC_DISC_FROMNOWON, TGCSItems.DISC_FROMNOWON,
            109.0f,
            15
    );

    public static final JukeboxSong JUKEBOX_SONG_DEATHODYSSEY = newJukeboxSong(TGCSSounds.MUSIC_DISC_DEATHODYSSEY, TGCSItems.DISC_DEATHODYSSEY,
            510.0f,
            15
    );

    public static final JukeboxSong JUKEBOX_SONG_DAUGHTEROFHALLOWNEST = newJukeboxSong(TGCSSounds.MUSIC_DISC_DAUGHTEROFHALLOWNEST, TGCSItems.DISC_DAUGHTEROFHALLOWNEST,
            172.0f,
            15
    );

    public static final JukeboxSong JUKEBOX_SONG_REMEMBER = newJukeboxSong(TGCSSounds.MUSIC_DISC_REMEMBER, TGCSItems.DISC_REMEMBER,
            243.0f,
            15
    );

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {

        context.register(TGCSSounds.MUSIC_DISC_AIZO_KEY, JUKEBOX_SONG_AIZO);
        context.register(TGCSSounds.MUSIC_DISC_FIREPLACE_KEY, JUKEBOX_SONG_FIREPLACE);
        context.register(TGCSSounds.MUSIC_DISC_CATSWING_KEY, JUKEBOX_SONG_CATSWING);
        context.register(TGCSSounds.MUSIC_DISC_FROMNOWON_KEY, JUKEBOX_SONG_FROMNOWON);
        context.register(TGCSSounds.MUSIC_DISC_DEATHODYSSEY_KEY, JUKEBOX_SONG_DEATHODYSSEY);
        context.register(TGCSSounds.MUSIC_DISC_DAUGHTEROFHALLOWNEST_KEY, JUKEBOX_SONG_DAUGHTEROFHALLOWNEST);
        context.register(TGCSSounds.MUSIC_DISC_REMEMBER_KEY, JUKEBOX_SONG_REMEMBER);
    }


    public static JukeboxSong newJukeboxSong(DeferredHolder<SoundEvent, ? extends SoundEvent> soundEvent, DeferredItem<?> songPlayerItem, float lengthInSeconds, int comparatorOutput) {
        return new JukeboxSong(soundEvent, Component.translatable("item." + songPlayerItem.getId().toLanguageKey() + "." + SONG_TRANSLATION_SUFFIX), lengthInSeconds, comparatorOutput);
    }
}

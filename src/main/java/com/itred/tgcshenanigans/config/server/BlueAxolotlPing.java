package com.itred.tgcshenanigans.config.server;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.config.IConfiguredEventHandler;
import com.itred.tgcshenanigans.sound.TGCSSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class BlueAxolotlPing implements IConfiguredEventHandler {

    private static final List<Axolotl> storedAxolotls = new ArrayList<>();
    private static int COUNTER = 0;

    @SubscribeEvent
    private void onEntityTracked(PlayerEvent.StartTracking event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();

        if (player.level().isClientSide() && entity instanceof Axolotl) {
            storedAxolotls.add((Axolotl) entity);
        }

    }

    @SubscribeEvent
    private void onEntityStopTracking(PlayerEvent.StopTracking event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();

        if (player.level().isClientSide() && entity instanceof Axolotl) {
            storedAxolotls.remove(entity);
        }
    }

    @SubscribeEvent
    private void onPlayerTick(PlayerTickEvent.Pre event) {

        Player player = event.getEntity();
        // Honestly I'd rather not hook up this event at all sever-side, buuut I'm not sure if that's how it works.
        if (!player.level().isClientSide()) {
            return;
        }

        COUNTER++;

        if (COUNTER > 10) {

            BlueAxolotlSpawnSfx blueAxolotlPing = Config.BLUE_AXOLOTL_PING_SOUND.get();

            if (blueAxolotlPing == BlueAxolotlSpawnSfx.NONE || blueAxolotlPing.getSoundEffect().isEmpty()) {
                return;
            }

            COUNTER = 0;

            Level level = player.level();


            // Perform operations on a copy of the table, since using an iterator could cause a one-in-a-million Access Violation exception
            // when de-loading an axolotl the instant we start looping through the list.
            List<Axolotl> clonedStoredAxolotls = List.copyOf(storedAxolotls);

            // TODO: Particle effect, server config opt, maybe increase the range

            for (Axolotl axolotl : clonedStoredAxolotls) {

                if (axolotl.isAddedToLevel() && (axolotl.getVariant() != Axolotl.Variant.BLUE)) {
                    storedAxolotls.remove(axolotl);
                    continue;
                }

                if (axolotl.distanceTo(player) <= 64) {
                    axolotl.playSound(blueAxolotlPing.getSoundEffect().get().get(), 5f, ((float) player.getRandom().nextInt(95, 105)) / 100);
                    ThisGCsShenanigans.LOGGER.info("Blue axolotl spawned!");
                    storedAxolotls.remove(axolotl);
                }


            }


        }


    }

    @Override
    public boolean shouldEnable() {
        return Config.ENABLE_BLUE_AXOLOTL_PING.get();

    }

    @Override
    public void enable(IEventBus bus, IConfiguredEventHandler instance) {
        NeoForge.EVENT_BUS.register(instance);
    }

    @Override
    public void disable(IEventBus bus, IConfiguredEventHandler instance) {
        NeoForge.EVENT_BUS.unregister(instance);
    }

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
}

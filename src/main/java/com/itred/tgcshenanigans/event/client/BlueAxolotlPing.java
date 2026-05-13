package com.itred.tgcshenanigans.event.client;

import com.itred.tgcshenanigans.Config;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.config.BlueAxolotlSpawnSfx;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.List;

public class BlueAxolotlPing {

    private static final List<Axolotl> storedAxolotls = new ArrayList<>();
    private static int COUNTER = 0;

    @SubscribeEvent
    static void onEntityTracked(PlayerEvent.StartTracking event) {
        Entity entity = event.getTarget();

        if (entity instanceof Axolotl) {
            storedAxolotls.add((Axolotl) entity);
        }

    }

    @SubscribeEvent
    static void onEntityStopTracking(PlayerEvent.StopTracking event) {
        Entity entity = event.getTarget();

        if (entity instanceof Axolotl) {
            storedAxolotls.remove(entity);
        }
    }

    @SubscribeEvent
    static void onPlayerTick(PlayerTickEvent.Pre event) {
        COUNTER++;

        if (COUNTER > 10) {

            BlueAxolotlSpawnSfx blueAxolotlPing = Config.BLUE_AXOLOTL_PING_SOUND.get();

            if (blueAxolotlPing == BlueAxolotlSpawnSfx.NONE || blueAxolotlPing.getSoundEffect().isEmpty()) {
                return;
            }

            COUNTER = 0;

            Player player = event.getEntity();
            Level level = player.level();

            if (!level.isClientSide()) {
                return;
            }

            // Perform operations on a copy of the table, since using an iterator could cause a one-in-a-million Access Violation exception
            // when de-loading an axolotl the instant we start looping through the list.
            List<Axolotl> clonedStoredAxolotls = List.copyOf(storedAxolotls);

            // TODO: Particle effect, server config opt, maybe increase the range

            for (Axolotl axolotl : clonedStoredAxolotls) {

                if (axolotl.isAddedToLevel() && (axolotl.getVariant() != Axolotl.Variant.BLUE)) {
                    storedAxolotls.remove(axolotl);
                    continue;
                }

                if (axolotl.distanceTo(player) < 64) {
                    axolotl.playSound(blueAxolotlPing.getSoundEffect().get().get(), 5f, ((float) player.getRandom().nextInt(95, 105)) / 100);
                    ThisGCsShenanigans.LOGGER.info("Blue axolotl spawned!");
                    storedAxolotls.remove(axolotl);
                }


            }


        }


    }

}

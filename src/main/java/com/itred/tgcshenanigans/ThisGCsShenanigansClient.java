package com.itred.tgcshenanigans;

import com.itred.tgcshenanigans.sound.TGCSSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ThisGCsShenanigans.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ThisGCsShenanigans.MODID, value = Dist.CLIENT)
public class ThisGCsShenanigansClient {
    public ThisGCsShenanigansClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        ThisGCsShenanigans.LOGGER.info("HELLO FROM CLIENT SETUP");
        ThisGCsShenanigans.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

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
            COUNTER = 0;

            Player player = event.getEntity();
            Level level = player.level();

            if (!level.isClientSide()) {
                return;
            }

            // Perform operations on a copy of the table, since using an iterator could cause a one-in-a-million Access Violation exception
            // when de-loading an axolotl the instant we start looping through the list.
            List<Axolotl> clonedStoredAxolotls = List.copyOf(storedAxolotls);

            for (Axolotl axolotl : clonedStoredAxolotls) {

                if (axolotl.isAddedToLevel() && (axolotl.getVariant() != Axolotl.Variant.BLUE)) {
                    storedAxolotls.remove(axolotl);
                    continue;
                }

                if (axolotl.distanceTo(player) < 16) {
                    axolotl.playSound(TGCSSounds.BLUEAXOLOTL_BW.get());
                    storedAxolotls.remove(axolotl);
                }


            }


        }


    }

}

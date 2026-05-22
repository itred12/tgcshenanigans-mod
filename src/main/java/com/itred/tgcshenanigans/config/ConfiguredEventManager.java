package com.itred.tgcshenanigans.config;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.config.server.BlueAxolotlPing;
import com.itred.tgcshenanigans.config.server.DisableEntity;
import com.itred.tgcshenanigans.config.server.DurabilityRework;
import com.itred.tgcshenanigans.config.startup.ConfigureEntityAttributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.Map;

public class ConfiguredEventManager {

   // What could possibly go wrong?
   private final IEventBus modBus;
   private final Map<ModConfig.Type, List<IConfiguredEventHandler>> configurableEventHandlers;

   // "Bro just typing shi"
   // Seriously what am I doin here, I think its good????
   public ConfiguredEventManager(IEventBus modBus) {
      this.modBus = modBus;

      configureStartupConfig(modBus);

      configurableEventHandlers = Map.of(

              ModConfig.Type.SERVER, List.of(
                      new BlueAxolotlPing(),
                      new DisableEntity(),
                      new DurabilityRework()
              ),

              ModConfig.Type.CLIENT, List.of(),

              ModConfig.Type.COMMON, List.of(),

              ModConfig.Type.STARTUP, List.of(
                      new ConfigureEntityAttributes()
              )

      );

      modBus.addListener(this::onConfigLoad);
      modBus.addListener(this::onConfigUnload);
      modBus.addListener(this::onConfigReload);

   }


   private void configureStartupConfig(IEventBus bus) {

   }

   private void loadConfig(ModConfig config) {

      for (IConfiguredEventHandler eventHandler : configurableEventHandlers.get(config.getType())) {
         ThisGCsShenanigans.LOGGER.info("Querying configurable event handler \"{}\" from config \"{}\"", eventHandler.getClass().getSimpleName(), config.getType());
         if (eventHandler.shouldEnable()) {
            ThisGCsShenanigans.LOGGER.info("Loading event handler \"{}\" from config \"{}\"!", eventHandler.getClass().getSimpleName(), config.getType());
            eventHandler.enable(modBus, eventHandler);
         }
      }

   }

   private void unloadConfig(ModConfig config) {

      for (IConfiguredEventHandler eventHandler : configurableEventHandlers.get(config.getType())) {
         ThisGCsShenanigans.LOGGER.info("Unloading event handler \"{}\" from config \"{}\"...", eventHandler.getClass().getSimpleName(), config.getType());
         eventHandler.disable(modBus, eventHandler);
      }

   }


   private void onConfigLoad(ModConfigEvent.Loading event) {
      ModConfig config = event.getConfig();

      if (config.getModId().equals(ThisGCsShenanigans.MODID)) {
         loadConfig(config);
      }
   }

   private void onConfigUnload(ModConfigEvent.Unloading event) {
      ModConfig config = event.getConfig();

      if (config.getModId().equals(ThisGCsShenanigans.MODID)) {
         unloadConfig(config);
      }

   }

   private void onConfigReload(ModConfigEvent.Reloading event) {
      ModConfig config = event.getConfig();

      if (config.getModId().equals(ThisGCsShenanigans.MODID)) {
         loadConfig(config);
      }
   }




}

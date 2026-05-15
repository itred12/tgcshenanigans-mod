package com.itred.tgcshenanigans.network;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.network.payload.KeyPressC2SPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;

@EventBusSubscriber(modid = ThisGCsShenanigans.MODID)
public class PayloadRegistrar {

    @SubscribeEvent
    public static void registerNetworkPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToServer(KeyPressC2SPayload.TYPE, KeyPressC2SPayload.STREAM_CODEC, new MainThreadPayloadHandler<>(ServerReciever::onKeyPress));
    }
}

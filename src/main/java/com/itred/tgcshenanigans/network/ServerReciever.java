package com.itred.tgcshenanigans.network;

import com.iafenvoy.origins.attachment.OriginDataHolder;
import com.iafenvoy.origins.data.power.component.builtin.ResourceComponent;
import com.iafenvoy.origins.util.math.ResourceOperation;
import com.itred.tgcshenanigans.ThisGCsShenanigans;
import com.itred.tgcshenanigans.network.payload.KeyPressC2SPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerReciever {

    // So far, listens for:
        // The space bar being held, for Dove of Mary origin
        // The space bar being released, for Dove of Mary origin
    public static void onKeyPress(KeyPressC2SPayload payload, IPayloadContext context) {


        Player player = context.player();

        if (payload.key().equals("key.jump")) {

            OriginDataHolder.get(player)
                    .getComponent(ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, "space_held"), ResourceComponent.class)
                    .ifPresent(x -> x.updateResource(ResourceOperation.SET.getOperator(),
                            payload.state() ? 1 : 0));

        }
    }

}

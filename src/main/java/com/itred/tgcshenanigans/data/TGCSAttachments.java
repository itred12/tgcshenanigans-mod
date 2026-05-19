package com.itred.tgcshenanigans.data;

import com.itred.tgcshenanigans.ThisGCsShenanigans;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class TGCSAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ThisGCsShenanigans.MODID);

    public static final Supplier<AttachmentType<Boolean>> NANAMI_CROSSHAIR_ATTACHMENT = ATTACHMENT_REGISTER.register(
            "nanami_crosshair",
            () -> AttachmentType.builder(() -> false)
                    .sync(ByteBufCodecs.BOOL)
                    .build()
    );

    public static final Supplier<AttachmentType<Float>> NANAMI_CROSSHAIR_STARTANGLE = ATTACHMENT_REGISTER.register(
            "nanami_crosshair_startangle",
            () -> AttachmentType.builder(() -> 0f)
                    .sync(ByteBufCodecs.FLOAT)
                    .build()
    );

}

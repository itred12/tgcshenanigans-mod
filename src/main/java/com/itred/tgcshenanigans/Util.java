package com.itred.tgcshenanigans;

import net.minecraft.resources.ResourceLocation;

public class Util {

    public static ResourceLocation modLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(ThisGCsShenanigans.MODID, path);
    }

}

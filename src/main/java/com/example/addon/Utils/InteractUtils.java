package com.example.addon.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class InteractUtils {
    public static void normalHit(Entity target, boolean swing) {
        mc.interactionManager.attackEntity(mc.player, target);
        if (swing) mc.player.swingHand(Hand.MAIN_HAND);
    }

    public static void packetHit(Entity target, boolean swing) {
        mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isPlayer()));
        if (swing) mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
    }
}

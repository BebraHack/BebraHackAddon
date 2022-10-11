package com.example.addon.Utils;

import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class InvHelper {
    public static boolean isInventoryFull() {
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            if (itemStack == null || itemStack.getItem() instanceof AirBlockItem) return false;
        }
        return true;
    }

    public static void syncSlot() {
        mc.player.getInventory().selectedSlot = mc.player.getInventory().selectedSlot;
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
    }

    public static int getBlank() {
        int index = -1;
        for(int i = 0; i < 45; i++) {
            if(mc.player.getInventory().getStack(i).isEmpty()) {
                index = i;
                break;
            }
        }
        return index;
    }
}

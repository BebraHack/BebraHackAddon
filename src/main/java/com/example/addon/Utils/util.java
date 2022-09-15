package com.example.addon.Utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class util {
    public static ItemStack getArmor(int slot) {return mc.player.getInventory().armor.get(slot);}
    public static Item getArmorItem(int slot) {return getArmor(slot).getItem();}
    public static boolean isInElytra() {return getArmorItem(2) == Items.ELYTRA && mc.player.isFallFlying();}
}

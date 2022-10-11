package com.example.addon.modules.misc;

import com.example.addon.Addon;
import com.example.addon.Utils.ArmorUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

//SilentItem by ranele (copper hack addon) https://discord.gg/tqRPu6Pudg

public class SilentItem extends Module {
    public SilentItem() {
        super(Addon.misc, "silent-item", " ");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Item> itemUse = sgGeneral.add(new EnumSetting.Builder<Item>().name("item").defaultValue(Item.Rocket).build());
    private final Setting<Boolean> notify = sgGeneral.add(new BoolSetting.Builder().name("notify").defaultValue(true).build());

    private int mainSlot;
    private boolean invMove;

    @Override
    public void onActivate(){
        invMove = false;
        mainSlot = mc.player.getInventory().selectedSlot;
    }

    @Override
    public void onDeactivate(){
        invMove = false;
        mainSlot = -1;
    }

    @EventHandler
    private void onTickEvent(TickEvent.Pre event) {
        FindItemResult fP = InvUtils.find(Items.ENDER_PEARL);
        if (!fP.found() && itemUse.get() == Item.Pearl) toggle();
        FindItemResult fR = InvUtils.find(Items.FIREWORK_ROCKET);
        if (!fR.found() && itemUse.get() == Item.Rocket) toggle();
        if (itemUse.get() == Item.Pearl) {
            FindItemResult pearl = InvUtils.find(Items.ENDER_PEARL);
            int pearlSlot = pearl.slot();
            if (!pearl.found() && notify.get()) {
                info("No pearl found!");
                return;
            }
            InvUtils.move().from(pearlSlot).to(mainSlot);
            if (mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                invMove = true;
            }
            if (invMove) {
                InvUtils.move().from(mainSlot).to(pearlSlot);
            }
            toggle();
        }
        if (itemUse.get() == Item.Rocket) {
            if (!ArmorUtils.isFallFlying()) toggle();
            FindItemResult rocket = InvUtils.find(Items.FIREWORK_ROCKET);
            int rocketSlot = rocket.slot();
            if (!rocket.found() && notify.get()) {
                info("No rocket found!");
                toggle();
                return;
            }
            if (!rocket.found()) toggle();
            InvUtils.move().from(rocketSlot).to(mainSlot);
            if (mc.player.getMainHandStack().getItem() == Items.FIREWORK_ROCKET) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                invMove = true;
            }
            if (invMove) {
                InvUtils.move().from(mainSlot).to(rocketSlot);
            }
            toggle();
        }
    }

    public enum Item {
        Pearl,
        Rocket;
    }
}

package com.example.addon.modules.misc;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;

public class Spinner extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Integer> b = sgGeneral.add(new IntSetting.Builder().name("speed").description("The speed at which you rotate.").defaultValue(5).min(0).sliderMax(100).build());
    private final Setting<Boolean> c = sgGeneral.add(new BoolSetting.Builder().name("pauseItems").description("Stop rotate when you use some pvp items.").defaultValue(true).build());

    private short a = 0;

    public Spinner() {
        super(Addon.misc, "spinner", "very very very very very fast spin / max speed 180");
    }

    @EventHandler
    public void onTick(TickEvent.Post post) {
        if (!c.get() || mc.player.getMainHandStack().getItem() != Items.EXPERIENCE_BOTTLE && mc.player.getMainHandStack().getItem() != Items.ENDER_PEARL && mc.player.getOffHandStack().getItem() != Items.EXPERIENCE_BOTTLE && mc.player.getOffHandStack().getItem() != Items.ENDER_PEARL && mc.player.getMainHandStack().getItem() != Items.OBSIDIAN && mc.player.getMainHandStack().getItem() != Items.NETHERITE_SWORD && mc.player.getMainHandStack().getItem() != Items.NETHERITE_PICKAXE && mc.player.getMainHandStack().getItem() != Items.END_CRYSTAL && mc.player.getMainHandStack().getItem() != Items.STONE_BUTTON && mc.player.getMainHandStack().getItem() != Items.ANVIL && mc.player.getMainHandStack().getItem() != Items.CHIPPED_ANVIL && mc.player.getMainHandStack().getItem() != Items.DAMAGED_ANVIL && mc.player.getMainHandStack().getItem() != Items.BOW && mc.player.getMainHandStack().getItem() != Items.TNT && mc.player.getMainHandStack().getItem() != Items.FLINT)  {
            a = (short) (a + b.get());
            if (a > 180) {
                a = -180;
            }

            Rotations.rotate(a, 0.0D);
        }
    }
}

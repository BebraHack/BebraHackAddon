package com.example.addon.modules.BebraHackRender;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class TimeAnimator extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    // General
    private final Setting<Integer> speed = sgGeneral.add(new IntSetting.Builder()
        .name("speed")
        .description("How fast the day and night cycle should be.")
        .defaultValue(100)
        .sliderRange(1, 500)
        .build()
    );


    public TimeAnimator() {
        super(Addon.render, "time-animator", "Speeds up the day and night cycle.");
    }


    private long timer;


    @Override
    public void onActivate() {
        mc.world.setTimeOfDay(mc.world.getTime());
        timer = 0;
    }

    @Override
    public void onDeactivate() {
        mc.world.setTimeOfDay(mc.world.getTime());
        timer = 0;
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof WorldTimeUpdateS2CPacket) {
            event.cancel();
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        timer ++;
        mc.world.setTimeOfDay(timer * (speed.get()));

        if (mc.world.getTimeOfDay() >= 24000 || mc.world.getTimeOfDay() <= -24000){
            timer = 0;
            mc.world.setTimeOfDay(0);
        }
    }
}

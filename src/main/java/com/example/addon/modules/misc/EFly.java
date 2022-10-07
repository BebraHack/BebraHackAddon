package com.example.addon.modules.misc;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class EFly extends Module {

    public enum Mode {Boost, Automatic}


    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgAutomatic = settings.createGroup("Automatic");

    //General
    private final Setting<Boolean> takeoff = sgGeneral.add(new BoolSetting.Builder().name("take-off").defaultValue(false).build());
    public final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>().name("mode").defaultValue(Mode.Boost).build());
    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder().name("factor").defaultValue(0.270).min(0.1).sliderMax(4).build());

    //Automatic
    private final Setting<Integer> startAt = sgAutomatic.add(new IntSetting.Builder().name("start-fly-at").description("The Y coordinate to start automatic module. Recommended 70~").defaultValue(75).min(25).sliderMin(25).sliderMax(255).build());
    private final Setting<Integer> targetY = sgAutomatic.add(new IntSetting.Builder().name("target-y").description("The target coordinate for boosting.").defaultValue(200).min(50).sliderMin(50).sliderMax(255).build());
    private final Setting<Integer> boostingTicks = sgAutomatic.add(new IntSetting.Builder().name("boostingTicks").description("Amount of ticks to boost").defaultValue(40).min(20).max(80).sliderMin(20).sliderMax(80).build());
    private final Setting<Boolean> lava = sgGeneral.add(new BoolSetting.Builder().name("place-lava").defaultValue(false).build());
    private final Setting<Integer> del = sgGeneral.add(new IntSetting.Builder().name("delay").description("lava delay").defaultValue(2).min(1).build());
    boolean boosting;
    int boostTicks;
    float prevPitch;
    int delay;

    public EFly() {
        super(Addon.misc, "Elytra Fly", "Elytra Fly that works on ncp servers.");
    }

    @Override
    public void onActivate() {
        boosting = false;
        boostTicks = 0;
        if(lava.get() && mc.player.isOnGround()){
            delay = del.get();
            prevPitch = mc.player.getPitch();
            mc.player.setPitch(90);
            FindItemResult slot = InvUtils.find(Items.LAVA_BUCKET);
            InvUtils.swap(slot.slot(), true);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);

        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {

        if(delay == 0 && lava.get()){
            FindItemResult slot = InvUtils.find(Items.BUCKET);
            InvUtils.swap(slot.slot(), false);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            InvUtils.swapBack();
            mc.player.setPitch(prevPitch);
        }
        delay--;

        float yaw = (float) Math.toRadians(mc.player.getYaw());

        if (!mc.player.isFallFlying()) {
            if (takeoff.get() && !mc.player.isOnGround() && mc.options.jumpKey.isPressed())
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            return;
        }

        if (mc.player.getAbilities().flying) {
            mc.player.getAbilities().flying = false;
        }

        //Boost
        if (mode.get() == Mode.Boost) {
            if (mc.options.forwardKey.isPressed()) {
                mc.player.addVelocity(-MathHelper.sin(yaw) * speed.get() / 10, 0, MathHelper.cos(yaw) * speed.get() / 10);
            }
        }

        if (mode.get() == Mode.Automatic) {
            int y = (int) mc.player.getY();
            int b = boostingTicks.get() / 4;


            if (boosting || y >= startAt.get() && y < targetY.get()) {
                boosting = true;
                boostTicks++;


                if (boostTicks < b * 2) {
                    mc.player.setPitch(35);
                    mc.player.addVelocity(-MathHelper.sin(yaw) * speed.get() / 10, 0, MathHelper.cos(yaw) * speed.get() / 10);
                } else {
                    float i = boostTicks / 1.5f;
                    if (i > 35) i = 35;
                    mc.player.setPitch(-i);
                }

                if (boostTicks > boostingTicks.get() + 10) {
                    boosting = false;
                    boostTicks = 0;
                }
            }
        }
    }
}

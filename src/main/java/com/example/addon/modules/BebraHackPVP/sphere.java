package com.example.addon.modules.BebraHackPVP;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class sphere extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgMisc = settings.createGroup("Misc");



    private final Setting<Boolean> sphere = sgGeneral.add(new BoolSetting.Builder().name("sphere").description("sphere for you").defaultValue(true).build());

    private final Setting<Boolean> checkEntity = sgMisc.add(new BoolSetting.Builder().name("check-entity").description("entity check").defaultValue(true).build());
    private final Setting<Boolean> onlyOnGround = sgMisc.add(new BoolSetting.Builder().name("only-on-ground").description("Works only when you standing on blocks.").defaultValue(false).build());
    private final Setting<Boolean> center = sgMisc.add(new BoolSetting.Builder().name("center").description("Teleports you to the center of the block.").defaultValue(true).build());
    private final Setting<Boolean> disableOnJump = sgMisc.add(new BoolSetting.Builder().name("disable-on-jump").description("Automatically disables when you jump.").defaultValue(false).build());
    private final Setting<Boolean> disableOnYChange = sgMisc.add(new BoolSetting.Builder().name("disable-on-y-change").description("Automatically disables when your y level (step, jumping, atc).").defaultValue(false).build());
    private final Setting<Boolean> rotate = sgMisc.add(new BoolSetting.Builder().name("rotate").description("Automatically faces towards the obsidian being placed.").defaultValue(false).build());
    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("Blocks to search for.")
        .onChanged(blocks1 -> {
            if (isActive() && Utils.canUpdate()) onActivate();
        })
        .build()
    );


    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean return_;

    public sphere() {
        super(Addon.combat, "sphere", "build sphere");
    }

    @Override
    public void onActivate() {
        if (center.get()) PlayerUtils.centerPlayer();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if ((disableOnJump.get() && (mc.options.jumpKey.isPressed() || mc.player.input.jumping)) || (disableOnYChange.get() && mc.player.prevY < mc.player.getY())) {
            toggle();
            return;
        }

        if (onlyOnGround.get() && !mc.player.isOnGround()) return;

        return_ = false;

        place(0, -1, 0);
        if (return_) return;
        place(1, 0, 0);
        if (return_) return;
        place(-1, 0, 0);
        if (return_) return;
        place(0, 0, 1);
        if (return_) return;
        place(0, 0, -1);
        if (return_) return;


        if (sphere.get()) {
            //вниз
            place(0, -1, 0);
            if (return_) return;
            place(1, -1, 0);
            if (return_) return;
            place(-1, -1, 0);
            if (return_) return;
            place(0, -1, 1);
            if (return_) return;
            place(0, -1, -1);
            if (return_) return;
            place(-1, -1, -1);
            if (return_) return;
            place(-1, -1, 1);
            if (return_) return;
            place(1, -1, -1);
            if (return_) return;
            place(1, -1, 1);
            if (return_) return;
            place(1, -2, 0);
            if (return_) return;
            place(-1, -2, 0);
            if (return_) return;
            place(0, -2, 1);
            if (return_) return;
            place(0, -2, -1);
            if (return_) return;
            place(-1, -2, -1);
            if (return_) return;
            place(-1, -2, 1);
            if (return_) return;
            place(1, -2, -1);
            if (return_) return;
            place(1, -2, 1);
            if (return_) return;
            place(0, -2, 0);
            if (return_) return;
            place(1, -3, 0);
            if (return_) return;
            place(-1, -3, 0);
            if (return_) return;
            place(0, -3, 1);
            if (return_) return;
            place(0, -3, -1);
            if (return_) return;
            place(0, -3, 0);
            if (return_) return;
            place(-1, -3, -1);
            if (return_) return;
            place(-1, -3, 1);
            if (return_) return;
            place(1, -3, -1);
            if (return_) return;
            place(1, -3, 1);
            if (return_) return;
            place(1, -4, 0);
            if (return_) return;
            place(-1, -4, 0);
            if (return_) return;
            place(0, -4, 1);
            if (return_) return;
            place(0, -4, -1);
            if (return_) return;
            place(0, -4, 0);
            if (return_) return;
            place(-1, -4, -1);
            if (return_) return;
            place(-1, -4, 1);
            if (return_) return;
            place(1, -4, -1);
            if (return_) return;
            place(1, -4, 1);
            if (return_) return;
            //вверх
            place(1, 1, 0);
            if (return_) return;
            place(-1, 1, 0);
            if (return_) return;
            place(0, 1, 1);
            if (return_) return;
            place(0, 1, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, 1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(1, 0, 1);
            if (return_) return;
            place(-1, 1, -1);
            if (return_) return;
            place(-1, 1, 1);
            if (return_) return;
            place(1, 1, -1);
            if (return_) return;
            place(1, 1, 1);
            if (return_) return;
            place(0, 2, 0);
            if (return_) return;
            place(1, 2, 0);
            if (return_) return;
            place(-1, 2, 0);
            if (return_) return;
            place(0, 2, 1);
            if (return_) return;
            place(0, 2, -1);
            if (return_) return;
            place(-1, 2, -1);
            if (return_) return;
            place(-1, 2, 1);
            if (return_) return;
            place(1, 2, -1);
            if (return_) return;
            place(1, 2, 1);
            if (return_) return;
            place(1, 3, 0);
            if (return_) return;
            place(-1, 3, 0);
            if (return_) return;
            place(0, 3, 1);
            if (return_) return;
            place(0, 3, -1);
            if (return_) return;
            place(0, 3, 0);
            if (return_) return;
            place(-1, 3, -1);
            if (return_) return;
            place(-1, 3, 1);
            if (return_) return;
            place(1, 3, -1);
            if (return_) return;
            place(1, 3, 1);
            if (return_) return;
            place(1, 4, 0);
            if (return_) return;
            place(-1, 4, 0);
            if (return_) return;
            place(0, 4, 1);
            if (return_) return;
            place(0, 4, -1);
            if (return_) return;
            place(0, 4, 0);
            if (return_) return;
            place(-1, 4, -1);
            if (return_) return;
            place(-1, 4, 1);
            if (return_) return;
            place(1, 4, -1);
            if (return_) return;
            place(1, 4, 1);
            if (return_) return;
            place(1, 5, 0);
            if (return_) return;
            place(-1, 5, 0);
            if (return_) return;
            place(0, 5, 1);
            if (return_) return;
            place(0, 5, -1);
            if (return_) return;
            place(0, 5, 0);
            if (return_) return;
            place(-1, 5, -1);
            if (return_) return;
            place(-1, 5, 1);
            if (return_) return;
            place(1, 5, -1);
            if (return_) return;
            place(1, 5, 1);
            if (return_) return;
            place(1, 6, 0);
            if (return_) return;
            place(-1, 6, 0);
            if (return_) return;
            place(0, 6, 1);
            if (return_) return;
            place(0, 6, -1);
            if (return_) return;
            place(0, 6, 0);
            if (return_) return;
            place(-1, 6, -1);
            if (return_) return;
            place(-1, 6, 1);
            if (return_) return;
            place(1, 6, -1);
            if (return_) return;
            place(1, 6, 1);
            if (return_) return;
            place(0, 7, 0);
            if (return_) return;
            place(1, 7, 0);
            if (return_) return;
            place(-1, 7, 0);
            if (return_) return;
            place(0, 7, 1);
            if (return_) return;
            place(0, 7, -1);
            if (return_) return;
            //прямо
            // x2
            place(2, 0, 0);
            if (return_) return;
            place(2, -1, 0);
            if (return_) return;
            place(2, 1, 0);
            if (return_) return;
            place(2, -2, 0);
            if (return_) return;
            place(2, 2, 0);
            if (return_) return;
            place(2, -3, 0);
            if (return_) return;
            place(2, 3, 0);
            if (return_) return;
            place(2, 4, 0);
            if (return_) return;
            place(2, 5, 0);
            if (return_) return;
            place(2, 6, 0);
            if (return_) return;
            //x3
            place(3, 0, 0);
            if (return_) return;
            place(3, -1, 0);
            if (return_) return;
            place(3, -2, 0);
            if (return_) return;
            place(3, -3, 0);
            if (return_) return;
            place(3, 1, 0);
            if (return_) return;
            place(3, 2, 0);
            if (return_) return;
            place(3, 3, 0);
            if (return_) return;
            place(3, 4, 0);
            if (return_) return;
            place(3, 5, 0);
            if (return_) return;
            //x4
            place(4, 0, 0);
            if (return_) return;
            place(4, -1, 0);
            if (return_) return;
            place(4, -2, 0);
            if (return_) return;
            place(4, 1, 0);
            if (return_) return;
            place(4, 2, 0);
            if (return_) return;
            place(4, 3, 0);
            if (return_) return;
            place(4, 4, 0);
            if (return_) return;
            //x5
            place(5, 0, 0);
            if (return_) return;
            place(5, 1, 0);
            if (return_) return;
            place(5, 2, 0);
            if (return_) return;
            place(5, 3, 0);
            if (return_) return;
            //бок 1
            // x2
            place(2, 0, 1);
            if (return_) return;
            place(2, -1, 1);
            if (return_) return;
            place(2, 1, 1);
            if (return_) return;
            place(2, -2, 1);
            if (return_) return;
            place(2, 2, 1);
            if (return_) return;
            place(2, -3, 1);
            if (return_) return;
            place(2, 3, 1);
            if (return_) return;
            place(2, 4, 1);
            if (return_) return;
            place(2, 5, 1);
            if (return_) return;
            //x3
            place(3, 0, 1);
            if (return_) return;
            place(3, -1, 1);
            if (return_) return;
            place(3, -2, 1);
            if (return_) return;
            place(3, -3, 1);
            if (return_) return;
            place(3, 1, 1);
            if (return_) return;
            place(3, 2, 1);
            if (return_) return;
            place(3, 3, 1);
            if (return_) return;
            place(3, 4, 1);
            if (return_) return;
            place(3, 5, 1);
            if (return_) return;
            //x4
            place(4, 0, 1);
            if (return_) return;
            place(4, -1, 1);
            if (return_) return;
            place(4, -2, 1);
            if (return_) return;
            place(4, 1, 1);
            if (return_) return;
            place(4, 2, 1);
            if (return_) return;
            place(4, 3, 1);
            if (return_) return;
            place(4, 4, 1);
            if (return_) return;
            //бок 2
            // x2
            place(2, 0, -1);
            if (return_) return;
            place(2, -1, -1);
            if (return_) return;
            place(2, 1, -1);
            if (return_) return;
            place(2, -2, -1);
            if (return_) return;
            place(2, 2, -1);
            if (return_) return;
            place(2, -3, -1);
            if (return_) return;
            place(2, 3, -1);
            if (return_) return;
            place(2, 4, -1);
            if (return_) return;
            place(2, 5, -1);
            if (return_) return;
            //x3
            place(3, 0, -1);
            if (return_) return;
            place(3, -1, -1);
            if (return_) return;
            place(3, -2, -1);
            if (return_) return;
            place(3, -3, -1);
            if (return_) return;
            place(3, 1, -1);
            if (return_) return;
            place(3, 2, -1);
            if (return_) return;
            place(3, 3, -1);
            if (return_) return;
            place(3, 4, -1);
            if (return_) return;
            place(3, 5, -1);
            if (return_) return;
            //x4
            place(4, 0, -1);
            if (return_) return;
            place(4, -1, -1);
            if (return_) return;
            place(4, -2, -1);
            if (return_) return;
            place(4, 1, -1);
            if (return_) return;
            place(4, 2, -1);
            if (return_) return;
            place(4, 3, -1);
            if (return_) return;
            place(4, 4, -1);
            if (return_) return;
            //зад
            // x2
            place(-2, 0, 0);
            if (return_) return;
            place(-2, -1, 0);
            if (return_) return;
            place(-2, 1, 0);
            if (return_) return;
            place(-2, -2, 0);
            if (return_) return;
            place(-2, 2, 0);
            if (return_) return;
            place(-2, -3, 0);
            if (return_) return;
            place(-2, 3, 0);
            if (return_) return;
            place(-2, 4, 0);
            if (return_) return;
            place(-2, 5, 0);
            if (return_) return;
            place(-2, 6, 0);
            if (return_) return;
            //x3
            place(-3, 0, 0);
            if (return_) return;
            place(-3, -1, 0);
            if (return_) return;
            place(-3, -2, 0);
            if (return_) return;
            place(-3, -3, 0);
            if (return_) return;
            place(-3, 1, 0);
            if (return_) return;
            place(-3, 2, 0);
            if (return_) return;
            place(-3, 3, 0);
            if (return_) return;
            place(-3, 4, 0);
            if (return_) return;
            place(-3, 5, 0);
            if (return_) return;
            //x4
            place(-4, 0, 0);
            if (return_) return;
            place(-4, -1, 0);
            if (return_) return;
            place(-4, -2, 0);
            if (return_) return;
            place(-4, 1, 0);
            if (return_) return;
            place(-4, 2, 0);
            if (return_) return;
            place(-4, 3, 0);
            if (return_) return;
            place(-4, 4, 0);
            if (return_) return;
            //x5
            place(-5, 0, 0);
            if (return_) return;
            place(-5, 1, 0);
            if (return_) return;
            place(-5, 2, 0);
            if (return_) return;
            place(-5, 3, 0);
            if (return_) return;
            //бок 1
            // x2
            place(-2, 0, 1);
            if (return_) return;
            place(-2, -1, 1);
            if (return_) return;
            place(-2, 1, 1);
            if (return_) return;
            place(-2, -2, 1);
            if (return_) return;
            place(-2, 2, 1);
            if (return_) return;
            place(-2, -3, 1);
            if (return_) return;
            place(-2, 3, 1);
            if (return_) return;
            place(-2, 4, 1);
            if (return_) return;
            place(-2, 5, 1);
            if (return_) return;
            //x3
            place(-3, 0, 1);
            if (return_) return;
            place(-3, -1, 1);
            if (return_) return;
            place(-3, -2, 1);
            if (return_) return;
            place(-3, -3, 1);
            if (return_) return;
            place(-3, 1, 1);
            if (return_) return;
            place(-3, 2, 1);
            if (return_) return;
            place(-3, 3, 1);
            if (return_) return;
            place(-3, 4, 1);
            if (return_) return;
            place(-3, 5, 1);
            if (return_) return;
            //x4
            place(-4, 0, 1);
            if (return_) return;
            place(-4, -1, 1);
            if (return_) return;
            place(-4, -2, 1);
            if (return_) return;
            place(-4, 1, 1);
            if (return_) return;
            place(-4, 2, 1);
            if (return_) return;
            place(-4, 3, 1);
            if (return_) return;
            place(-4, 4, 1);
            if (return_) return;
            //бок 2
            // x2
            place(-2, 0, -1);
            if (return_) return;
            place(-2, -1, -1);
            if (return_) return;
            place(-2, 1, -1);
            if (return_) return;
            place(-2, -2, -1);
            if (return_) return;
            place(-2, 2, -1);
            if (return_) return;
            place(-2, -3, -1);
            if (return_) return;
            place(-2, 3, -1);
            if (return_) return;
            place(-2, 4, -1);
            if (return_) return;
            place(-2, 5, -1);
            if (return_) return;
            //x3
            place(-3, 0, -1);
            if (return_) return;
            place(-3, -1, -1);
            if (return_) return;
            place(-3, -2, -1);
            if (return_) return;
            place(-3, -3, -1);
            if (return_) return;
            place(-3, 1, -1);
            if (return_) return;
            place(-3, 2, -1);
            if (return_) return;
            place(-3, 3, -1);
            if (return_) return;
            place(-3, 4, -1);
            if (return_) return;
            place(-3, 5, -1);
            if (return_) return;
            //x4
            place(-4, 0, -1);
            if (return_) return;
            place(-4, -1, -1);
            if (return_) return;
            place(-4, -2, -1);
            if (return_) return;
            place(-4, 1, -1);
            if (return_) return;
            place(-4, 2, -1);
            if (return_) return;
            place(-4, 3, -1);
            if (return_) return;
            place(-4, 4, -1);
            if (return_) return;
            //право
            // x2
            place(0, 0, 2);
            if (return_) return;
            place(0, -1, 2);
            if (return_) return;
            place(0, 1, 2);
            if (return_) return;
            place(0, -2, 2);
            if (return_) return;
            place(0, 2, 2);
            if (return_) return;
            place(0, -3, 2);
            if (return_) return;
            place(0, 3, 2);
            if (return_) return;
            place(0, 4, 2);
            if (return_) return;
            place(0, 5, 2);
            if (return_) return;
            place(0, 6, 2);
            if (return_) return;
            //x3
            place(0, 0, 3);
            if (return_) return;
            place(0, -1, 3);
            if (return_) return;
            place(0, -2, 3);
            if (return_) return;
            place(0, -3, 3);
            if (return_) return;
            place(0, 1, 3);
            if (return_) return;
            place(0, 2, 3);
            if (return_) return;
            place(0, 3, 3);
            if (return_) return;
            place(0, 4, 3);
            if (return_) return;
            place(0, 5, 3);
            if (return_) return;
            //x4
            place(0, 0, 4);
            if (return_) return;
            place(0, -1, 4);
            if (return_) return;
            place(0, -2, 4);
            if (return_) return;
            place(0, 1, 4);
            if (return_) return;
            place(0, 2, 4);
            if (return_) return;
            place(0, 3, 4);
            if (return_) return;
            place(0, 4, 4);
            if (return_) return;
            //x5
            place(0, 0, 5);
            if (return_) return;
            place(0, 1, 5);
            if (return_) return;
            place(0, 2, 5);
            if (return_) return;
            place(0, 3, 5);
            if (return_) return;
            //бок 1
            // x2
            place(1, 0, 2);
            if (return_) return;
            place(1, -1, 2);
            if (return_) return;
            place(1, 1, 2);
            if (return_) return;
            place(1, -2, 2);
            if (return_) return;
            place(1, 2, 2);
            if (return_) return;
            place(1, -3, 2);
            if (return_) return;
            place(1, 3, 2);
            if (return_) return;
            place(1, 4, 2);
            if (return_) return;
            place(1, 5, 2);
            if (return_) return;
            //x3
            place(1, 0, 3);
            if (return_) return;
            place(1, -1, 3);
            if (return_) return;
            place(1, -2, 3);
            if (return_) return;
            place(1, -3, 3);
            if (return_) return;
            place(1, 1, 3);
            if (return_) return;
            place(1, 2, 3);
            if (return_) return;
            place(1, 3, 3);
            if (return_) return;
            place(1, 4, 3);
            if (return_) return;
            place(1, 5, 3);
            if (return_) return;
            //x4
            place(1, 0, 4);
            if (return_) return;
            place(1, -1, 4);
            if (return_) return;
            place(1, -2, 4);
            if (return_) return;
            place(1, 1, 4);
            if (return_) return;
            place(1, 2, 4);
            if (return_) return;
            place(1, 3, 4);
            if (return_) return;
            place(1, 4, 4);
            if (return_) return;
            //бок 2
            // x2
            place(-1, 0, 2);
            if (return_) return;
            place(-1, -1, 2);
            if (return_) return;
            place(-1, 1, 2);
            if (return_) return;
            place(-1, -2, 2);
            if (return_) return;
            place(-1, 2, 2);
            if (return_) return;
            place(-1, -3, 2);
            if (return_) return;
            place(-1, 3, 2);
            if (return_) return;
            place(-1, 4, 2);
            if (return_) return;
            place(-1, 5, 2);
            if (return_) return;
            //x3
            place(-1, 0, 3);
            if (return_) return;
            place(-1, -1, 3);
            if (return_) return;
            place(-1, -2, 3);
            if (return_) return;
            place(-1, -3, 3);
            if (return_) return;
            place(-1, 1, 3);
            if (return_) return;
            place(-1, 2, 3);
            if (return_) return;
            place(-1, 3, 3);
            if (return_) return;
            place(-1, 4, 3);
            if (return_) return;
            place(-1, 5, 3);
            if (return_) return;
            //x4
            place(-1, 0, 4);
            if (return_) return;
            place(-1, -1, 4);
            if (return_) return;
            place(-1, -2, 4);
            if (return_) return;
            place(-1, 1, 4);
            if (return_) return;
            place(-1, 2, 4);
            if (return_) return;
            place(-1, 3, 4);
            if (return_) return;
            place(-1, 4, 4);
            if (return_) return;
            //лево
            // x2
            place(0, 0, -2);
            if (return_) return;
            place(0, -1, -2);
            if (return_) return;
            place(0, 1, -2);
            if (return_) return;
            place(0, -2, -2);
            if (return_) return;
            place(0, 2, -2);
            if (return_) return;
            place(0, -3, -2);
            if (return_) return;
            place(0, 3, -2);
            if (return_) return;
            place(0, 4, -2);
            if (return_) return;
            place(0, 5, -2);
            if (return_) return;
            place(0, 6, -2);
            if (return_) return;
            //x3
            place(0, 0, -3);
            if (return_) return;
            place(0, -1, -3);
            if (return_) return;
            place(0, -2, -3);
            if (return_) return;
            place(0, -3, -3);
            if (return_) return;
            place(0, 1, -3);
            if (return_) return;
            place(0, 2, -3);
            if (return_) return;
            place(0, 3, -3);
            if (return_) return;
            place(0, 4, -3);
            if (return_) return;
            place(0, 5, -3);
            if (return_) return;
            //x4
            place(0, 0, -4);
            if (return_) return;
            place(0, -1, -4);
            if (return_) return;
            place(0, -2, -4);
            if (return_) return;
            place(0, 1, -4);
            if (return_) return;
            place(0, 2, -4);
            if (return_) return;
            place(0, 3, -4);
            if (return_) return;
            place(0, 4, -4);
            if (return_) return;
            //x5
            place(0, 0, -5);
            if (return_) return;
            place(0, 1, -5);
            if (return_) return;
            place(0, 2, -5);
            if (return_) return;
            place(0, 3, -5);
            if (return_) return;
            //бок 1
            // x2
            place(1, 0, -2);
            if (return_) return;
            place(1, -1, -2);
            if (return_) return;
            place(1, 1, -2);
            if (return_) return;
            place(1, -2, -2);
            if (return_) return;
            place(1, 2, -2);
            if (return_) return;
            place(1, -3, -2);
            if (return_) return;
            place(1, 3, -2);
            if (return_) return;
            place(1, 4, -2);
            if (return_) return;
            place(1, 5, -2);
            if (return_) return;
            //x3
            place(1, 0, -3);
            if (return_) return;
            place(1, -1, -3);
            if (return_) return;
            place(1, -2, -3);
            if (return_) return;
            place(1, -3, -3);
            if (return_) return;
            place(1, 1, -3);
            if (return_) return;
            place(1, 2, -3);
            if (return_) return;
            place(1, 3, -3);
            if (return_) return;
            place(1, 4, -3);
            if (return_) return;
            place(1, 5, -3);
            if (return_) return;
            //x4
            place(1, 0, -4);
            if (return_) return;
            place(1, -1, -4);
            if (return_) return;
            place(1, -2, -4);
            if (return_) return;
            place(1, 1, -4);
            if (return_) return;
            place(1, 2, -4);
            if (return_) return;
            place(1, 3, -4);
            if (return_) return;
            place(1, 4, -4);
            if (return_) return;
            //бок 2
            // x2
            place(-1, 0, -2);
            if (return_) return;
            place(-1, -1, -2);
            if (return_) return;
            place(-1, 1, -2);
            if (return_) return;
            place(-1, -2, -2);
            if (return_) return;
            place(-1, 2, -2);
            if (return_) return;
            place(-1, -3, -2);
            if (return_) return;
            place(-1, 3, -2);
            if (return_) return;
            place(-1, 4, -2);
            if (return_) return;
            place(-1, 5, -2);
            if (return_) return;
            //x3
            place(-1, 0, -3);
            if (return_) return;
            place(-1, -1, -3);
            if (return_) return;
            place(-1, -2, -3);
            if (return_) return;
            place(-1, -3, -3);
            if (return_) return;
            place(-1, 1, -3);
            if (return_) return;
            place(-1, 2, -3);
            if (return_) return;
            place(-1, 3, -3);
            if (return_) return;
            place(-1, 4, -3);
            if (return_) return;
            place(-1, 5, -3);
            if (return_) return;
            //x4
            place(-1, 0, -4);
            if (return_) return;
            place(-1, -1, -4);
            if (return_) return;
            place(-1, -2, -4);
            if (return_) return;
            place(-1, 1, -4);
            if (return_) return;
            place(-1, 2, -4);
            if (return_) return;
            place(-1, 3, -4);
            if (return_) return;
            place(-1, 4, -4);
            if (return_) return;
            //полоска ++
            place(2, -3, 2);
            if (return_) return;
            place(2, -2, 2);
            if (return_) return;
            place(2, -1, 2);
            if (return_) return;
            place(2, 0, 2);
            if (return_) return;
            place(2, 1, 2);
            if (return_) return;
            place(2, 2, 2);
            if (return_) return;
            place(2, 3, 2);
            if (return_) return;
            place(2, 4, 2);
            if (return_) return;
            place(2, 5, 2);
            if (return_) return;
            //полоска +-
            place(2, -3, -2);
            if (return_) return;
            place(2, -2, -2);
            if (return_) return;
            place(2, -1, -2);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(2, 1, -2);
            if (return_) return;
            place(2, 2, -2);
            if (return_) return;
            place(2, 3, -2);
            if (return_) return;
            place(2, 4, -2);
            if (return_) return;
            place(2, 5, -2);
            if (return_) return;
            //полоска --
            place(-2, -3, -2);
            if (return_) return;
            place(-2, -2, -2);
            if (return_) return;
            place(-2, -1, -2);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(-2, 1, -2);
            if (return_) return;
            place(-2, 2, -2);
            if (return_) return;
            place(-2, 3, -2);
            if (return_) return;
            place(-2, 4, -2);
            if (return_) return;
            place(-2, 5, -2);
            if (return_) return;
            //полоска -+
            place(-2, -3, 2);
            if (return_) return;
            place(-2, -2, 2);
            if (return_) return;
            place(-2, -1, 2);
            if (return_) return;
            place(-2, 0, 2);
            if (return_) return;
            place(-2, 1, 2);
            if (return_) return;
            place(-2, 2, 2);
            if (return_) return;
            place(-2, 3, 2);
            if (return_) return;
            place(-2, 4, 2);
            if (return_) return;
            place(-2, 5, 2);
            //полоска ++ номер 2
            place(3, -2, 2);
            if (return_) return;
            place(3, -1, 2);
            if (return_) return;
            place(3, 0, 2);
            if (return_) return;
            place(3, 1, 2);
            if (return_) return;
            place(3, 2, 2);
            if (return_) return;
            place(3, 3, 2);
            if (return_) return;
            place(3, 4, 2);
            if (return_) return;
            place(3, 5, 2);
            if (return_) return;
            //
            place(2, -2, 3);
            if (return_) return;
            place(2, -1, 3);
            if (return_) return;
            place(2, 0, 3);
            if (return_) return;
            place(2, 1, 3);
            if (return_) return;
            place(2, 2, 3);
            if (return_) return;
            place(2, 3, 3);
            if (return_) return;
            place(2, 4, 3);
            if (return_) return;
            place(2, 5, 3);
            if (return_) return;
            //полоска +- номер 2
            place(3, -2, -2);
            if (return_) return;
            place(3, -1, -2);
            if (return_) return;
            place(3, 0, -2);
            if (return_) return;
            place(3, 1, -2);
            if (return_) return;
            place(3, 2, -2);
            if (return_) return;
            place(3, 3, -2);
            if (return_) return;
            place(3, 4, -2);
            if (return_) return;
            place(3, 5, -2);
            if (return_) return;
            //
            place(2, -2, -3);
            if (return_) return;
            place(2, -1, -3);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(2, 1, -3);
            if (return_) return;
            place(2, 2, -3);
            if (return_) return;
            place(2, 3, -3);
            if (return_) return;
            place(2, 4, -3);
            if (return_) return;
            place(2, 5, -3);
            if (return_) return;
            //полоска -- номер 2
            place(-3, -2, -2);
            if (return_) return;
            place(-3, -1, -2);
            if (return_) return;
            place(-3, 0, -2);
            if (return_) return;
            place(-3, 1, -2);
            if (return_) return;
            place(-3, 2, -2);
            if (return_) return;
            place(-3, 3, -2);
            if (return_) return;
            place(-3, 4, -2);
            if (return_) return;
            place(-3, 5, -2);
            if (return_) return;
            //
            place(-2, -2, -3);
            if (return_) return;
            place(-2, -1, -3);
            if (return_) return;
            place(-2, 0, -3);
            if (return_) return;
            place(-2, 1, -3);
            if (return_) return;
            place(-2, 2, -3);
            if (return_) return;
            place(-2, 3, -3);
            if (return_) return;
            place(-2, 4, -3);
            if (return_) return;
            place(-2, 5, -3);
            if (return_) return;
            //полоска -+ номер 2
            place(-3, -2, 2);
            if (return_) return;
            place(-3, -1, 2);
            if (return_) return;
            place(-3, 0, 2);
            if (return_) return;
            place(-3, 1, 2);
            if (return_) return;
            place(-3, 2, 2);
            if (return_) return;
            place(-3, 3, 2);
            if (return_) return;
            place(-3, 4, 2);
            if (return_) return;
            place(-3, 5, 2);
            if (return_) return;
            //
            place(-2, -2, 3);
            if (return_) return;
            place(-2, -1, 3);
            if (return_) return;
            place(-2, 0, 3);
            if (return_) return;
            place(-2, 1, 3);
            if (return_) return;
            place(-2, 2, 3);
            if (return_) return;
            place(-2, 3, 3);
            if (return_) return;
            place(-2, 4, 3);
            if (return_) return;
            place(-2, 5, 3);
            if (return_) return;
            //полоска ++ 3
            place(3, -2, 3);
            if (return_) return;
            place(3, -1, 3);
            if (return_) return;
            place(3, 0, 3);
            if (return_) return;
            place(3, 1, 3);
            if (return_) return;
            place(3, 2, 3);
            if (return_) return;
            place(3, 3, 3);
            if (return_) return;
            place(3, 4, 3);
            if (return_) return;
            //полоска +- 3
            place(3, -2, -3);
            if (return_) return;
            place(3, -1, -3);
            if (return_) return;
            place(3, 0, -3);
            if (return_) return;
            place(3, 1, -3);
            if (return_) return;
            place(3, 2, -3);
            if (return_) return;
            place(3, 3, -3);
            if (return_) return;
            place(3, 4, -3);
            if (return_) return;
            //полоска -- 3
            place(-3, -2, -3);
            if (return_) return;
            place(-3, -1, -3);
            if (return_) return;
            place(-3, 0, -3);
            if (return_) return;
            place(-3, 1, -3);
            if (return_) return;
            place(-3, 2, -3);
            if (return_) return;
            place(-3, 3, -3);
            if (return_) return;
            place(-3, 4, -3);
            if (return_) return;
            //полоска -+ 3
            place(-3, -2, 3);
            if (return_) return;
            place(-3, -1, 3);
            if (return_) return;
            place(-3, 0, 3);
            if (return_) return;
            place(-3, 1, 3);
            if (return_) return;
            place(-3, 2, 3);
            if (return_) return;
            place(-3, 3, 3);
            if (return_) return;
            place(-3, 4, 3);
            if (return_) return;
            //полоска ++ номер 4
            place(4, -1, 2);
            if (return_) return;
            place(4, 0, 2);
            if (return_) return;
            place(4, 1, 2);
            if (return_) return;
            place(4, 2, 2);
            if (return_) return;
            place(4, 3, 2);
            if (return_) return;
            place(4, 4, 2);
            if (return_) return;
            //
            place(2, -1, 4);
            if (return_) return;
            place(2, 0, 4);
            if (return_) return;
            place(2, 1, 4);
            if (return_) return;
            place(2, 2, 4);
            if (return_) return;
            place(2, 3, 4);
            if (return_) return;
            place(2, 4, 4);
            if (return_) return;
            //полоска +- номер 4
            place(4, -1, -2);
            if (return_) return;
            place(4, 0, -2);
            if (return_) return;
            place(4, 1, -2);
            if (return_) return;
            place(4, 2, -2);
            if (return_) return;
            place(4, 3, -2);
            if (return_) return;
            place(4, 4, -2);
            if (return_) return;
            //
            place(2, -1, -4);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
            place(2, 1, -4);
            if (return_) return;
            place(2, 2, -4);
            if (return_) return;
            place(2, 3, -4);
            if (return_) return;
            place(2, 4, -4);
            if (return_) return;
            //полоска -- номер 4
            place(-4, -1, -2);
            if (return_) return;
            place(-4, 0, -2);
            if (return_) return;
            place(-4, 1, -2);
            if (return_) return;
            place(-4, 2, -2);
            if (return_) return;
            place(-4, 3, -2);
            if (return_) return;
            place(-4, 4, -2);
            if (return_) return;
            //
            place(-2, -1, -4);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(-2, 1, -4);
            if (return_) return;
            place(-2, 2, -4);
            if (return_) return;
            place(-2, 3, -4);
            if (return_) return;
            place(-2, 4, -4);
            if (return_) return;
            //полоска -+ номер 4
            place(-4, -1, 2);
            if (return_) return;
            place(-4, 0, 2);
            if (return_) return;
            place(-4, 1, 2);
            if (return_) return;
            place(-4, 2, 2);
            if (return_) return;
            place(-4, 3, 2);
            if (return_) return;
            place(-4, 4, 2);
            if (return_) return;
            //
            place(-2, -1, 4);
            if (return_) return;
            place(-2, 0, 4);
            if (return_) return;
            place(-2, 1, 4);
            if (return_) return;
            place(-2, 2, 4);
            if (return_) return;
            place(-2, 3, 4);
            if (return_) return;
            place(-2, 4, 4);
            if (return_) return;

            //++l
            place(4, -1, 3);
            if (return_) return;
            place(4, 0, 3);
            if (return_) return;
            place(4, 1, 3);
            if (return_) return;
            place(4, 2, 3);
            if (return_) return;
            place(4, 3, 3);
            if (return_) return;
            //++r
            place(3, -1, 4);
            if (return_) return;
            place(3, 0, 4);
            if (return_) return;
            place(3, 1, 4);
            if (return_) return;
            place(3, 2, 4);
            if (return_) return;
            place(3, 3, 4);
            if (return_) return;
            //+-l
            place(4, -1, -3);
            if (return_) return;
            place(4, 0, -3);
            if (return_) return;
            place(4, 1, -3);
            if (return_) return;
            place(4, 2, -3);
            if (return_) return;
            place(4, 3, -3);
            if (return_) return;
            //+-r
            place(3, -1, -4);
            if (return_) return;
            place(3, 0, -4);
            if (return_) return;
            place(3, 1, -4);
            if (return_) return;
            place(3, 2, -4);
            if (return_) return;
            place(3, 3, -4);
            if (return_) return;
            //-+l
            place(-4, -1, 3);
            if (return_) return;
            place(-4, 0, 3);
            if (return_) return;
            place(-4, 1, 3);
            if (return_) return;
            place(-4, 2, 3);
            if (return_) return;
            place(-4, 3, 3);
            if (return_) return;
            //-+r
            place(-3, -1, 4);
            if (return_) return;
            place(-3, 0, 4);
            if (return_) return;
            place(-3, 1, 4);
            if (return_) return;
            place(-3, 2, 4);
            if (return_) return;
            place(-3, 3, 4);
            if (return_) return;
            //--l
            place(-4, -1, -3);
            if (return_) return;
            place(-4, 0, -3);
            if (return_) return;
            place(-4, 1, -3);
            if (return_) return;
            place(-4, 2, -3);
            if (return_) return;
            place(-4, 3, -3);
            if (return_) return;
            //--r
            place(-3, -1, -4);
            if (return_) return;
            place(-3, 0, -4);
            if (return_) return;
            place(-3, 1, -4);
            if (return_) return;
            place(-3, 2, -4);
            if (return_) return;
            place(-3, 3, -4);
            if (return_) return;

            //dop+x
            place(5, 0, 0);
            if (return_) return;
            place(5, 1, 0);
            if (return_) return;
            place(5, 2, 0);
            if (return_) return;
            //
            place(5, 0, 1);
            if (return_) return;
            place(5, 1, 1);
            if (return_) return;
            place(5, 2, 1);
            if (return_) return;
            //
            place(5, 0, -1);
            if (return_) return;
            place(5, 1, -1);
            if (return_) return;
            place(5, 2, -1);
            if (return_) return;

            //dop-x
            place(-5, 0, 0);
            if (return_) return;
            place(-5, 1, 0);
            if (return_) return;
            place(-5, 2, 0);
            if (return_) return;
            //
            place(-5, 0, 1);
            if (return_) return;
            place(-5, 1, 1);
            if (return_) return;
            place(-5, 2, 1);
            if (return_) return;
            //
            place(-5, 0, -1);
            if (return_) return;
            place(-5, 1, -1);
            if (return_) return;
            place(-5, 2, -1);
            if (return_) return;

            //dop+z
            place(0, 0, 5);
            if (return_) return;
            place(0, 1, 5);
            if (return_) return;
            place(0, 2, 5);
            if (return_) return;
            //
            place(1, 0, 5);
            if (return_) return;
            place(1, 1, 5);
            if (return_) return;
            place(1, 2, 5);
            if (return_) return;
            //
            place(-1, 0, 5);
            if (return_) return;
            place(-1, 1, 5);
            if (return_) return;
            place(-1, 2, 5);
            if (return_) return;

            //dop-z
            place(0, 0, -5);
            if (return_) return;
            place(0, 1, -5);
            if (return_) return;
            place(0, 2, -5);
            if (return_) return;
            //
            place(1, 0, -5);
            if (return_) return;
            place(1, 1, -5);
            if (return_) return;
            place(1, 2, -5);
            if (return_) return;
            //
            place(-1, 0, -5);
            if (return_) return;
            place(-1, 1, -5);
            if (return_) return;
            place(-1, 2, -5);
            if (return_) return;

            //finish+x
            place(5, 1, 1);
            if (return_) return;
            place(5, 1, -1);
            if (return_) return;

            //finish-x
            place(-5, 1, 1);
            if (return_) return;
            place(-5, 1, -1);
            if (return_) return;

            //finish+z
            place(1, 1, 5);
            if (return_) return;
            place(-1, 1, 5);
            if (return_) return;

            //finish-z
            place(1, 1, -5);
            if (return_) return;
            place(-1, 1, -5);
            if (return_) return;

        }

    }

    private boolean blockFilter(Block block) {
        return block == Blocks.OBSIDIAN ||
            block == Blocks.CRYING_OBSIDIAN ||
            block == Blocks.NETHERITE_BLOCK ||
            block == Blocks.ENDER_CHEST ||
            block == Blocks.NETHERRACK ||
            block == Blocks.STONE ||
            block == Blocks.BEDROCK ||
            block == Blocks.QUARTZ_BLOCK ||
            block == Blocks.GLASS ||
            block == Blocks.MELON ||
            block == Blocks.CAKE ||
            block == Blocks.CACTUS ||
            block == Blocks.TORCH ||
            block == Blocks.SEA_LANTERN ||
            block == Blocks.SHULKER_BOX ||
            block == Blocks.BLUE_BED ||
            block == Blocks.SMITHING_TABLE ||
            block == Blocks.WHITE_BED ||
            block == Blocks.WITHER_SKELETON_SKULL ||
            block == Blocks.GLOWSTONE ||
            block == Blocks.PUMPKIN ||
            block == Blocks.COBBLESTONE ||
            block == Blocks.CRAFTING_TABLE ||
            block == Blocks.DIAMOND_BLOCK ||
            block == Blocks.TURTLE_EGG ||
            block == Blocks.SLIME_BLOCK ||
            block == Blocks.AIR ||
            block == Blocks.SPONGE ||
            block == Blocks.TNT ||



            block == Blocks.RESPAWN_ANCHOR;
    }

    private boolean place(int x, int y, int z) {
        setBlockPos(x, y, z);
        BlockState blockState = mc.world.getBlockState(blockPos);
        if (!blockState.getMaterial().isReplaceable()) return true;
        if (BlockUtils.place(blockPos, InvUtils.findInHotbar(itemStack -> blocks.get().contains(Block.getBlockFromItem(itemStack.getItem()))), rotate.get(), 100, checkEntity.get())) {
            return_ = true;
        }
        return false;
    }

    private void setBlockPos(int x, int y, int z) {
        blockPos.set(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);
    }
}

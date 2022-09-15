package com.example.addon.modules.BebraHackPVP;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
/*
@Author Ya_pank
not sunochek code
 */

public class AntiSurroundBlocks extends Module {
    public enum Mode {Button, Web, Lever, Glass, String}

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgMode = settings.createGroup("Mode");

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder().name("Range").description("The maximum distance to target players/place range.").defaultValue(4).range(0, 5).sliderMax(5).build());
    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder().name("Rotate").description("Rotates when placing.").defaultValue(true).build());
    private final Setting<Boolean> disable = sgGeneral.add(new BoolSetting.Builder().name("AutoDisable").description("Disabling after place.").defaultValue(true).build());
    //private final Setting<Boolean> support = sgGeneral.add(new BoolSetting.Builder().name("Support").description("PLacing Obsidian in bottom.").defaultValue(false).build());
    //modes
    public final Setting<AntiSurroundBlocks.Mode> mode = sgMode.add(new EnumSetting.Builder<AntiSurroundBlocks.Mode>().name("Mode").description("AntiSurroundBlocks mode.").defaultValue(AntiSurroundBlocks.Mode.Button).build());
    //render

    private PlayerEntity target;
    private FindItemResult item;
    private BlockPos pos;


    public AntiSurroundBlocks() {
        super(Addon.combat, "AntiSurroundBlocks", "Automatically trying to fuck players surround.");
    }

    @Override
    public void onActivate() {
        target = null;
        pos = null;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        target = TargetUtils.getPlayerTarget(range.get(), SortPriority.LowestDistance);
        if (target == null) return;
        pos = target.getBlockPos();
        switch (mode.get()) {
            case Button -> item = InvUtils.findInHotbar(Items.CRIMSON_BUTTON);
            case Web -> item = InvUtils.findInHotbar(Items.COBWEB);
            case Glass -> item = InvUtils.findInHotbar(Items.GLASS_PANE);
            case Lever -> item = InvUtils.findInHotbar(Items.LEVER);
            case String -> item = InvUtils.findInHotbar(Items.STRING);
        }
        /*
        if (support.get()) {
            doPlace(target.getBlockPos().down(), InvUtils.findInHotbar(Items.OBSIDIAN));
        }
        */

        for (Direction dir : Direction.values()) {
            if (dir == Direction.DOWN || dir == Direction.UP) continue;
            boolean canPlace = BlockUtils.canPlace(pos.offset(dir));
            if (canPlace) {
                doPlace(target.getBlockPos(), item);
                if (disable.get()) {
                    toggle();
                }
                return;
            }
        }
    }

    private void doPlace(BlockPos blockPos, FindItemResult findItemResult) {
        if (mode.get().equals(Mode.Button) || mode.get().equals(Mode.Web) || mode.get().equals(Mode.Glass) || mode.get().equals(Mode.String)) {
            for (Direction dir : Direction.values()) {
                if (dir == Direction.DOWN || dir == Direction.UP) continue;
                BlockUtils.place(pos.offset(dir), item, rotate.get(), 0);
            }
        } else {
            for (Direction dir : Direction.values()) {
                if (dir == Direction.DOWN || dir == Direction.UP) continue;
                BlockUtils.place(pos.offset(dir).down(), item, rotate.get(), 0);
            }
        }
    }
}

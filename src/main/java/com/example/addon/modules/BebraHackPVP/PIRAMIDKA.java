package com.example.addon.modules.BebraHackPVP;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.Collections;
import java.util.List;

public class PIRAMIDKA extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgMisc = settings.createGroup("Misc");

    private final Setting<Boolean> doubleHeight = sgGeneral.add(new BoolSetting.Builder().name("double-height").description("anti face place").defaultValue(true).build());
    private final Setting<Boolean> antiCity = sgGeneral.add(new BoolSetting.Builder().name("anti-city").description("pyramid half").defaultValue(true).build());
    private final Setting<Boolean> downBlock = sgGeneral.add(new BoolSetting.Builder().name("down-block").description("pyramid down").defaultValue(true).build());
    private final Setting<Boolean> upBlock = sgGeneral.add(new BoolSetting.Builder().name("up-block").description("pyramid up").defaultValue(true).build());
    private final Setting<Boolean> upupBlock = sgGeneral.add(new BoolSetting.Builder().name("up2-down").description("pyramid up 2 times").defaultValue(false).build());
    private final Setting<Boolean> antiCev = sgGeneral.add(new BoolSetting.Builder().name("anti-cev").description("anti-cev").defaultValue(false).build());
    private final Setting<Boolean> checkEntity = sgMisc.add(new BoolSetting.Builder().name("check-entity").description("entity check").defaultValue(true).build());
    private final Setting<Boolean> onlyOnGround = sgMisc.add(new BoolSetting.Builder().name("only-on-ground").description("Works only when you standing on blocks.").defaultValue(false).build());
    private final Setting<Boolean> center = sgMisc.add(new BoolSetting.Builder().name("center").description("Teleports you to the center of the block.").defaultValue(true).build());
    private final Setting<Boolean> disableOnJump = sgMisc.add(new BoolSetting.Builder().name("disable-on-jump").description("Automatically disables when you jump.").defaultValue(false).build());
    private final Setting<Boolean> disableOnYChange = sgMisc.add(new BoolSetting.Builder().name("disable-on-y-change").description("Automatically disables when your y level (step, jumping, atc).").defaultValue(false).build());
    private final Setting<Boolean> rotate = sgMisc.add(new BoolSetting.Builder().name("rotate").description("Automatically faces towards the obsidian being placed.").defaultValue(false).build());
    private final Setting<List<Block>> blocks = sgMisc.add(new BlockListSetting.Builder().name("block").description("What blocks to use for surround.").defaultValue(Collections.singletonList(Blocks.OBSIDIAN)).filter(this::blockFilter).build());

    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean return_;

    public PIRAMIDKA() {
        super(Addon.combat, "PIRAMIDKA", "for monkeys");
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

        if (doubleHeight.get()) {
            place(1, 1, 0);
            if (return_) return;
            place(-1, 1, 0);
            if (return_) return;
            place(0, 1, 1);
            if (return_) return;
            place(0, 1, -1);
            if (return_) return;

        }

        if (antiCity.get()){
            place(0, 0, 2);
            if (return_) return;
            place(2, 0, 0);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(-2, 0, 0);
            if (return_) return;
            place(1, 0, 1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, 1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(1, -1, 0);
            if (return_) return;
            place(-1, -1, 0);
            if (return_) return;
            place(0, -1, 1);
            if (return_) return;
            place(0, -1, -1);
            if (return_) return;
        }

        if (downBlock.get()) {
            place(0, -2, 0);
            if (return_) return;
        }

        if (upBlock.get()) {
            place(0, 2, 0);
            if (return_) return;
        }

        if (antiCev.get()){
            place(0, 3, 0);
            if (return_) return;
            place(1, 2, 0);
            if (return_) return;
            place(-1, 2, 0);
            if (return_) return;
            place(0, 2, 1);
            if (return_) return;
            place(0, 2, -1);
            if (return_) return;
        }

        if (upupBlock.get() && !antiCev.get()){
            place(0, 3, 0);
            if (return_) return;
        }
    }

    private boolean blockFilter(Block block) {
        return  block.getBlastResistance() >= 600;
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

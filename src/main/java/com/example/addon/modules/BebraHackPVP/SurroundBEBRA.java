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
public class SurroundBEBRA extends Module {
    private final SettingGroup sgGeneral = settings.createGroup("general");
    private final Setting<Boolean> NoAirPlace = sgGeneral.add(new BoolSetting.Builder().name("NoAirPlace").description("For no airplace servers.").defaultValue(false).build());
    private final Setting<Boolean> DoubleScaffold = sgGeneral.add(new BoolSetting.Builder().name("DoubleScaffold").description("place 2 blocks under you.").defaultValue(false).build());
    private final Setting<Boolean> ForSelfAnvilBEBRA = sgGeneral.add(new BoolSetting.Builder().name("NoAirPlaceAnvil").description("if you use SelfAnvilBEBRA, then turn on all modes including this one, but turn off SelfTrap").defaultValue(false).build());

    private final SettingGroup sgMisc = settings.createGroup("Misc");

    private final Setting<Boolean> checkEntity = sgMisc.add(new BoolSetting.Builder().name("check-entity").description("entity check").defaultValue(true).build());
    private final Setting<Boolean> onlyOnGround = sgMisc.add(new BoolSetting.Builder().name("only-on-ground").description("Works only when you standing on blocks.").defaultValue(false).build());
    private final Setting<Boolean> center = sgMisc.add(new BoolSetting.Builder().name("center").description("Teleports you to the center of the block.").defaultValue(true).build());
    private final Setting<Boolean> disableOnJump = sgMisc.add(new BoolSetting.Builder().name("disable-on-jump").description("Automatically disables when you jump.").defaultValue(false).build());
    private final Setting<Boolean> disableOnYChange = sgMisc.add(new BoolSetting.Builder().name("disable-on-y-change").description("Automatically disables when your y level (step, jumping, atc).").defaultValue(false).build());
    private final Setting<Boolean> rotate = sgMisc.add(new BoolSetting.Builder().name("rotate").description("Automatically faces towards the obsidian being placed.").defaultValue(false).build());
    private final Setting<List<Block>> blocks = sgMisc.add(new BlockListSetting.Builder().name("block").description("What blocks to use for surround.").defaultValue(Collections.singletonList(Blocks.OBSIDIAN)).filter(this::blockFilter).build());

    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean return_;

    public SurroundBEBRA() {
        super(Addon.combat, "SurroundBEBRA", "SurroundBEBRA for 1.12.2 and 1.19.2");
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
        if (DoubleScaffold.get()) {
            place(0, -1, 0);
            if (return_) return;
            place(0, -2, 0);
            if (return_) return;
        }
        if (NoAirPlace.get()) {
            place(1, -1, 0);
            if (return_) return;
            place(-1, -1, 0);
            if (return_) return;
            place(0, -1, 1);
            if (return_) return;
            place(0, -1, -1);
            if (return_) return;
        }
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

        if (ForSelfAnvilBEBRA.get()) {
            place(1, 1, 0);
            if (return_) return;
            place(1, 2, 0);
            if (return_) return;
        }

    }

    private boolean blockFilter(Block block) {
        return block.getBlastResistance() >= 600;
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

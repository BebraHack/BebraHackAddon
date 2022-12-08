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

public class SelfAnvilBEBRA extends Module {
    //private final SettingGroup sgGeneral = settings.createGroup("general");

    //private final Setting<Boolean> oneAnvil = sgGeneral.add(new BoolSetting.Builder().name("one").description("For no airplace servers.").defaultValue(false).build());

    //private final Setting<Boolean> NoAirPlace = sgGeneral.add(new BoolSetting.Builder().name("NoAirPlace").description("For no airplace servers.").defaultValue(false).build());
    private final SettingGroup sgMisc = settings.createGroup("Misc");

    private final Setting<Boolean> checkEntity = sgMisc.add(new BoolSetting.Builder().name("check-entity").description("entity check").defaultValue(true).build());
    private final Setting<Boolean> center = sgMisc.add(new BoolSetting.Builder().name("center").description("Teleports you to the center of the block.").defaultValue(true).build());

    private final Setting<Boolean> disableOnYChange = sgMisc.add(new BoolSetting.Builder().name("disable-on-y-change").description("Automatically disables when your y level (step, jumping, atc).").defaultValue(false).build());
    private final Setting<Boolean> rotate = sgMisc.add(new BoolSetting.Builder().name("rotate").description("Automatically faces towards the Anvil being placed.").defaultValue(false).build());
    private final Setting<List<Block>> blocks = sgMisc.add(new BlockListSetting.Builder().name("block").description("What blocks to use for surround.").defaultValue(Collections.singletonList(Blocks.ANVIL)).filter(this::blockFilter).build());

    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean return_;

    public SelfAnvilBEBRA() {
        super(Addon.combat, "AutoAnvilBEBRA", "puts in throws off anvils on you");
    }

    @Override
    public void onActivate() {
        if (center.get()) PlayerUtils.centerPlayer();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {

        place(0, 2, 0);
        if (return_) return;

        /*
        if(oneAnvil .get()) {
            place(0, 2, 0);
            toggle();
        }

         */
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

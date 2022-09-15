package com.example.addon.modules.misc;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

public class AutoBuildSwastika extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<type> build = sgGeneral.add(new EnumSetting.Builder<type>().name("build").description("Which type gonna build.").defaultValue(type.Swastika).build());
    private final Setting<Integer> bpt = sgGeneral.add(new IntSetting.Builder().name("block-per-tick").description("Block per tick.").defaultValue(1).range(0, 40).build());

    public AutoBuildSwastika() {
        super(Addon.misc, "auto-build-rewrite", "Places build that you choose.");
    }

    private int ticks = 0;
    private BlockPos[] blockPos;

    @Override
    public void onActivate() {
        ticks = 0;
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        assert mc.world != null && mc.player != null;
        init();

        ticks = 0;
        // не изменяй блять
        for (int pos = 0; pos < blockPos.length; pos++) {
            if (BlockUtils.canPlace(blockPos[pos], true) && ticks <= bpt.get()) {
                BlockUtils.place(blockPos[pos], findBlock(), 50);
                ticks++;
            }
        }
    }

    public void init() {
        switch (mc.player.getHorizontalFacing()) {
            case NORTH -> {
                if (build.get() == type.Swastika)
                    blockPos = new BlockPos[]{add(2, 0, -1), add(0, 0, -1), add(-1, 0, -1), add(-2, 0, -1), add(2, 1, -1), add(0, 1, -1), add(2, 2, -1), add(1, 2, -1), add(0, 2, -1), add(-1, 2, -1), add(-2, 2, -1), add(0, 3, -1), add(-2, 3, -1), add(2, 4, -1), add(1, 4, -1), add(0, 4, -1), add(-2, 4, -1)};
            }
            case SOUTH -> {
                if (build.get() == type.Swastika)
                    blockPos = new BlockPos[]{add(-2, 0, 1), add(0, 0, 1), add(1, 0, 1), add(2, 0, 1), add(-2, 1, 1), add(0, 1, 1), add(-2, 2, 1), add(-1, 2, 1), add(0, 2, 1), add(1, 2, 1), add(2, 2, 1), add(0, 3, 1), add(2, 3, 1), add(-2, 4, 1), add(-1, 4, 1), add(0, 4, 1), add(2, 4, 1)};
            }
            case WEST -> {
                if (build.get() == type.Swastika)
                    blockPos = new BlockPos[]{add(-1, 0, -2), add(-1, 0, 0), add(-1, 0, 1), add(-1, 0, 2), add(-1, 1, -2), add(-1, 1, 0), add(-1, 2, -2), add(-1, 2, -1), add(-1, 2, 0), add(-1, 2, 1), add(-1, 2, 2), add(-1, 3, 0), add(-1, 3, 2), add(-1, 4, -2), add(-1, 4, -1), add(-1, 4, 0), add(-1, 4, 2)};
            }
            case EAST -> {
                if (build.get() == type.Swastika)
                    blockPos = new BlockPos[]{add(1, 0, 2), add(1, 0, 0), add(1, 0, -1), add(1, 0, -2), add(1, 1, 2), add(1, 1, 0), add(1, 2, 2), add(1, 2, 1), add(1, 2, 0), add(1, 2, -1), add(1, 2, -2), add(1, 3, 0), add(1, 3, -2), add(1, 4, 2), add(1, 4, 1), add(1, 4, 0), add(1, 4, -2)};
            }
        }
    }

    public BlockPos add(int x, int y, int z) {
        return new BlockPos(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);
    }

    private FindItemResult findBlock() {
        return InvUtils.findInHotbar(itemStack -> itemStack.getItem() instanceof BlockItem);
    }

    public enum type {
        Penis,
        Swastika,
        Golem,
        Wither
    }
}

package bebra.hack.addon.modules.pve;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.utils.Utils;

import java.util.List;

public class Platforms extends Module {
    public enum build {Platform3x3, Platform5x5;}
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean place, toggle;
    private final Setting<Boolean> checkEntity = sgGeneral.add(new BoolSetting.Builder().name("check-entity").description("entity check").defaultValue(true).build());
    private final SettingGroup sgMisc = settings.createGroup("Misc");
    private final Setting<build> buildMod = sgMisc.add(new EnumSetting.Builder<build>().name("build").description(" ").defaultValue(build.Platform3x3).build());
    private final Setting<Boolean> rotate = sgMisc.add(new BoolSetting.Builder().name("rotate").description(" ").defaultValue(true).build());
    private final Setting<Boolean> togg = sgMisc.add(new BoolSetting.Builder().name("auto-toggle").description(" ").defaultValue(true).build());
    private final Setting<Boolean> Board3x3byX = sgGeneral.add(new BoolSetting.Builder().name("Board 3x3byX").description("Board 3x3byX.").defaultValue(false).build());
    private final Setting<Boolean> Board3x3byZ = sgGeneral.add(new BoolSetting.Builder().name("Board 3x3byZ").description("Board 3x3byZ.").defaultValue(false).build());
    private final Setting<Boolean> Board5x5byX = sgGeneral.add(new BoolSetting.Builder().name("Board 5x5byX").description("Board 5x5byX.").defaultValue(false).build());
    private final Setting<Boolean> Board5x5byZ = sgGeneral.add(new BoolSetting.Builder().name("Board 5x5byZ").description("Board 5x5byZ.").defaultValue(false).build());
    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("Blocks to search for.")
        .onChanged(blocks1 -> {
            if (isActive() && Utils.canUpdate()) onActivate();
        })
        .build()
    );
    @EventHandler
    private void onTick(TickEvent.Pre event) {
        place = false;

        if(Board3x3byX .get()) {
            p(-1, 0, 2);
            if (place) return;
            p(0, 0, 2);
            if (place) return;
            p(1, 0, 2);
            if (place) return;
            p(-1, 0, -2);
            if (place) return;
            p(0, 0, -2);
            if (place) return;
            p(1, 0, -2);
            if (place) return;
        }

        if(Board3x3byZ .get()) {
            p(-2, 0, -1);
            if (place) return;
            p(-2, 0, 0);
            if (place) return;
            p(-2, 0, 1);
            if (place) return;
            p(2, 0, -1);
            if (place) return;
            p(2, 0, 0);
            if (place) return;
            p(2, 0, 1);
            if (place) return;
        }

        if(Board5x5byX .get()) {
            p(-2, 0, -3);
            if (place) return;
            p(-1, 0, -3);
            if (place) return;
            p(-2, 0, 3);
            if (place) return;
            p(-1, 0, 3);
            if (place) return;
            p(0, 0, -3);
            if (place) return;
            p(0, 0, 3);
            if (place) return;
            p(1, 0, -3);
            if (place) return;
            p(2, 0, -3);
            if (place) return;
            p(0, 0, 3);
            if (place) return;
            p(1, 0, 3);
            if (place) return;
            p(2, 0, 3);
            if (place) return;
        }

        if(Board5x5byZ .get()) {
            p(3, 0, -2);
            if (place) return;
            p(3, 0, -1);
            if (place) return;
            p(-3, 0, -2);
            if (place) return;
            p(-3, 0, -1);
            if (place) return;
            p(-3, 0, 0);
            if (place) return;
            p(3, 0, 0);
            if (place) return;
            p(-3, 0, 1);
            if (place) return;
            p(-3, 0, 2);
            if (place) return;
            p(3, 0, 1);
            if (place) return;
            p(3, 0, 2);
            if (place) return;
        }

        switch (buildMod.get()) {
            case Platform3x3 -> {
                p(0, -1, 0);
                if (place) return;
                p(1, -1, 0);
                if (place) return;
                p(-1, -1, 0);
                if (place) return;
                p(0, -1, 1);
                if (place) return;
                p(0, -1, -1);
                if (place) return;
                p(1, -1, 1);
                if (place) return;
                p(1, -1, -1);
                if (place) return;
                p(-1, -1, 1);
                if (place) return;
                p(-1, -1, -1);
                if (place) return;
                if (togg.get()) {
                    ChatUtils.info("Done");
                    place = false;
                    toggle();
                }
            }
            case Platform5x5 -> {
                p(0, -1, 0);
                if (place) return;
                p(-1, -1, 0);
                if (place) return;
                p(-2, -1, 0);
                if (place) return;
                p(1, -1, 0);
                if (place) return;
                p(2, -1, 0);
                if (place) return;
                p(0, -1, -1);
                if (place) return;
                p(0, -1, -2);
                if (place) return;
                p(0, -1, 1);
                if (place) return;
                p(0, -1, 2);
                if (place) return;
                p(-1, -1, -1);
                if (place) return;
                p(1, -1, 1);
                if (place) return;
                p(-2, -1, -2);
                if (place) return;
                p(2, -1, 2);
                if (place) return;
                p(1, -1, -1);
                if (place) return;
                p(-1, -1, 1);
                if (place) return;
                p(2, -1, -2);
                if (place) return;
                p(-2, -1, 2);
                if (place) return;
                p(2, -1, 1);
                if (place) return;
                p(2, -1, -1);
                if (place) return;
                p(-2, -1, 1);
                if (place) return;
                p(-2, -1, -1);
                if (place) return;
                p(1, -1, 2);
                if (place) return;
                p(-1, -1, 2);
                if (place) return;
                p(1, -1, -2);
                if (place) return;
                p(-1, -1, -2);
                if (place) return;
                if (togg.get()) {
                    ChatUtils.info("Done");
                    place = false;
                    toggle();
                }
            }
        }
    }

    public Platforms() {
        super(Addon.misc, "platforms", "for roads");
    }

    private boolean p(int x, int y, int z) {
        clearBlockPosition(x, y, z);BlockState blockState = mc.world.getBlockState(blockPos);
        if (!blockState.getMaterial().isReplaceable()) return true;
        if (BlockUtils.place(blockPos, InvUtils.findInHotbar(itemStack -> blocks.get().contains(Block.getBlockFromItem(itemStack.getItem()))), rotate.get(), 100, checkEntity.get())) {
            place = true;
        }
        return false;
    }
     private void clearBlockPosition(int x, int y, int z) {blockPos.set(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);}
}

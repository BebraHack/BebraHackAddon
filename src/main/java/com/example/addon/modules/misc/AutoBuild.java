package com.example.addon.modules.misc;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
public class AutoBuild extends Module {
    public enum build {Portal, Penis, heart, Bunker, Platform, Wither, Swastika, Highway;}
    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean place, toggle;
    private final SettingGroup sgMisc = settings.createGroup("Misc");
    private final Setting<AutoBuild.build> buildMod = sgMisc.add(new EnumSetting.Builder<AutoBuild.build>().name("build").description(" ").defaultValue(AutoBuild.build.Penis).build());
    private final Setting<Boolean> rotate = sgMisc.add(new BoolSetting.Builder().name("rotate").description(" ").defaultValue(true).build());
    private final Setting<Boolean> togg = sgMisc.add(new BoolSetting.Builder().name("auto-toggle").description(" ").defaultValue(true).build());

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        place = false;
        switch(buildMod.get()) {
            case Penis -> {
                p(2, 0, 0);
                if (place) return;
                p(2, 0, -1);
                if (place) return;
                p(2, 0, 1);
                if (place) return;
                p(2, 1, 0);
                if (place) return;
                p(2, 2, 0);
                if (place) return;
                p(2, 3, 0);
                if (place) return;
                if (togg.get()) {
                    ChatUtils.info("Done");
                    place = false;
                    toggle();
                }
            }
            case heart -> {
                p(2, 0, 0);
                if (place) return;
                p(2, 1, 0);
                if (place) return;
                p(2, 1, 1);
                if (place) return;
                p(2, 1, -1);
                if (place) return;
                p(2, 2, 0);
                if (place) return;
                p(2, 2, 1);
                if (place) return;
                p(2, 2, -1);
                if (place) return;
                p(2, 2, -2);
                if (place) return;
                p(2, 2, 2);
                if (place) return;
                p(2, 3, -2);
                if (place) return;
                p(2, 3, -1);
                if (place) return;
                p(2, 3, 2);
                if (place) return;
                p(2, 3, 1);
                if (place) return;
                if (togg.get()) {
                    ChatUtils.info("Done");
                    place = false;
                    toggle();
                }
            }

            case Platform -> {
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

            case Portal -> {
                p(2, 0, 0);
                if (place) return;
                p(2, 0, 1);
                if (place) return;
                p(2, 1, 2);
                if (place) return;
                p(2, 2, 2);
                if (place) return;
                p(2, 3, 2);
                if (place) return;
                p(2, 4, 1);
                if (place) return;
                p(2, 4, 0);
                if (place) return;
                p(2, 3, -1);
                if (place) return;
                p(2, 2, -1);
                if (place) return;
                p(2, 1, -1);
                if (place) return;
                if (togg.get()) {
                    ChatUtils.info("Done");
                    place = false;
                    toggle();
                }
            }

            case Bunker -> {
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
                p(1, 0, 0);
                if (place) return;
                p(-1, 0, 0);
                if (place) return;
                p(0, 0, 1);
                if (place) return;
                p(0, 0, -1);
                if (place) return;
                p(-1, 1, 0);
                if (place) return;
                p(1, 1, 0);
                if (place) return;
                p(0, 1, 1);
                if (place) return;
                p(0, 1, -1);
                if (place) return;
                p(1, 2, 0);
                if (place) return;
                p(-1, 2, 0);
                if (place) return;
                p(0, 2, 1);
                if (place) return;
                p(0, 2, -1);
                if (place) return;
                p(0, 2, 0);
                if (place) return;
                p(0, 3, 0);
                if (place) return;
                p(0, 4, 0);
                if (place) return;
                p(1, 3, 0);
                if (place) return;
                p(0, 3, 1);
                if (place) return;
                p(0, 3, -1);
                if (place) return;
                p(-1, 3, 0);
                if (place) return;
                p(2, 0, 0);
                if (place) return;
                p(-2, 0, 0);
                if (place) return;
                p(0, 0, 2);
                if (place) return;
                p(0, 0, -2);
                if (place) return;
                p(1, 0, 1);
                if (place) return;
                p(1, 0, -1);
                if (place) return;
                p(-1, 0, 1);
                if (place) return;
                p(2, 1, 0);
                if (place) return;
                p(-2, 1, 0);
                if (place) return;
                p(0, 1, 2);
                if (place) return;
                p(0, 1, -2);
                if (place) return;
                p(1, 1, 1);
                if (place) return;
                p(1, 1, -1);
                if (place) return;
                p(-1, 1, 1);
                if (place) return;
                p(-1, 1, -1);
                if (togg.get()) {
                    ChatUtils.info("Done");
                    place = false;
                    toggle();
                }
            }

            case Wither -> {
                if (place) return;
                b(2, 0, 0);
                if (place) return;
                b(2, 1, 0);
                if (place) return;
                b(2, 1, 1);
                if (place) return;
                b(2, 1, -1);
                if (place) return;
                h(2, 2, 0);
                if (place) return;
                h(2, 2, 1);
                if (place) return;
                h(2, 2, -1);
                if (togg.get()) {
                    ChatUtils.info("Done");
                    place = false;
                    toggle();
                }
            }

            case Swastika -> {
                p(1, 0, 0);
                if (place) return;
                p(1, 0, -1);
                if (place) return;
                p(1, 0, -2);
                if (place) return;
                p(1, 1, 0);
                if (place) return;
                p(1, 2, 0);
                if (place) return;
                p(1, 2, -1);
                if (place) return;
                p(1, 2, -2);
                if (place) return;
                p(1, 3, -2);
                if (place) return;
                p(1, 4, -2);
                if (place) return;
                p(1, 3, 0);
                if (place) return;
                p(1, 4, 0);
                if (place) return;
                p(1, 4, 1);
                if (place) return;
                p(1, 4, 2);
                if (place) return;
                p(1, 2, 1);
                if (place) return;
                p(1, 2, 2);
                if (place) return;
                p(1, 1, 2);
                if (place) return;
                p(1, 0, 2);
                if (place) return;
                if (togg.get()) {
                    ChatUtils.info("Done");
                    place = false;
                    toggle();
                }
            }

            case Highway -> {
                if (place) return;
                p(0, -1, 0);
                if (place) return;
                p(1, -1, 0);
                if (place) return;
                p(0, -1, -1);
                if (place) return;
                p(2, -1, 0);
                if (place) return;
                p(0, -1, -2);
                if (togg.get()) {
                    ChatUtils.info("Done");
                    place = false;
                    toggle();
                }
            }
        }
    }

    public AutoBuild() {super(Addon.misc, "auto-build", " ");}
    private boolean p(int x, int y, int z) {
        clearBlockPosition(x, y, z);BlockState blockState = mc.world.getBlockState(blockPos);
        if (!blockState.getMaterial().isReplaceable()) return true;
        if (BlockUtils.place(blockPos, InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 100)) {
            place = true;
        }
        return false;
    }

    private boolean b(int x, int y, int z) {
        clearBlockPosition(x, y, z);BlockState blockState = mc.world.getBlockState(blockPos);
        if (!blockState.getMaterial().isReplaceable()) return true;
        if (BlockUtils.place(blockPos, InvUtils.findInHotbar(Items.SOUL_SAND), rotate.get(), 100)) {
            place = true;
        }
        return false;
    }

    private boolean h(int x, int y, int z) {
        clearBlockPosition(x, y, z);BlockState blockState = mc.world.getBlockState(blockPos);
        if (!blockState.getMaterial().isReplaceable()) return true;
        if (BlockUtils.place(blockPos, InvUtils.findInHotbar(Items.WITHER_SKELETON_SKULL), rotate.get(), 100)) {
            place = true;
        }
        return false;
    }

    private void clearBlockPosition(int x, int y, int z) {blockPos.set(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);}
}

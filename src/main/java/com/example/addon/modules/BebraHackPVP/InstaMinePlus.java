package com.example.addon.modules.BebraHackPVP;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.entity.player.StartBreakingBlockEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class
InstaMinePlus extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgDelay = settings.createGroup("Delays");
    private final SettingGroup sgRender = settings.createGroup("Render");


    // General
    private final Setting<Boolean> debug = sgGeneral.add(new BoolSetting.Builder()
        .name("debug")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> pick = sgGeneral.add(new BoolSetting.Builder()
        .name("only-pick")
        .description("Only tries to mine the block if you are holding a pickaxe.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> ironPick = sgGeneral.add(new BoolSetting.Builder()
        .name("iron-pick")
        .description("Allows iron pickaxe to count as a pickaxe for instamine.")
        .defaultValue(false)
        .visible(pick::get)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Faces the blocks being mined server side.")
        .defaultValue(true)
        .build()
    );


    // Delays
    private final Setting<Integer> bypassDelay = sgDelay.add(new IntSetting.Builder()
        .name("bypass delay")
        .description("The delay between breaks for bypass mode.")
        .defaultValue(4)
        .min(1)
        .sliderMax(100)
        .build()
    );

    private final Setting<Integer> bypassIterations = sgDelay.add(new IntSetting.Builder()
        .name("bypass iterations")
        .description("The amount of times bypass mode should happen.")
        .defaultValue(0)
        .min(0)
        .sliderMax(20)
        .build()
    );

    private final Setting<Integer> normalDelay = sgDelay.add(new IntSetting.Builder()
        .name("normal delay")
        .description("The delay between breaks for normal mode.")
        .defaultValue(41)
        .min(1)
        .sliderMax(100)
        .build()
    );

    private final Setting<Integer> normalIterations = sgDelay.add(new IntSetting.Builder()
        .name("normal iterations")
        .description("The amount of times bypass mode should happen.")
        .defaultValue(1)
        .min(0)
        .sliderMax(20)
        .build()
    );


    // Render
    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder()
        .name("render")
        .description("Renders a block overlay on the block being broken.")
        .defaultValue(true)
        .build()
    );

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("How the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("The color of the sides of the blocks being rendered.")
        .defaultValue(new SettingColor(204, 0, 0, 10))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The color of the lines of the blocks being rendered.")
        .defaultValue(new SettingColor(204, 0, 0, 255))
        .build()
    );


    public InstaMinePlus() {
        super(Addon.combat, "insta-mine+", "Attempts to bypass instant mine.");
    }


    private int ticksPassed;
    private int ticks;
    private int iteration;

    private boolean updated;

    private final BlockPos.Mutable blockPos = new BlockPos.Mutable(0, -1, 0);
    private Direction direction;


    @Override
    public void onActivate() {
        ticksPassed = 0;
        ticks = 0;
        blockPos.set(0, -1, 0);
        iteration = 0;
        updated = false;
    }

    @EventHandler
    private void onStartBreakingBlock(StartBreakingBlockEvent event) {
        direction = event.direction;
        blockPos.set(event.blockPos);
    }

    private int getDelay() {
        if (bypassIterations.get() >= iteration && iteration > 0) {
            if (debug.get()) warning("Starting bypass iterations");
            return bypassDelay.get();
        } else if (normalIterations.get() >= iteration && iteration > bypassIterations.get()) {
            if (debug.get()) warning("Starting normal iterations");
            return normalDelay.get();
        } else if (iteration > (normalIterations.get() + bypassIterations.get())) {
            if (debug.get()) warning("Restarting iterations");
            iteration = 0;
        }
        return normalDelay.get();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.world.getBlockState(blockPos).isOf(Blocks.AIR) && !updated) {
            iteration++;
            updated = true;
        }

        if (ticksPassed < 1) ticksPassed++;
        else {
            if (!mc.world.getBlockState(blockPos).isOf(Blocks.AIR)) {
                updated = false;
                ticksPassed = 0;
            }
        }

        if (ticks >= getDelay()) {
            ticks = 0;

            if (shouldMine()) {
                if (rotate.get()) {
                    Rotations.rotate(Rotations.getYaw(blockPos), Rotations.getPitch(blockPos), () -> mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, direction)));
                } else {
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, direction));
                }

                mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

            }
        } else {
            ticks++;
        }
    }

    private boolean shouldMine() {
        if (blockPos.getY() == -1) return false;
        if (!BlockUtils.canBreak(blockPos)) return false;
        Item item = mc.player.getMainHandStack().getItem();
        return !pick.get() || (item == Items.DIAMOND_PICKAXE ||item == Items.NETHERITE_PICKAXE || (ironPick.get() && (item == Items.IRON_PICKAXE)));
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (!render.get() || !shouldMine()) return;
        event.renderer.box(blockPos, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }
}

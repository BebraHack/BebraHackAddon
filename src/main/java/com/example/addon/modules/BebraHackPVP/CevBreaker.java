package com.example.addon.modules.BebraHackPVP;

import com.example.addon.Addon;
import com.example.addon.Utils.Task;
import meteordevelopment.meteorclient.events.entity.player.FinishUsingItemEvent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.ClientPlayerInteractionManagerAccessor;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.misc.Vec3;
import meteordevelopment.meteorclient.utils.player.*;
import meteordevelopment.meteorclient.utils.render.NametagUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.meteorclient.utils.world.CardinalDirection;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.addon.Utils.BlockInfo.*;

public class CevBreaker extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");
    private final SettingGroup sgNone = settings.createGroup("");

    private final Setting<Notifications.Mode> notifications = sgNone.add(new EnumSetting.Builder<Notifications.Mode>().name("notifications").defaultValue(Notifications.Mode.Toast).build());

    private final Setting<Integer> targetRange = sgGeneral.add(new IntSetting.Builder().name("target-range").description("The range players can be targeted.").defaultValue(6).sliderRange(0, 7).build());
    private final Setting<Boolean> instant = sgGeneral.add(new BoolSetting.Builder().name("instant").description("Uses instamine exploit.").defaultValue(false).build());
    private final Setting<Double> breakProgress = sgGeneral.add(new DoubleSetting.Builder().name("break-progress").description("Places crystal if break progress of breaking block is higher.").defaultValue(0.95).sliderRange(0, 0.99).visible(() -> !instant.get()).build());
    private final Setting<Boolean> rightClickEat = sgGeneral.add(new BoolSetting.Builder().name("right-click-eat").description("Stops breaking the block and starts eating EGapple.").defaultValue(false).build());
    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder().name("delay").description("Delay for breaking block with instamine exploit.").defaultValue(10).sliderRange(5, 20).visible(instant::get).build());
    private final Setting<Boolean> ninja = sgGeneral.add(new BoolSetting.Builder().name("ninja").description("Places obsidian above target head if its time to place crystal.").defaultValue(false).visible(instant::get).build());
    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder().name("rotate").description("Automatically faces towards the blocks being placed.").defaultValue(false).build());
    private final Setting<Boolean> debug = sgGeneral.add(new BoolSetting.Builder().name("debug").description("Sends info in chat about CEV.").defaultValue(false).build());

    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder().name("render").description("Renders the block where it interacting.").defaultValue(true).build());
    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>().name("shape-mode").defaultValue(ShapeMode.Both).visible(render::get).build());
    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder().name("side-color").description("The side color for positions to be broken.").defaultValue(new SettingColor(255, 0, 170, 35)).visible(() -> render.get() && shapeMode.get() != ShapeMode.Lines).build());
    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder().name("line-color").description("The line color for positions to be broken.").defaultValue(new SettingColor(255, 0, 170)).visible(() -> render.get() && shapeMode.get() != ShapeMode.Sides).build());
    private final Setting<Boolean> renderProgress = sgRender.add(new BoolSetting.Builder().name("render-progress").description("Renders the progress of breaking block.").defaultValue(true).visible(() -> !instant.get()).build());
    private final Setting<Double> scale = sgRender.add(new DoubleSetting.Builder().name("scale").description("The scale of rendered text").defaultValue(1.5).sliderRange(0.01, 3).visible(() -> !instant.get() && renderProgress.get()).build());

    public CevBreaker() {
        super(Addon.combat, "cev-breaker", "");
    }

    private FindItemResult pickaxe, crystal, obsidian, gap;
    private EndCrystalEntity crystalEntity;
    private BlockPos breakPos, prevPos;
    private PlayerEntity target;
    private int ticks = 0;
    private boolean isEating;

    private final Task obsidianTask = new Task();
    private final Task crystalTask = new Task();
    private final Task startTask = new Task();

    @Override
    public void onActivate() {
        ticks = 0;

        breakPos = null;
        prevPos = new BlockPos(0,1,0);
        crystalEntity = null;

        isEating = false;

        startTask.reset();
        reset();
    }

    @Override
    public void onDeactivate() {
        if (breakPos != null) mc.interactionManager.attackBlock(breakPos, Direction.UP);
    }

    @EventHandler
    public void onEat(FinishUsingItemEvent event) {
        if (isEating) {
            mc.options.useKey.setPressed(false);
            isEating = false;
        }
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        assert mc.player != null && mc.world != null && mc.interactionManager != null;

        pickaxe = InvUtils.findInHotbar(Items.NETHERITE_PICKAXE, Items.DIAMOND_PICKAXE);
        crystal = InvUtils.findInHotbar(Items.END_CRYSTAL);
        obsidian = InvUtils.findInHotbar(Items.OBSIDIAN);

        target = TargetUtils.getPlayerTarget(targetRange.get(), SortPriority.LowestDistance);
        if (TargetUtils.isBadTarget(target, targetRange.get())) {
            Notifications.send("Target is null", notifications);
            toggle();
            return;
        }

        if (!pickaxe.found() || !crystal.found() || !obsidian.found()) {
            ChatUtils.info("Items: " + (!pickaxe.found() ? Formatting.RED + "pickaxe, " : Formatting.GREEN + "pickaxe, ") + (!crystal.found() ? Formatting.RED + "crystal, " : Formatting.GREEN + "crystal, ") + (!obsidian.found() ? Formatting.RED + "obsidian. " : Formatting.GREEN + "obsidian. "));
            toggle();
            return;
        }

        gap = InvUtils.find(Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE);
        if (rightClickEat.get() && mc.options.useKey.isPressed() && gap.found()) isEating = true;

        if (isEating) {
            mc.player.getInventory().selectedSlot = gap.slot();
            mc.options.useKey.setPressed(true);
            return;
        }

        if (((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getBreakingProgress() <= 0.01) breakPos = validPos(target);

        if (breakPos == null) {
            Notifications.send("Position is null", notifications);
            toggle();
            return;
        }

        BlockPos crystalPos = target.getBlockPos().up().up();
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof EndCrystalEntity) {
                BlockPos entityPos = entity.getBlockPos();

                for (Direction direction : Direction.values()) {
                    if (direction.equals(Direction.DOWN)) continue;

                    if (entityPos.equals(crystalPos.offset(direction))) crystalEntity = (EndCrystalEntity) entity;
                }
            }
        }

        if (instant.get()) {
            if (!ninja.get()) {
                obsidianTask.run(() -> {
                    if (debug.get()) info("obsidian placed");
                    BlockUtils.place(breakPos, obsidian, 50, rotate.get());
                });
            }

            if (mc.world.getBlockState(breakPos).getBlock() == Blocks.OBSIDIAN) {
                mc.player.getInventory().selectedSlot = pickaxe.slot();
                if (!prevPos.equals(breakPos)) {
                    if (debug.get()) info("reset");
                    startTask.reset();
                }
                startTask.run(() -> {
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, Direction.UP));
                    prevPos = breakPos;
                    if (debug.get()) info(prevPos.toShortString() + " = " + breakPos.toShortString());
                });
            }

            if (startTask.isCalled()) {
                ticks++;
                if (ticks == delay.get() - 3 && ninja.get()) {
                    obsidianTask.run(() -> {
                        if (debug.get()) info("obsidian placed");
                        BlockUtils.place(breakPos, obsidian, 50, rotate.get());
                    });
                }
                if (ticks == delay.get() - 2) {
                    crystalTask.run(() -> {
                        if (debug.get()) info("crystal placed");
                        int prevSlot = mc.player.getInventory().selectedSlot;
                        if (mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL) mc.player.getInventory().selectedSlot = crystal.slot();

                        mc.interactionManager.interactBlock(mc.player, crystal.getHand(), new BlockHitResult(mc.player.getPos(), Direction.DOWN, breakPos, true));
                        mc.player.getInventory().selectedSlot = prevSlot;
                    });
                }
                if (ticks == delay.get() - 1) {
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, Direction.UP));
                }
                if (ticks == delay.get()) {
                    if (crystalEntity != null && getState(breakPos).isAir()) {
                        if (debug.get()) info("crystal attacked");
                        mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(crystalEntity, mc.player.isSneaking()));
                    }
                    reset();
                    ticks = 0;
                }
            }
        } else {
            if (!prevPos.equals(breakPos)) {
                if (debug.get()) info("reset");
                obsidianTask.reset();
            }
            obsidianTask.run(() -> {
                if (debug.get()) info("obsidian placed");
                BlockUtils.place(breakPos, obsidian, 50, rotate.get());
                prevPos = breakPos;
                if (debug.get()) info(prevPos.toShortString() + " = " + breakPos.toShortString());
            });

            if (mc.world.getBlockState(breakPos).getBlock() == Blocks.OBSIDIAN) {
                mc.player.getInventory().selectedSlot = pickaxe.slot();
                mc.interactionManager.updateBlockBreakingProgress(breakPos, Direction.UP);

                float progress = ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getBreakingProgress();
                if (progress < breakProgress.get()) return;

                crystalTask.run(() -> {
                    if (debug.get()) info("crystal placed");
                    int prevSlot = mc.player.getInventory().selectedSlot;
                    if (mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL) mc.player.getInventory().selectedSlot = crystal.slot();
                    mc.interactionManager.interactBlock(mc.player, crystal.getHand(), new BlockHitResult(mc.player.getPos(), Direction.DOWN, breakPos, true));
                    mc.player.getInventory().selectedSlot = prevSlot;
                });
            }

            if (crystalEntity != null && getState(breakPos).isAir()) {
                if (debug.get()) info("crystal attacked");
                mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(crystalEntity, mc.player.isSneaking()));
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakPos, Direction.UP));
                reset();
            }
        }
    }

    private BlockPos validPos(PlayerEntity player) {
        if (player == null) return null;

        BlockPos pos = player.getBlockPos();
        if (getState(pos.up(3)).isAir() && (getState(pos.up(2)).isOf(Blocks.OBSIDIAN) || getState(pos.up(2)).isOf(Blocks.AIR))) return pos.up(2);
        List<BlockPos> posList = new ArrayList<>();

        for (CardinalDirection direction : CardinalDirection.values()) {
            if (getState(pos.offset(direction.toDirection()).up(2)).isAir() && (getState(pos.offset(direction.toDirection()).up(1)).isOf(Blocks.OBSIDIAN) || getState(pos.offset(direction.toDirection()).up(1)).isOf(Blocks.AIR))) posList.add(pos.offset(direction.toDirection()).up(1));
        }

        posList.sort(Comparator.comparingDouble(PlayerUtils::distanceTo));
        return posList.isEmpty() ? null : posList.get(0);
    }

    @EventHandler
    public void on3DRender(Render3DEvent event) {
        if (breakPos == null || !render.get()) return;

        event.renderer.box(breakPos, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }

    @EventHandler
    public void on2DRender(Render2DEvent event) {
        if (breakPos == null || !renderProgress.get() || instant.get()) return;

        Vec3 pos = new Vec3(breakPos.getX() + 0.5, breakPos.getY() + 0.5, breakPos.getZ() + 0.5);
        if (NametagUtils.to2D(pos, scale.get())) {
            String progress;
            NametagUtils.begin(pos);
            TextRenderer.get().begin(1.0, false, true);
            progress = String.format("%.2f", ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getBreakingProgress()) + "%";
            if (progress.equals("0.00%")) progress = "";
            TextRenderer.get().render(progress, -TextRenderer.get().getWidth(progress) / 2.0, 0.0, (crystalEntity == null ? new Color(255, 255, 255, 255) : new Color(106, 255, 78, 255)));
            TextRenderer.get().end();
            NametagUtils.end();
        }
    }

    private void reset() {
        obsidianTask.reset();
        crystalTask.reset();
        crystalEntity = null;
    }

    @Override
    public String getInfoString() {
        return target != null ? target.getGameProfile().getName() : null; // adds target name to the module array list
    }
}
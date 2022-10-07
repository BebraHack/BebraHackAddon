package com.example.addon.modules.BebraHackPVP;

import com.example.addon.Utils.BEntityUtils;
import com.example.addon.Utils.BWorldUtils;
import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.meteor.KeyEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.AbstractBlockAccessor;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Burrow extends Module {
    public enum Mode {
        Normal,
        Smart
    }

    public enum RubberbandDirection {
        Up,
        Down
    }

    public enum CenterMode {
        Snap,
        Center,
        None
    }

    public enum Block {
        EChest,
        Obsidian,
        Anvil,
        AncientDebris,
        Netherite,
        Anchor,
        EnchantingTable,
        Held
    }


    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgPlacing = settings.createGroup("Placing");

    // General
    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("burrow-mode")
        .description("How the module should function.")
        .defaultValue(Mode.Normal)
        .build()
    );

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("smart-range")
        .description("How close a player should get to burrow.")
        .defaultValue(2.5)
        .range(1,5)
        .sliderRange(1,5)
        .visible(() -> mode.get() == Mode.Smart)
        .build()
    );

    private final Setting<Block> block = sgGeneral.add(new EnumSetting.Builder<Block>()
        .name("block")
        .description("The block to use for Burrow.")
        .defaultValue(Block.Obsidian)
        .build()
    );

    private final Setting<Block> fallbackBlock = sgGeneral.add(new EnumSetting.Builder<Block>()
        .name("fallback-block")
        .description("The fallback block to use for Burrow.")
        .defaultValue(Block.EChest)
        .build()
    );

    private final Setting<Boolean> onlyInHole = sgGeneral.add(new BoolSetting.Builder()
        .name("only-in-holes")
        .description("Stops you from burrowing when not in a hole.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> onlyOnGround = sgGeneral.add(new BoolSetting.Builder()
        .name("only-on-ground")
        .description("Stops you from burrowing when not on the ground.")
        .defaultValue(true)
        .build()
    );


    // Placing
    private final Setting<CenterMode> centerMode = sgPlacing.add(new EnumSetting.Builder<CenterMode>()
        .name("center")
        .description("How it should center you before burrowing.")
        .defaultValue(CenterMode.Center)
        .build()
    );

    private final Setting<Boolean> rotate = sgPlacing.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Faces the block you place server-side.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> instant = sgPlacing.add(new BoolSetting.Builder()
        .name("instant")
        .description("Jumps with packets rather than vanilla jump.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> automatic = sgPlacing.add(new BoolSetting.Builder()
        .name("automatic")
        .description("Automatically burrows on activate rather than waiting for jump.")
        .defaultValue(true)
        .visible(() -> mode.get() == Mode.Normal)
        .build()
    );

    private final Setting<Double> triggerHeight = sgPlacing.add(new DoubleSetting.Builder()
        .name("trigger-height")
        .description("How high you have to jump before a rubberband is triggered.")
        .defaultValue(1.12)
        .range(0.01, 1.4)
        .sliderRange(0.01, 1.4)
        .build()
    );

    private final Setting<RubberbandDirection> rubberbandDirection = sgPlacing.add(new EnumSetting.Builder<RubberbandDirection>()
        .name("rubberband-direction")
        .description("Which direction to rubberband you when your are burrowing.")
        .defaultValue(RubberbandDirection.Up)
        .build()
    );

    private final Setting<Integer> minRubberbandHeight = sgPlacing.add(new IntSetting.Builder()
        .name("min-height")
        .description("Min height to rubberband.")
        .defaultValue(3)
        .min(2)
        .sliderRange(2,30)
        .build()
    );

    private final Setting<Integer> maxRubberbandHeight = sgPlacing.add(new IntSetting.Builder()
        .name("max-height")
        .description("Max height to rubberband.")
        .defaultValue(7)
        .min(2)
        .sliderRange(2,30)
        .build()
    );

    private final Setting<Double> timer = sgPlacing.add(new DoubleSetting.Builder()
        .name("timer")
        .description("Timer override.")
        .defaultValue(1)
        .min(0.01)
        .sliderRange(0.01, 10)
        .build()
    );


    public Burrow() {
        super(Addon.combat, "burrow", "Attempts to clip you into a block.");
    }


    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean shouldBurrow;


    @Override
    public void onActivate() {
        if (onlyOnGround.get() && !mc.player.isOnGround()) {
            error("Not on the ground, disabling.");
            toggle();
            return;
        }

        if (!mc.world.getBlockState(BWorldUtils.roundBlockPos(mc.player.getPos())).getMaterial().isReplaceable()) {
            error("Already burrowed, disabling.");
            toggle();
            return;
        }

        if (!BEntityUtils.isInHole(mc.player, true, BEntityUtils.BlastResistantType.Any) && onlyInHole.get()) {
            error("Not in a hole, disabling.");
            toggle();
            return;
        }

        if (!checkHead()) {
            error("Not enough headroom to burrow, disabling.");
            toggle();
            return;
        }

        FindItemResult result = getItem();
        if (!result.isHotbar() && !result.isOffhand()) result = getFallbackItem();

        if (!result.isHotbar() && !result.isOffhand()) {
            error("No burrow block found, disabling.");
            toggle();
            return;
        }

        blockPos.set(BWorldUtils.roundBlockPos(mc.player.getPos()));

        Modules.get().get(Timer.class).setOverride(this.timer.get());

        shouldBurrow = false;

        if (automatic.get()) {
            if (instant.get()) shouldBurrow = true;
            else mc.player.jump();
        } else {
            info("Waiting for manual jump.");
        }
    }

    @Override
    public void onDeactivate() {
        Modules.get().get(Timer.class).setOverride(Timer.OFF);
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mode.get() == Mode.Smart) {
            if (TargetUtils.getPlayerTarget(range.get(), SortPriority.LowestDistance) != null) burrow();
        } else {
            if (!instant.get()) shouldBurrow = mc.player.getY() > blockPos.getY() + triggerHeight.get();
            if (!shouldBurrow && instant.get()) blockPos.set(BWorldUtils.roundBlockPos(mc.player.getPos()));


            if (shouldBurrow) {
                if (rotate.get())
                    Rotations.rotate(Rotations.getYaw(BWorldUtils.roundBlockPos(mc.player.getPos())), Rotations.getPitch(BWorldUtils.roundBlockPos(mc.player.getPos())), 50, this::burrow);
                else burrow();

                toggle();
            }
        }
    }

    @EventHandler
    private void onKey(KeyEvent event) {
        if (instant.get() && !shouldBurrow) {
            if (event.action == KeyAction.Press && mc.options.jumpKey.matchesKey(event.key, 0)) {
                shouldBurrow = true;
            }
            blockPos.set(BWorldUtils.roundBlockPos(mc.player.getPos()));
        }
    }

    private void burrow() {
        FindItemResult block = getItem();

        if ((!block.isHotbar() && !block.isOffhand())) block = getFallbackItem();

        if (!(mc.player.getInventory().getStack(block.slot()).getItem() instanceof BlockItem)) return;

        if (centerMode.get() == CenterMode.Snap) BWorldUtils.snapPlayer(BWorldUtils.roundBlockPos(mc.player.getPos()));
        else if (centerMode.get() == CenterMode.Center) PlayerUtils.centerPlayer();

        if (instant.get()) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.4, mc.player.getZ(), false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.75, mc.player.getZ(), false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.01, mc.player.getZ(), false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.15, mc.player.getZ(), false));
        }

        InvUtils.swap(block.slot(), true);

        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(Utils.vec3d(blockPos), Direction.UP, blockPos, false));
        mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

        InvUtils.swapBack();

        if (instant.get()) {
            if (rubberbandDirection.get() == RubberbandDirection.Up) {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + upperSpace(), mc.player.getZ(), false));
            } else {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + lowerSpace(), mc.player.getZ(), false));
            }
        } else {
            if (rubberbandDirection.get() == RubberbandDirection.Up) {
                mc.player.updatePosition(mc.player.getX(), mc.player.getY() + upperSpace(), mc.player.getZ());
            } else {
                mc.player.updatePosition(mc.player.getX(), mc.player.getY() + upperSpace(), mc.player.getZ());
            }
        }

        if (mode.get() == Mode.Smart) toggle();
        info("bruh");
    }

    private FindItemResult getItem() {
        return switch (block.get()) {
            case EChest -> InvUtils.findInHotbar(Items.ENDER_CHEST);
            case Anvil -> InvUtils.findInHotbar(itemStack -> net.minecraft.block.Block.getBlockFromItem(itemStack.getItem()) instanceof AnvilBlock);
            case AncientDebris -> InvUtils.findInHotbar(Items.ANCIENT_DEBRIS);
            case Netherite -> InvUtils.findInHotbar(Items.NETHERITE_BLOCK);
            case Anchor -> InvUtils.findInHotbar(Items.RESPAWN_ANCHOR);
            case EnchantingTable -> InvUtils.findInHotbar(Items.ENCHANTING_TABLE);
            case Held -> new FindItemResult(mc.player.getInventory().selectedSlot, mc.player.getMainHandStack().getCount());
            default -> InvUtils.findInHotbar(Items.OBSIDIAN, Items.CRYING_OBSIDIAN);
        };
    }

    private FindItemResult getFallbackItem() {
        return switch (fallbackBlock.get()) {
            case EChest -> InvUtils.findInHotbar(Items.ENDER_CHEST);
            case Anvil -> InvUtils.findInHotbar(itemStack -> net.minecraft.block.Block.getBlockFromItem(itemStack.getItem()) instanceof AnvilBlock);
            case AncientDebris -> InvUtils.findInHotbar(Items.ANCIENT_DEBRIS);
            case Netherite -> InvUtils.findInHotbar(Items.NETHERITE_BLOCK);
            case Anchor -> InvUtils.findInHotbar(Items.RESPAWN_ANCHOR);
            case EnchantingTable -> InvUtils.findInHotbar(Items.ENCHANTING_TABLE);
            case Held -> new FindItemResult(mc.player.getInventory().selectedSlot, mc.player.getMainHandStack().getCount());
            default -> InvUtils.findInHotbar(Items.OBSIDIAN, Items.CRYING_OBSIDIAN);
        };
    }

    private boolean checkHead() {
        BlockState blockState1 = mc.world.getBlockState(blockPos.set(mc.player.getX() + .3, mc.player.getY() + 2.3, mc.player.getZ() + .3));
        BlockState blockState2 = mc.world.getBlockState(blockPos.set(mc.player.getX() + .3, mc.player.getY() + 2.3, mc.player.getZ() - .3));
        BlockState blockState3 = mc.world.getBlockState(blockPos.set(mc.player.getX() - .3, mc.player.getY() + 2.3, mc.player.getZ() - .3));
        BlockState blockState4 = mc.world.getBlockState(blockPos.set(mc.player.getX() - .3, mc.player.getY() + 2.3, mc.player.getZ() + .3));
        boolean air1 = blockState1.getMaterial().isReplaceable();
        boolean air2 = blockState2.getMaterial().isReplaceable();
        boolean air3 = blockState3.getMaterial().isReplaceable();
        boolean air4 = blockState4.getMaterial().isReplaceable();
        return air1 & air2 & air3 & air4;
    }

    private int upperSpace() {
        for (int dy = minRubberbandHeight.get(); dy <= maxRubberbandHeight.get(); dy++) {
            BlockState blockState1 = mc.world.getBlockState(BWorldUtils.roundBlockPos(mc.player.getPos()).up(dy));
            BlockState blockState2 = mc.world.getBlockState(BWorldUtils.roundBlockPos(mc.player.getPos()).up(dy + 1));

            boolean air1 = !((AbstractBlockAccessor)blockState1.getBlock()).isCollidable();
            boolean air2 = !((AbstractBlockAccessor)blockState2.getBlock()).isCollidable();

            if (air1 & air2) return dy;
        }

        return 0;
    }

    private int lowerSpace() {
        for (int dy = -minRubberbandHeight.get(); dy >= -maxRubberbandHeight.get(); dy--) {
            BlockState blockState1 = mc.world.getBlockState(BWorldUtils.roundBlockPos(mc.player.getPos()).up(dy));
            BlockState blockState2 = mc.world.getBlockState(BWorldUtils.roundBlockPos(mc.player.getPos()).up(dy + 1));

            boolean air1 = !((AbstractBlockAccessor)blockState1.getBlock()).isCollidable();
            boolean air2 = !((AbstractBlockAccessor)blockState2.getBlock()).isCollidable();

            if (air1 & air2) return dy;
        }

        return 0;
    }

    @Override
    public String getInfoString() {
        if (mode.get() == Mode.Smart) return "Standby";
        return null;
    }
}

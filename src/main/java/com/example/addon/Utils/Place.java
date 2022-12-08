package com.example.addon.Utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import meteordevelopment.meteorclient.mixininterface.IClientPlayerInteractionManager;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.Rotations;
import net.minecraft.block.*;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Place {
    // General
    public static void place(BlockPosX bp, FindItemResult itemResult, Swap swap, boolean packet, boolean rotate, boolean oldPlace) {
        if (oldPlace) {
            place$Old(bp, itemResult, swap, packet, rotate);
        } else place$New(bp, itemResult, swap, packet, rotate);
    }

    // 1.13+ place
    public static void place$New(BlockPosX bp, FindItemResult itemResult, Swap swap, boolean packet, boolean rotate) {
        place$New(bp, itemResult.slot(), swap, packet, rotate);
    }

    public static void place$New(BlockPosX bp, int slot, Swap swap, boolean packet, boolean rotate) {
        if (bp == null || slot == -1) return;
        if (!canPlace$New(bp)) return;

        int prevSlot = mc.player.getInventory().selectedSlot;
        Direction direction = placeDirection(bp);
        Hand hand = slot == 44 ? Hand.OFF_HAND : Hand.MAIN_HAND;

        boolean found = direction != null;
        if (found) {
            BlockPosX offset = bp.offset(direction);
            if (canPlace$Old(offset)) {
                if (rotate) Rotations.rotate(Rotations.getYaw(bp.get()), Rotations.getPitch(bp.get()));
                place$Normal(bp, slot, swap, packet, hand);
                return;
            }

            BlockHitResult hitResult = new BlockHitResult(offset.closestVec3d(), direction.getOpposite(), offset.get(), true);
            if (rotate) Rotations.rotate(Rotations.getYaw(offset.get()), Rotations.getPitch(offset.get()));

            switch (swap) {
                case Normal, Silent -> {
                    swap(slot);
                    place(hand, hitResult, packet, true);
                }
                case Move -> move(slot, () -> place(hand, hitResult, packet, true));
            }
        } else {
            if (rotate) Rotations.rotate(Rotations.getYaw(bp.get()), Rotations.getPitch(bp.get()));
            place$Normal(bp, slot, swap, packet, hand);
        }

        if (swap == Swap.Silent) swap(prevSlot);
    }

    private static void place$Normal(BlockPosX bp, int slot, Swap swap, boolean packet, Hand hand) {
        Direction direction = mc.player.getY() > bp.y() ? Direction.UP : Direction.DOWN;
        BlockHitResult hitResult = new BlockHitResult(bp.center(), direction, bp.get(), false);

        switch (swap) {
            case Normal, Silent -> {
                swap(slot);
                place(hand, hitResult, packet, false);
            }
            case Move -> move(slot, () -> place(hand, hitResult, packet, false));
        }
    }

    // 1.12 place
    public static void place$Old(BlockPosX bp, FindItemResult itemResult, Swap swap, boolean packet, boolean rotate) {
        place$Old(bp, itemResult.slot(), swap, packet, rotate);
    }

    public static void place$Old(BlockPosX bp, int slot, Swap swap, boolean packet, boolean rotate) {
        if (bp == null || slot == -1) return;
        if (!canPlace$Old(bp)) return;

        int prevSlot = mc.player.getInventory().selectedSlot;
        Direction direction = placeDirection(bp);
        if (direction == null) return;

        BlockPosX offset = bp.offset(direction);
        if (canPlace$Old(offset)) return;
        boolean shouldSneak = clickable(offset);

        Hand hand = slot == 44 ? Hand.OFF_HAND : Hand.MAIN_HAND;
        BlockHitResult hitResult = new BlockHitResult(offset.closestVec3d(), direction.getOpposite(), offset.get(), true);

        if (rotate) Rotations.rotate(Rotations.getYaw(offset.get()), Rotations.getPitch(offset.get()));
        switch (swap) {
            case Normal, Silent -> {
                swap(slot);
                place(hand, hitResult, packet, shouldSneak);
            }
            case Move -> move(slot, () -> place(hand, hitResult, packet, shouldSneak));
        }

        if (swap == Swap.Silent) swap(prevSlot);
    }

    // Misc
    // Returns direction of the closest block to bp;
    // Example: returns UP = bp.offset(UP), direction (opposite) DOWN;
    public static Direction placeDirection(BlockPosX bp) {
        double distance = 999.0;
        Direction direction = null;

        Direction clickable = null;
        for (Direction dir : Direction.values()) {
            BlockPosX cbp = bp.offset(dir);

            if (cbp.air() || cbp.replaceable() || cbp.fluid()) continue;
            if (clickable(cbp)) {
                clickable = dir;
                continue;
            }

            if (cbp.distance() < distance) {
                distance = cbp.distance();
                direction = dir;
            }
        }

        return direction == null ? clickable : direction;
    }

    private static void place(Hand hand, BlockHitResult hitResult, boolean packet, boolean sneak) {
        boolean shouldSneak = sneak && !mc.player.isSneaking();
        if (shouldSneak) {
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
            if (mc.player.getPose() == EntityPose.CROUCHING) mc.player.setPose(EntityPose.STANDING);
        }

        if (packet) {
            mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(hand, hitResult, 0));
            mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(hand));
        } else {
            mc.interactionManager.interactBlock(mc.player, hand, hitResult);
            mc.player.swingHand(hand);
        }

        if (shouldSneak) mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
    }

//    public static boolean canPlace(BlockPosX bp, Direction direction, boolean oldPlace) {
//        if (oldPlace && bp.offset(direction).down().air()) return false;
//
//        return canPlace(bp);
//    }

    private static boolean canPlace$New(BlockPosX bp) {
        if (!World.isValid(bp.get())) return false;

        return !bp.hasEntity() && (bp.replaceable() || bp.fluid());
    }

    private static boolean canPlace$Old(BlockPosX bp) {
        if (!World.isValid(bp.get())) return false;

        return !bp.hasEntity() && bp.replaceable();
    }

    public static boolean clickable(BlockPosX bp) {
        return bp.of(CraftingTableBlock.class)
            || bp.of(FurnaceBlock.class)
            || bp.of(AnvilBlock.class)
            || bp.of(AbstractButtonBlock.class)
            || bp.of(BlockWithEntity.class)
            || bp.of(BedBlock.class)
            || bp.of(FenceGateBlock.class)
            || bp.of(DoorBlock.class)
            || bp.of(NoteBlock.class)
            || bp.of(TrapdoorBlock.class)
            || bp.of(ChestBlock.class)
            || bp.of(Blocks.LODESTONE, Blocks.ENCHANTING_TABLE, Blocks.RESPAWN_ANCHOR, Blocks.LEVER, Blocks.BLAST_FURNACE, Blocks.BREWING_STAND, Blocks.LOOM, Blocks.CARTOGRAPHY_TABLE, Blocks.SMITHING_TABLE, Blocks.SMOKER, Blocks.STONECUTTER, Blocks.GRINDSTONE);
    }

    // Swap
    private static void swap(FindItemResult itemResult) {
        swap(itemResult.slot());
    }

    private static void swap(int slot) {
        int currentSlot = mc.player.getInventory().selectedSlot;
        if (currentSlot == slot) return;
        if (slot < 0 || slot > 8) return;

        mc.player.getInventory().selectedSlot = slot;
        ((IClientPlayerInteractionManager) mc.interactionManager).syncSelected();
    }

    private static void move(FindItemResult itemResult, Runnable runnable) {
        move(itemResult.slot(), runnable);
    }

    private static void move(int slot, Runnable runnable) {
        if (slot == -1 || slot == 44) {
            runnable.run();
            return;
        }

        move(mc.player.getInventory().selectedSlot, slot);
        runnable.run();
        move(mc.player.getInventory().selectedSlot, slot);
    }

    private static void move(int from, int to) {
        ScreenHandler handler = mc.player.currentScreenHandler;

        Int2ObjectArrayMap<ItemStack> stack = new Int2ObjectArrayMap<>();
        stack.put(to, handler.getSlot(to).getStack());

        mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(handler.syncId, handler.getRevision(), PlayerInventory.MAIN_SIZE + from, to, SlotActionType.SWAP, handler.getCursorStack().copy(), stack));
    }


    public enum Swap {
        Normal, Silent, Move, OFF
    }
}

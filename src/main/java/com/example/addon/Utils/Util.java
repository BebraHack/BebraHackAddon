package com.example.addon.Utils;

import com.example.addon.Utils.BlockPosX;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Util {
    public static List<BlockPosX> getSphere(PlayerEntity player, double radius) {
        Vec3d eyePos = new Vec3d(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ());
        ArrayList<BlockPosX> blocks = new ArrayList<>();

        for (double i = eyePos.getX() - radius; i < eyePos.getX() + radius; i++) {
            for (double j = eyePos.getY() - radius; j < eyePos.getY() + radius; j++) {
                for (double k = eyePos.getZ() - radius; k < eyePos.getZ() + radius; k++) {
                    Vec3d vecPos = new Vec3d(i, j, k);

                    // Closest Vec3d
                    double x = MathHelper.clamp(eyePos.getX() - vecPos.getX(), 0.0, 1.0);
                    double y = MathHelper.clamp(eyePos.getY() - vecPos.getY(), 0.0, 1.0);
                    double z = MathHelper.clamp(eyePos.getZ() - vecPos.getZ(), 0.0, 1.0);
                    Vec3d vec3d = new Vec3d(eyePos.getX() + x, eyePos.getY() + y, eyePos.getZ() + z);

                    // Distance to Vec3d
                    float f = (float) (eyePos.getX() - vec3d.x);
                    float g = (float) (eyePos.getY() - vec3d.y);
                    float h = (float) (eyePos.getZ() - vec3d.z);

                    double distance = MathHelper.sqrt(f * f + g * g + h * h);

                    if (distance > radius) continue;
                    BlockPosX blockPos = new BlockPosX(vecPos);

                    if (blocks.contains(blockPos)) continue;
                    blocks.add(blockPos);
                }
            }
        }

        return blocks;
    }

    public static boolean hasEntity(Box box) {
        return hasEntity(box, entity -> entity instanceof PlayerEntity || entity instanceof EndCrystalEntity || entity instanceof TntEntity || entity instanceof ItemEntity);
    }

    public static boolean hasEntity(Box box, Predicate<Entity> predicate) {
        return !mc.world.getOtherEntities(null, box, predicate).isEmpty();
    }

    public static EndCrystalEntity getEntity(BlockPosX blockPos) {
        if (blockPos == null) return null;

        return hasEntity(new Box(blockPos), entity -> entity instanceof EndCrystalEntity) ?
                (EndCrystalEntity) mc.world.getOtherEntities(null, new Box(blockPos), entity -> entity instanceof EndCrystalEntity).get(0) : null;
    }

    public  static Vec3d roundVec(Entity entity) {
        BlockPosX blockPos = new BlockPosX(entity.getBlockPos().down());

        return new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
    }

    public static Vec3d roundVec(BlockPosX blockPos) {
        return new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
    }

    public static double distanceTo(BlockPosX blockPos) {
        return distanceTo(closestVec3d(new Box(blockPos)));
    }

    public static double distanceTo(Vec3d vec3d) {
        return distanceTo(vec3d.getX(), vec3d.getY(), vec3d.getZ());
    }

    public static double distanceTo(double x, double y, double z) {
        Vec3d eyePos = new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());

        float f = (float) (eyePos.getX() - x);
        float g = (float) (eyePos.getY() - y);
        float h = (float) (eyePos.getZ() - z);
        return MathHelper.sqrt(f * f + g * g + h * h);
    }

    public static Vec3d closestVec3d(Box box) {
        if (box == null) return new Vec3d(0.0, 0.0, 0.0);
        Vec3d eyePos = new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());

        double x = MathHelper.clamp(eyePos.getX(), box.minX, box.maxX);
        double y = MathHelper.clamp(eyePos.getY(), box.minY, box.maxY);
        double z = MathHelper.clamp(eyePos.getZ(), box.minZ, box.maxZ);

        return new Vec3d(x, y, z);
    }

    public static List<BlockPosX> getSurroundBlocks(PlayerEntity player, boolean allBlocks) {
        ArrayList<BlockPosX> positions = new ArrayList<>();
        List<Entity> getEntityBoxes;

        for (BlockPosX blockPos : getSphere(new BlockPosX(player.getBlockPos()), 3, 1)) {
            if (!allBlocks && !mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) continue;
            getEntityBoxes = mc.world.getOtherEntities(null, new Box(blockPos), entity -> entity == player);
            if (!getEntityBoxes.isEmpty()) continue;

            for (Direction direction : Direction.values()) {
                if (direction == Direction.UP || direction == Direction.DOWN) continue;

                getEntityBoxes = mc.world.getOtherEntities(null, new Box(blockPos.offset(direction)), entity -> entity == player);
                if (!getEntityBoxes.isEmpty()) positions.add(new BlockPosX(blockPos));
            }
        }

        return positions;
    }

    public static boolean isSurrounded(PlayerEntity player) {
        return getSurroundBlocks(player, false).isEmpty();
    }

    public static List<BlockPosX> getSphere(BlockPosX centerPos, int radius, int height) {
        ArrayList<BlockPosX> blocks = new ArrayList<>();

        for (int i = centerPos.getX() - radius; i < centerPos.getX() + radius; i++) {
            for (int j = centerPos.getY() - height; j < centerPos.getY() + height; j++) {
                for (int k = centerPos.getZ() - radius; k < centerPos.getZ() + radius; k++) {
                    BlockPosX pos = new BlockPosX(i, j, k);
                    if (centerPos.getSquaredDistance(pos) <= radius && !blocks.contains(pos)) blocks.add(pos);
                }
            }
        }

        return blocks;
    }

    public static OtherClientPlayerEntity predictedTarget(PlayerEntity player) {
        OtherClientPlayerEntity fakeTarget = new OtherClientPlayerEntity(mc.world, player.getGameProfile(), player.getPublicKey());
        fakeTarget.setHealth(player.getHealth());
        fakeTarget.setAbsorptionAmount(player.getAbsorptionAmount());
        fakeTarget.getInventory().clone(player.getInventory());
        fakeTarget.setVelocity(0, 0, 0);

        return fakeTarget;
    }

    public static void move(FindItemResult itemResult, Runnable runnable) {
        if (itemResult.isOffhand()) {
            runnable.run();
            return;
        }

        move(mc.player.getInventory().selectedSlot, itemResult.slot());
        runnable.run();
        move(mc.player.getInventory().selectedSlot, itemResult.slot());
    }

    private static void move(int from, int to) {
        ScreenHandler handler = mc.player.currentScreenHandler;

        Int2ObjectArrayMap<ItemStack> stack = new Int2ObjectArrayMap<>();
        stack.put(to, handler.getSlot(to).getStack());

        mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(handler.syncId, handler.getRevision(), PlayerInventory.MAIN_SIZE + from, to, SlotActionType.SWAP, handler.getCursorStack().copy(), stack));
    }
}

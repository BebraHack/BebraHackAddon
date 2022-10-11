package com.example.addon.Utils.bbc.world;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class BlockHelper {
    public static ArrayList<Block> buttons = new ArrayList<Block>() {{
        add(Blocks.STONE_BUTTON);
        add(Blocks.POLISHED_BLACKSTONE_BUTTON);
        add(Blocks.OAK_BUTTON);
        add(Blocks.SPRUCE_BUTTON);
        add(Blocks.BIRCH_BUTTON);
        add(Blocks.JUNGLE_BUTTON);
        add(Blocks.ACACIA_BUTTON);
        add(Blocks.DARK_OAK_BUTTON);
        add(Blocks.CRIMSON_BUTTON);
        add(Blocks.WARPED_BUTTON);
    }};

    public static Vec3d vec3d(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Block getBlock(BlockPos pos) { return getState(pos).getBlock(); }
    public static boolean isAir(BlockPos pos) { return getState(pos).isAir(); }
    public static BlockState getState(BlockPos pos) { return mc.world.getBlockState(pos); }
    public static boolean isSolid(BlockPos pos) {return getState(pos).isSolidBlock(mc.world, pos);}
    public static boolean isBlastRes(BlockPos pos) {return getBlock(pos).getBlastResistance() >= 600;}
    public static boolean isOf(BlockPos pos, Class klass) {
        return klass.isInstance(getState(pos).getBlock());
    }
    public static boolean isOf(BlockPos pos, Block block) {
        return getState(pos).isOf(block);
    }
    public static boolean isBreakable(BlockPos pos) {return getState(pos).getHardness(mc.world, pos) > 0;}
    public static boolean isReplaceable(BlockPos pos) {return getState(pos).getMaterial().isReplaceable();}
    public static double getHardness(BlockPos pos) {
        return mc.world.getBlockState(pos).getHardness(mc.world, pos);
    }

    public static Box getBoundingBox(BlockPos pos) {
        return new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    public static Vec3d bestHitPos(BlockPos pos) {
        if (pos == null) return new Vec3d(0.0, 0.0, 0.0);
        double x = MathHelper.clamp((mc.player.getX() - pos.getX()), 0.0, 1.0);
        double y = MathHelper.clamp((mc.player.getY() - pos.getY()), 0.0, 1.0);
        double z = MathHelper.clamp((mc.player.getZ() - pos.getZ()), 0.0, 1.0);
        return new Vec3d(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
    }

    public static Vec3d closestVec3d(BlockPos pos) {
        return closestVec3d(getBoundingBox(pos));
    }
    private static Vec3d closestVec3d(Box box) {
        if (box == null) return new Vec3d(0.0, 0.0, 0.0);
        Vec3d eyePos = new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());

        double x = MathHelper.clamp(eyePos.getX(), box.minX, box.maxX);
        double y = MathHelper.clamp(eyePos.getY(), box.minY, box.maxY);
        double z = MathHelper.clamp(eyePos.getZ(), box.minZ, box.maxZ);

        return new Vec3d(x, y, z);
    }

    public static Vec3d getBlockCenter(BlockPos pos){
        if (pos == null) return new Vec3d(0.0, 0.0, 0.0);
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        return new Vec3d(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
    }

    public static double distance(BlockPos block1, BlockPos block2) {
        double dX = block2.getX() - block1.getX();
        double dY = block2.getY() - block1.getY();
        double dZ = block2.getZ() - block1.getZ();
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static double distance(Vec3d block1, Vec3d block2) {
        double dX = block2.getX() - block1.getX();
        double dY = block2.getY() - block1.getY();
        double dZ = block2.getZ() - block1.getZ();
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dX = x2 - x1;
        double dY = y2 - y1;
        double dZ = z2 - z1;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
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

    public static Direction getPlaceableSide(BlockPos pos) {
        for (Direction side : Direction.values()) {
            BlockPos neighbour = pos.offset(side);
            BlockState blockState = mc.world.getBlockState(neighbour);
            if (!blockState.getMaterial().isReplaceable()) return side;
        }
        return null;
    }

    public static List<BlockPos> getSphere(BlockPos centerPos, int radius, int height) {
        ArrayList<BlockPos> blocks = new ArrayList<>();
        for (int i = centerPos.getX() - radius; i < centerPos.getX() + radius; i++) {
            for (int j = centerPos.getY() - height; j < centerPos.getY() + height; j++) {
                for (int k = centerPos.getZ() - radius; k < centerPos.getZ() + radius; k++) {
                    BlockPos pos = new BlockPos(i, j, k);
                    if (distance(centerPos, pos) <= radius && !blocks.contains(pos)) blocks.add(pos);
                }
            }
        }
        return blocks;
    }

    public static List<BlockPosX> getSphere(BlockPosX centerPos, int radius, int height) {
        ArrayList<BlockPosX> blocks = new ArrayList<>();
        for (int i = centerPos.getX() - radius; i < centerPos.getX() + radius; i++) {
            for (int j = centerPos.getY() - height; j < centerPos.getY() + height; j++) {
                for (int k = centerPos.getZ() - radius; k < centerPos.getZ() + radius; k++) {
                    BlockPosX pos = new BlockPosX(i, j, k);
                    if (distance(centerPos, pos) <= radius && !blocks.contains(pos)) blocks.add(pos);
                }
            }
        }
        return blocks;
    }

    public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
}

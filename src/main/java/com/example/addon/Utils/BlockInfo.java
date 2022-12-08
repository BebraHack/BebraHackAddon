package com.example.addon.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class BlockInfo {
    private static final ArrayList<BlockPos> blocks = new ArrayList<>();

    //Самые полезные переменные для БлокПос
    public static BlockState getState(BlockPos block) {return mc.world.getBlockState(block);}
    public static VoxelShape getShape(BlockPos block) {return getState(block).getOutlineShape(mc.world, block);}
    public static Box getBox(BlockPos block) {return getShape(block).getBoundingBox();}
    public static Block getBlock(BlockPos block) {return mc.world.getBlockState(block).getBlock();}
    public static boolean isAir(BlockPos block) {return mc.world.getBlockState(block).isAir();}
    public static Material getMaterial(BlockPos block) {return mc.world.getBlockState(block).getMaterial();}
    public static float getBlastResistance(BlockPos block) {return mc.world.getBlockState(block).getBlock().getBlastResistance();}
    public static float getBlastResistance(Block block) {return block.getBlastResistance();}
    public static boolean isReplaceable(BlockPos block) {return mc.world.getBlockState(block).getMaterial().isReplaceable();}
    public static boolean isSolid(BlockPos block) {return mc.world.getBlockState(block).getMaterial().isSolid();}
    public static boolean isBurnable(BlockPos block) {return mc.world.getBlockState(block).getMaterial().isBurnable();}
    public static boolean isLiquid(BlockPos block) {return mc.world.getBlockState(block).getMaterial().isLiquid();}
    public static float getHardness(BlockPos block) {return mc.world.getBlockState(block).getHardness(mc.world,block);}
    public static float getHardness(Block block) {return block.getHardness();}
    public static boolean isBlastResist(BlockPos block) {return getBlastResistance(block) >= 600;}
    public static boolean isBlastResist(Block block) {return getBlastResistance(block) >= 600;}
    public static boolean isBreakable(BlockPos block) {return getHardness(block) > 0;}
    public static boolean isBreakable(Block block) {return getHardness(block) > 0;}
    public static boolean isCombatBlock(BlockPos block) {return isBlastResist(block) && isBreakable(block);}
    public static boolean isCombatBlock(Block block) {return isBlastResist(block) && isBreakable(block);}
    public static Vec3d getCenterVec3d(BlockPos block) {return new Vec3d(block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5);}
    public static boolean notNull(BlockPos block) {return block != null;}
    public static boolean isNull(BlockPos block) {return block == null;}
    public static int X(BlockPos block) {return block.getX();}
    public static int Y(BlockPos block) {return block.getY();}
    public static int Z(BlockPos block) {return block.getZ();}
    public static boolean isWithinRange(BlockPos block, double range) {return mc.player.getBlockPos().isWithinDistance(block, range);}
    public static boolean isFullCube(BlockPos block) {
        return getState(block).isFullCube(mc.world, block);
    }
    public static Vec3d closestVec3d(BlockPos blockpos) {
        if (blockpos == null) return new Vec3d(0.0, 0.0, 0.0);
        double x = MathHelper.clamp((mc.player.getX() - blockpos.getX()), 0.0, 1.0);
        double y = MathHelper.clamp((mc.player.getY() - blockpos.getY()), 0.0, 0.6);
        double z = MathHelper.clamp((mc.player.getZ() - blockpos.getZ()), 0.0, 1.0);
        return new Vec3d(blockpos.getX() + x, blockpos.getY() + y, blockpos.getZ() + z);
    }

    public static List<BlockPos> getSphere(BlockPos centerPos, int radius, int height) {
        blocks.clear();

        for (int i = centerPos.getX() - radius; i < centerPos.getX() + radius; i++) {
            for (int j = centerPos.getY() - height; j < centerPos.getY() + height; j++) {
                for (int k = centerPos.getZ() - radius; k < centerPos.getZ() + radius; k++) {
                    BlockPos pos = new BlockPos(i, j, k);
                    if (distanceBetween(centerPos, pos) <= radius && !blocks.contains(pos)) blocks.add(pos);
                }
            }
        }

        return blocks;
    }

    public static double distanceBetween(BlockPos blockPos1, BlockPos blockPos2) {
        double d = blockPos1.getX() - blockPos2.getX();
        double e = blockPos1.getY() - blockPos2.getY();
        double f = blockPos1.getZ() - blockPos2.getZ();
        return MathHelper.sqrt((float) (d * d + e * e + f * f));
    }

}

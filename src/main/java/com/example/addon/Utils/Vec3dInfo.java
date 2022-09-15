package com.example.addon.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Vec3dInfo {

    //немного про век3д
    public static boolean isInRange(Vec3d vec3d, double radius) {return vec3d.isInRange(vec3d, radius);}
    public static boolean isWithinRange(Vec3d vec3d, double range) {return mc.player.getBlockPos().isWithinDistance(vec3d, range);}
    public static Vec3d add(Vec3d vec3d, Vec3d added) {return  new Vec3d(vec3d.add(added).getX(), vec3d.add(added).getY(), vec3d.add(added).getZ());}
    public static Vec3d add(Vec3d vec3d, double x, double y, double z) {return new Vec3d(vec3d.add(x, y, z).getX(), vec3d.add(x, y, z).getY(), vec3d.add(x, y, z).getZ());}
    public static double X(Vec3d vec3d) {return vec3d.getX();}
    public static double Y(Vec3d vec3d) {return vec3d.getY();}
    public static double Z(Vec3d vec3d) {return vec3d.getZ();}
    public static boolean notNull(Vec3d vec3d) {return vec3d != null;}

    public static Vec3d getEyeVec(PlayerEntity entity) {return entity.getPos().add(0, entity.getEyeHeight(entity.getPose()), 0);}
    public static Vec3d closestVec3d(BlockPos blockpos) {
        if (blockpos == null) return new Vec3d(0.0, 0.0, 0.0);
        double x = MathHelper.clamp((mc.player.getX() - blockpos.getX()), 0.0, 1.0);
        double y = MathHelper.clamp((mc.player.getY() - blockpos.getY()), 0.0, 0.6);
        double z = MathHelper.clamp((mc.player.getZ() - blockpos.getZ()), 0.0, 1.0);
        return new Vec3d(blockpos.getX() + x, blockpos.getY() + y, blockpos.getZ() + z);
    }
}

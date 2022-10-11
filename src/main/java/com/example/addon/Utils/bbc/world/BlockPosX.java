package com.example.addon.Utils.bbc.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.*;

import java.util.List;
import java.util.function.Predicate;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class BlockPosX extends BlockPos {
    public BlockPosX(int i, int j, int k) {
        super(i, j, k);
    }

    public BlockPosX(BlockPos blockPos) {
        super(blockPos);
    }

    public BlockPosX(Vec3d vec3d) {
        super(vec3d);
    }

    public int x() {
        return this.getX();
    }

    public int y() {
        return this.getY();
    }

    public int z() {
        return this.getZ();
    }

    // Blocks
    public BlockPos get() {
        return new BlockPos(this);
    }

    public boolean air() {
        return state().isAir();
    }

    public boolean of(Block block) {
        return state().isOf(block);
    }

    public boolean of(Class klass) {
        return klass.isInstance(this.state().getBlock());
    }

    public boolean of(Fluid fluid) {
        return this.state().getFluidState().isOf(fluid);
    }

    public boolean of(Block... blocks) {
        for (Block block : blocks) {
            if (this.of(block)) return true;
        }

        return false;
    }

    public boolean solid() {
        return state().isSolidBlock(mc.world, this);
    }

    public boolean fluid() {
        return of(Fluids.FLOWING_LAVA) || of(Fluids.FLOWING_WATER) || of(Fluids.LAVA) || of(Fluids.WATER);
    }

    public boolean blastRes() {
        return state().getBlock().getBlastResistance() >= 600;
    }

    public boolean breakable() {
        return hardness() > 0;
    }

    public boolean unbreakable() {
        return of(Blocks.BEDROCK) || of(Blocks.BARRIER) || of(Blocks.END_PORTAL) || of(Blocks.NETHER_PORTAL);
    }

    public boolean replaceable() {
        return state().getMaterial().isReplaceable();
    }

    public double hardness() {
        return mc.world.getBlockState(this).getHardness(mc.world, this);
    }

    public BlockState state() {
        return mc.world.getBlockState(this);
    }

    public void state(Block block) {
        mc.world.setBlockState(this, block.getDefaultState());
    }

    public boolean hasNeighbours() {
        return getNeighbour() != null;
    }

    public Direction getNeighbour() {
        for (Direction direction : Direction.values()) {
            if (!this.offset(direction).air()) return direction;
        }

        return null;
    }

    // Box
    public Box box() {
        return new Box(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1);
    }

    // Vec3d
    public Vec3d center() {
        return new Vec3d(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5);
    }

    public Vec3d vec3d() {
        return new Vec3d(this.getX(), this.getY(), this.getZ());
    }

    public Vec3d closestVec3d() {
        return closestVec3d(this.box());
    }

    private Vec3d closestVec3d(Box box) {
        if (box == null) return new Vec3d(0.0, 0.0, 0.0);
        Vec3d eyePos = new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());

        double x = MathHelper.clamp(eyePos.getX(), box.minX, box.maxX);
        double y = MathHelper.clamp(eyePos.getY(), box.minY, box.maxY);
        double z = MathHelper.clamp(eyePos.getZ(), box.minZ, box.maxZ);

        return new Vec3d(x, y, z);
    }

    // Distance
    public double distance(BlockPosX BlockPosX) {
        return distance(BlockPosX.center());
    }

    public double distance(PlayerEntity player) {
        return distance(player.getX() + 0.5, player.getY(), player.getZ() + 0.5);
    }

    public double distance(Vec3d vec3d) {
        return distance(vec3d.x, vec3d.y, vec3d.z);
    }

    public double distance() {
        Vec3d eyePos = new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());

        float f = (float) (eyePos.getX() - closestVec3d().x);
        float g = (float) (eyePos.getY() - closestVec3d().y);
        float h = (float) (eyePos.getZ() - closestVec3d().z);
        return MathHelper.sqrt(f * f + g * g + h * h);
    }

    private double distance(double x, double y, double z) {
        float f = (float) (this.getX() - x);
        float g = (float) (this.getY() - y);
        float h = (float) (this.getZ() - z);
        return MathHelper.sqrt(f * f + g * g + h * h);
    }

    // Entity
    public boolean hasEntity() {
        return hasEntity(entity -> entity instanceof PlayerEntity || entity instanceof EndCrystalEntity || entity instanceof TntEntity);
    }

    public boolean hasEntity(Predicate<Entity> predicate) {
        return hasEntity(this.box(), predicate);
    }

    public boolean hasEntity(Box box) {
        return hasEntity(box, entity -> entity instanceof PlayerEntity || entity instanceof EndCrystalEntity || entity instanceof TntEntity);
    }

    public boolean hasEntity(Box box, Predicate<Entity> predicate) {
        return !mc.world.getOtherEntities(null, box, predicate).isEmpty();
    }

    public List<Entity> getEntities() {
        return getEntities(entity -> entity instanceof PlayerEntity || entity instanceof EndCrystalEntity || entity instanceof TntEntity);
    }

    public List<Entity> getEntities(Predicate<Entity> predicate) {
        return getEntities(this.box(), predicate);
    }

    public List<Entity> getEntities(Box box) {
        return getEntities(box, entity -> entity instanceof PlayerEntity || entity instanceof EndCrystalEntity || entity instanceof TntEntity);
    }

    public List<Entity> getEntities(Box box, Predicate<Entity> predicate) {
        return mc.world.getOtherEntities(null, box, predicate);
    }

    // Overriding
    public BlockPosX down() {
        return new BlockPosX(super.down());
    }

    public BlockPosX down(int i) {
        return new BlockPosX(super.down(i));
    }

    public BlockPosX up() {
        return new BlockPosX(super.up());
    }

    public BlockPosX up(int i) {
        return new BlockPosX(super.up(i));
    }

    public BlockPosX north() {
        return new BlockPosX(super.north());
    }

    public BlockPosX north(int i) {
        return new BlockPosX(super.north(i));
    }

    public BlockPosX south() {
        return new BlockPosX(super.south());
    }

    public BlockPosX south(int i) {
        return new BlockPosX(super.south(i));
    }

    public BlockPosX west() {
        return new BlockPosX(super.west());
    }

    public BlockPosX west(int i) {
        return new BlockPosX(super.west(i));
    }

    public BlockPosX east() {
        return new BlockPosX(super.east());
    }

    public BlockPosX east(int i) {
        return new BlockPosX(super.east(i));
    }

    public BlockPosX offset(Direction direction) {
        return new BlockPosX(this.getX() + direction.getOffsetX(), this.getY() + direction.getOffsetY(), this.getZ() + direction.getOffsetZ());
    }

    public BlockPosX offset(Direction direction, int i) {
        return i == 0 ? this : new BlockPosX(this.getX() + direction.getOffsetX() * i, this.getY() + direction.getOffsetY() * i, this.getZ() + direction.getOffsetZ() * i);
    }

    public boolean equals(BlockPosX BlockPosX) {
        return this.getX() == BlockPosX.getX() && this.getY() == BlockPosX.getY() && this.getZ() == BlockPosX.getZ();
    }

    public boolean equals(BlockPos blockPos) {
        return this.getX() == blockPos.getX() && this.getY() == blockPos.getY() && this.getZ() == blockPos.getZ();
    }

}

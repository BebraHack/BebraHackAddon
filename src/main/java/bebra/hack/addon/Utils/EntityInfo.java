package bebra.hack.addon.Utils;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static bebra.hack.addon.Utils.BlockInfo.isBlastResist;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class EntityInfo {

    // Самые полезные переменные для Ентити
    public static boolean isAlive(PlayerEntity entity) {
        return entity.isAlive();
    }
    public static boolean isDead(PlayerEntity entity) {
        return entity.isDead();
    }
    public static boolean isOnGround(PlayerEntity entity) {
        return entity.isOnGround();
    }
    public static float getMovementSpeed(PlayerEntity entity) {
        return entity.getMovementSpeed();
    }
    public static boolean isMoving(PlayerEntity entity) {return entity.forwardSpeed != 0 || entity.sidewaysSpeed != 0;}
    public static boolean canRecieveDmg(PlayerEntity entity) {
        return entity.hurtTime == 0;
    }
    public static BlockPos getBlockPos(PlayerEntity entity) {
        return entity.getBlockPos();
    }
    public static BlockPos getBlockPos(Entity entity) {
        return entity.getBlockPos();
    }
    public static Position getPos(PlayerEntity entity) {
        return entity.getPos();
    }
    public static Vec3d getVelocity(PlayerEntity entity) {
        return entity.getVelocity();
    }
    public static Box getBoundingBox(PlayerEntity entity) {
        return entity.getBoundingBox();
    }
    public static Box getBoundingBox(Entity entity) {
        return entity.getBoundingBox();
    }
    public static PlayerAbilities getAbilities(PlayerEntity entity) {
        return entity.getAbilities();
    }
    public static boolean isCreative(PlayerEntity entity) {
        return entity.getAbilities().creativeMode;
    }
    public static float getFlySpeed(PlayerEntity entity) {
        return entity.getAbilities().getFlySpeed();
    }
    public static float getWalkSpeed(PlayerEntity entity) {
        return entity.getAbilities().getWalkSpeed();
    }
    public static void setWalkSpeed(PlayerEntity entity, float speed) {
        entity.getAbilities().setWalkSpeed(speed);
    }
    public static void setFlySpeed(PlayerEntity entity, float speed) {
        entity.getAbilities().setFlySpeed(speed);
    }
    public static World getWorld(PlayerEntity entity) {
        return entity.getWorld();
    }
    public static int deathTime(PlayerEntity entity) {
        return entity.deathTime;
    }
    public static int getFoodLevel(PlayerEntity entity) {
        return entity.getHungerManager().getFoodLevel();
    }
    public static float getSaturationLevel(PlayerEntity entity) {return entity.getHungerManager().getSaturationLevel();}
    public static float getExhaustionLevel(PlayerEntity entity) {
        return entity.getHungerManager().getExhaustion();
    }
    public static Inventory getInventory(PlayerEntity entity) {
        return entity.getInventory();
    }
    public static ItemStack getStack(PlayerEntity entity, int slot) {
        return entity.getInventory().getStack(slot);
    }
    public static int getMainSlot(PlayerEntity entity) {
        return entity.getInventory().selectedSlot;
    }
    public static int getOffhandSlot(PlayerEntity entity) {
        return 45;
    }
    public static int getEmptySlot(PlayerEntity entity) {
        return entity.getInventory().getEmptySlot();
    }
    public static boolean isEmptyInventory(PlayerEntity entity) {
        return entity.getInventory().isEmpty();
    }
    public static boolean notNull(PlayerEntity entity) {
        return entity != null;
    }
    public static String getName(PlayerEntity entity) {
        return entity.getGameProfile().getName();
    }
    public static double X(PlayerEntity entity) {
        return entity.getX();
    }
    public static double Y(PlayerEntity entity) {
        return entity.getY();
    }
    public static double Z(PlayerEntity entity) {
        return entity.getZ();
    }
    public static Iterable<Entity> getEntities() {
        return mc.world.getEntities();
    }
    public static List<AbstractClientPlayerEntity> getPlayers() {
        return mc.world.getPlayers();
    }

    public static boolean isSurrounded(LivingEntity entity) {
        return isBlastResist(entity.getBlockPos().south())
                && isBlastResist(entity.getBlockPos().west())
                && isBlastResist(entity.getBlockPos().east())
                && isBlastResist(entity.getBlockPos().north())
                && isBlastResist(entity.getBlockPos().down());
    }

    public static boolean isTrapped(LivingEntity entity) {
        return isBlastResist(entity.getBlockPos().south().up())
                && isBlastResist(entity.getBlockPos().west().up())
                && isBlastResist(entity.getBlockPos().east().up())
                && isBlastResist(entity.getBlockPos().north().up())
                && isBlastResist(entity.getBlockPos().up(2));
    }

    public static boolean isFaceTrapped(LivingEntity entity) {
        return isBlastResist(entity.getBlockPos().south().up())
                && isBlastResist(entity.getBlockPos().west().up())
                && isBlastResist(entity.getBlockPos().east().up())
                && isBlastResist(entity.getBlockPos().north().up());
    }

    public static boolean isInHole(PlayerEntity p) {
        BlockPos pos = p.getBlockPos();
        return !mc.world.getBlockState(pos.add(1, 0, 0)).isAir()
                && !mc.world.getBlockState(pos.add(-1, 0, 0)).isAir()
                && !mc.world.getBlockState(pos.add(0, 0, 1)).isAir()
                && !mc.world.getBlockState(pos.add(0, 0, -1)).isAir()
                && !mc.world.getBlockState(pos.add(0, -1, 0)).isAir();
    }

    public static boolean isDoubleSurrounded(LivingEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        return isBlastResist(blockPos.add(1, 0, 0)) &&
                isBlastResist(blockPos.add(-1, 0, 0)) &&
                isBlastResist(blockPos.add(0, 0, 1)) &&
                isBlastResist(blockPos.add(0, 0, -1)) &&
                isBlastResist(blockPos.add(1, 1, 0)) &&
                isBlastResist(blockPos.add(-1, 1, 0)) &&
                isBlastResist(blockPos.add(0, 1, 1)) &&
                isBlastResist(blockPos.add(0, 1, -1));
    }
}

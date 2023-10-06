package bebra.hack.addon.Utils;

import bebra.hack.addon.Utils.BlockHelper;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.world.TickRate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.tutorial.TutorialStep;
import meteordevelopment.meteorclient.utils.player.ChatUtils;


import java.util.ArrayList;

import static meteordevelopment.meteorclient.MeteorClient.mc;
class Wrapper {

    public static void disableTutorial() { mc.getTutorialManager().setStep(TutorialStep.NONE);}

    public static boolean isLagging() {
        return TickRate.INSTANCE.getTimeSinceLastTick() >= 0.8;
    }

    public static float getTotalHealth(PlayerEntity p) {
        return p.getHealth() + p.getAbsorptionAmount();
    }

    public static Item getItemFromSlot(Integer slot) {
        if (slot == -1) return null;
        if (slot == 45) return mc.player.getOffHandStack().getItem();
        return mc.player.getInventory().getStack(slot).getItem();
    }

    public static void updateSlot(int newSlot) {
        mc.player.getInventory().selectedSlot = newSlot;
    }

    public static void swingHand(boolean offhand) {
        if (offhand) {
            mc.player.swingHand(Hand.OFF_HAND);
        } else {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    public static boolean isPlayerMoving(PlayerEntity p) {
        return p.forwardSpeed != 0 || p.sidewaysSpeed != 0;
    }

    public static boolean isInHole(PlayerEntity p) {
        BlockPos pos = p.getBlockPos();
        return !mc.world.getBlockState(pos.add(1, 0, 0)).isAir()
            && !mc.world.getBlockState(pos.add(-1, 0, 0)).isAir()
            && !mc.world.getBlockState(pos.add(0, 0, 1)).isAir()
            && !mc.world.getBlockState(pos.add(0, 0, -1)).isAir()
            && !mc.world.getBlockState(pos.add(0, -1, 0)).isAir();
    }

    public static int randomNum(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static void messagePlayer(String playerName, String m) {
        assert mc.player != null;
        ChatUtils.sendPlayerMsg("/msg " + playerName + " " +  m);
    }

}
public class AutomationUtils {

    public static ArrayList<Vec3d> surroundPositions = new ArrayList<Vec3d>() {{
        add(new Vec3d(1, 0, 0));
        add(new Vec3d(-1, 0, 0));
        add(new Vec3d(0, 0, 1));
        add(new Vec3d(0, 0, -1));
    }};


    public static boolean isAnvilBlock(BlockPos pos) {
        return BlockHelper.getBlock(pos) == Blocks.ANVIL || BlockHelper.getBlock(pos) == Blocks.CHIPPED_ANVIL || BlockHelper.getBlock(pos) == Blocks.DAMAGED_ANVIL;
    }

    public static boolean isWeb(BlockPos pos) {
        return BlockHelper.getBlock(pos) == Blocks.COBWEB || BlockHelper.getBlock(pos) == Block.getBlockFromItem(Items.STRING);
    }

    public static boolean isBurrowed(PlayerEntity p, boolean holeCheck) {
        BlockPos pos = p.getBlockPos();
        if (holeCheck && !Wrapper.isInHole(p)) return false;
        return BlockHelper.getBlock(pos) == Blocks.ENDER_CHEST || BlockHelper.getBlock(pos) == Blocks.OBSIDIAN || isAnvilBlock(pos);
    }

    public static boolean isWebbed(PlayerEntity p) {
        BlockPos pos = p.getBlockPos();
        if (isWeb(pos)) return true;
        return isWeb(pos.up());
    }

    public static boolean isTrapBlock(BlockPos pos) {
        return BlockHelper.getBlock(pos) == Blocks.OBSIDIAN || BlockHelper.getBlock(pos) == Blocks.ENDER_CHEST;
    }

    public static boolean isSurroundBlock(BlockPos pos) {
        //some apes use anchors as surround blocks cus it has the same blast resistance as obsidian
        return BlockHelper.getBlock(pos) == Blocks.OBSIDIAN || BlockHelper.getBlock(pos) == Blocks.ENDER_CHEST || BlockHelper.getBlock(pos) == Blocks.RESPAWN_ANCHOR;
    }

    public static boolean canCrystal(PlayerEntity p) {
        BlockPos tpos = p.getBlockPos();
        for (Vec3d sp : surroundPositions) {
            BlockPos sb = tpos.add((int) sp.x, (int) sp.y, (int) sp.z);
            if (BlockHelper.getBlock(sb) == Blocks.AIR) return true;
        }
        return false;
    }

    public static void mineWeb(PlayerEntity p, int swordSlot) {
        if (p == null || swordSlot == -1) return;
        BlockPos pos = p.getBlockPos();
        BlockPos webPos = null;
        if (isWeb(pos)) webPos = pos;
        if (isWeb(pos.up())) webPos = pos.up();
        if (isWeb(pos.up(2))) webPos = pos.up(2);
        if (webPos == null) return;
        Wrapper.updateSlot(swordSlot);
        doRegularMine(webPos);
    }

    public static void doPacketMine(BlockPos targetPos) {
        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, targetPos, Direction.UP));
        Wrapper.swingHand(false);
        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, targetPos, Direction.UP));
    }

    public static void doRegularMine(BlockPos targetPos) {
        mc.interactionManager.updateBlockBreakingProgress(targetPos, Direction.UP);
        Vec3d hitPos = new Vec3d(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5);
        Rotations.rotate(Rotations.getYaw(hitPos), Rotations.getPitch(hitPos), 50, () -> Wrapper.swingHand(false));
    }

    public static boolean isInHole(PlayerEntity p) {
        BlockPos pos = p.getBlockPos();
        return !mc.world.getBlockState(pos.add(1, 0, 0)).isAir()
            && !mc.world.getBlockState(pos.add(-1, 0, 0)).isAir()
            && !mc.world.getBlockState(pos.add(0, 0, 1)).isAir()
            && !mc.world.getBlockState(pos.add(0, 0, -1)).isAir()
            && !mc.world.getBlockState(pos.add(0, -1, 0)).isAir();
    }
}

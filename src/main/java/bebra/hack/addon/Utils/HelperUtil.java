package bebra.hack.addon.Utils;

import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class HelperUtil {

    public static FindItemResult obby() {
        return InvUtils.findInHotbar(Items.OBSIDIAN);
    }

    public static FindItemResult piston() {
        return InvUtils.findInHotbar(Items.PISTON, Items.STICKY_PISTON);
    }

    public static FindItemResult redstone() {
        return InvUtils.findInHotbar(Items.REDSTONE_BLOCK, Items.REDSTONE_TORCH);
    }

    public static boolean isInHole(PlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        return !mc.world.getBlockState(pos.add(1, 0, 0)).isAir()
            && !mc.world.getBlockState(pos.add(-1, 0, 0)).isAir()
            && !mc.world.getBlockState(pos.add(0, 0, 1)).isAir()
            && !mc.world.getBlockState(pos.add(0, 0, -1)).isAir()
            && !mc.world.getBlockState(pos.add(0, -1, 0)).isAir();
    }
}

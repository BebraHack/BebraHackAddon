package bebra.hack.addon.modules.pve;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;


import java.util.List;

public class NewsPaperHelperEu extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgMisc = settings.createGroup("Misc");


    //private final Setting<Boolean> A = sgGeneral.add(new BoolSetting.Builder().name("NewsPaperHelperRu").description("Help for you").defaultValue(true).build());
    private final Setting<NewsPaperHelperEu.Euletter1> itemUse3 = sgGeneral.add(new EnumSetting.Builder<NewsPaperHelperEu.Euletter1>().name("Euletters1").defaultValue(NewsPaperHelperEu.Euletter1.A).build());
    private final Setting<NewsPaperHelperEu.Euletter2> itemUse4 = sgGeneral.add(new EnumSetting.Builder<NewsPaperHelperEu.Euletter2>().name("Euletters2").defaultValue(NewsPaperHelperEu.Euletter2.N).build());

    private final Setting<Boolean> checkEntity = sgMisc.add(new BoolSetting.Builder().name("check-entity").description("entity check").defaultValue(true).build());
    private final Setting<Boolean> onlyOnGround = sgMisc.add(new BoolSetting.Builder().name("only-on-ground").description("Works only when you standing on blocks.").defaultValue(false).build());
    private final Setting<Boolean> center = sgMisc.add(new BoolSetting.Builder().name("center").description("Teleports you to the center of the block.").defaultValue(true).build());
    private final Setting<Boolean> disableOnJump = sgMisc.add(new BoolSetting.Builder().name("disable-on-jump").description("Automatically disables when you jump.").defaultValue(false).build());
    private final Setting<Boolean> disableOnYChange = sgMisc.add(new BoolSetting.Builder().name("disable-on-y-change").description("Automatically disables when your y level (step, jumping, atc).").defaultValue(false).build());
    private final Setting<Boolean> rotate = sgMisc.add(new BoolSetting.Builder().name("rotate").description("Automatically faces towards the obsidian being placed.").defaultValue(true).build());
    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("Blocks to search for.")
        .onChanged(blocks1 -> {
            if (isActive() && Utils.canUpdate()) onActivate();
        })
        .build()
    );


    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean return_;

    public NewsPaperHelperEu() {
        super(Addon.misc, "NewsPaperHelperEu", "build letter");
    }

    @Override
    public void onActivate() {
        if (center.get()) PlayerUtils.centerPlayer();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if ((disableOnJump.get() && (mc.options.jumpKey.isPressed() || mc.player.input.jumping)) || (disableOnYChange.get() && mc.player.prevY < mc.player.getY())) {
            toggle();
            return;
        }

        if (onlyOnGround.get() && !mc.player.isOnGround()) return;

        return_ = false;

    }

    private boolean place(int x, int y, int z) {
        setBlockPos(x, y, z);
        BlockState blockState = mc.world.getBlockState(blockPos);
        if (!blockState.getMaterial().isReplaceable()) return true;
        if (BlockUtils.place(blockPos, InvUtils.findInHotbar(itemStack -> blocks.get().contains(Block.getBlockFromItem(itemStack.getItem()))), rotate.get(), 100, checkEntity.get())) {
            return_ = true;
        }
        return false;
    }


    private void setBlockPos(int x, int y, int z) {
        blockPos.set(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);
    }

    @EventHandler
    private void onTickEvent(TickEvent.Pre event) {
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.A) {

            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(0, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.B) {
            place(0, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.C) {
            place(0, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.D) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.E) {
            place(0, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.F) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.G) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(-2, 0, -1);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(-2, 0, -3);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.K) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.L) {
            place(-1, 0, -1);
            if (return_) return;
            place(0, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.M) {
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-2, 0, -1);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(-2, 0, -3);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(-2, 0, -5);
            if (return_) return;
            place(2, 0, -1);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
            place(2, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.H) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.I) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(0, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse3.get() == NewsPaperHelperEu.Euletter1.J) {
            place(0, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.N) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(2, 0, -1);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.O) {
            place(0, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.P) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.Q) {
            place(0, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(2, 0, 0);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.R) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.S) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.T) {
            place(0, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(0, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.U) {
            place(0, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.V) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.W) {
            place(-1, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(0, 0, -4);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(-2, 0, -3);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.X) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.Y) {
            place(0, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse4.get() == NewsPaperHelperEu.Euletter2.Z) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
        }
    }

    public enum Euletter1 {
        none,
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H,
        I,
        J,
        K,
        L,
        M;
    }

    public enum Euletter2 {
        none,
        N,
        O,
        P,
        Q,
        R,
        S,
        T,
        U,
        V,
        W,
        X,
        Y,
        Z;
    }
}

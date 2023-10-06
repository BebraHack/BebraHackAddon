package bebra.hack.addon.modules.pve;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;


import java.util.List;

public class NewsPaperHelperRu extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgMisc = settings.createGroup("Misc");


    //private final Setting<Boolean> A = sgGeneral.add(new BoolSetting.Builder().name("NewsPaperHelperRu").description("Help for you").defaultValue(true).build());
    private final Setting<NewsPaperHelperRu.letter1> itemUse = sgGeneral.add(new EnumSetting.Builder<NewsPaperHelperRu.letter1>().name("letters1").defaultValue(NewsPaperHelperRu.letter1.А).build());
    private final Setting<NewsPaperHelperRu.letter2> itemUse2 = sgGeneral.add(new EnumSetting.Builder<NewsPaperHelperRu.letter2>().name("letters2").defaultValue(NewsPaperHelperRu.letter2.С).build());

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

    public NewsPaperHelperRu() {
        super(Addon.misc, "NewsPaperHelperRu", "build letter");
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.А) {

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
        if (itemUse.get() == NewsPaperHelperRu.letter1.Б) {

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
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.В) {
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.Г) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.Д) {
            place(0, 0, -2);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(-2, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(2, 0, -1);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
        }
        if (itemUse.get() == NewsPaperHelperRu.letter1.Е) {
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.Ё) {
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
            place(-1, 0, -7);
            if (return_) return;
            place(1, 0, -7);
            if (return_) return;
        }
        if (itemUse.get() == NewsPaperHelperRu.letter1.Ж) {
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
            place(-1, 0, -3);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-2, 0, -1);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(2, 0, -1);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(-2, 0, -5);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
            place(2, 0, -5);
            if (return_) return;
        }
        if (itemUse.get() == NewsPaperHelperRu.letter1.З) {
            place(0, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
        }
        if (itemUse.get() == NewsPaperHelperRu.letter1.И) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(1, 0, -3);
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.Й) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(2, 0, -1);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
            place(0, 0, -6);
            if (return_) return;
            place(1, 0, -6);
            if (return_) return;
        }
        if (itemUse.get() == NewsPaperHelperRu.letter1.К) {
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.Л) {
            place(-1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(-2, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
        }
        if (itemUse.get() == NewsPaperHelperRu.letter1.М) {
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.Н) {
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.О) {
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
        if (itemUse.get() == NewsPaperHelperRu.letter1.П) {
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
            place(1, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Р) {
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
        if (itemUse2.get() == NewsPaperHelperRu.letter2.С) {
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
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Т) {
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
        if (itemUse2.get() == NewsPaperHelperRu.letter2.У) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Ф) {
            place(0, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(0, 0, -4);
            if (return_) return;
            place(-2, 0, -3);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Х) {
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
            place(-1, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Ц) {
            place(2, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Ч) {
            place(1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Ш) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(-2, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(2, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(0, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(-2, 0, -3);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(-2, 0, -5);
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
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Щ) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(-2, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(2, 0, -1);
            if (return_) return;
            place(3, 0, -1);
            if (return_) return;
            place(3, 0, 0);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(0, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(-2, 0, -3);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(-2, 0, -5);
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
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Ъ) {
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
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
            place(-2, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Ы) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(-2, 0, -1);
            if (return_) return;
            place(2, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-2, 0, -3);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Ь) {
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
            place(1, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -4);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Э) {
            place(0, 0, -1);
            if (return_) return;
            place(-1, 0, -1);
            if (return_) return;
            place(1, 0, -2);
            if (return_) return;
            place(1, 0, -3);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(1, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(-1, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Ю) {
            place(0, 0, -1);
            if (return_) return;
            place(-2, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(2, 0, -1);
            if (return_) return;
            place(0, 0, -2);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(-2, 0, -2);
            if (return_) return;
            place(2, 0, -2);
            if (return_) return;
            place(0, 0, -3);
            if (return_) return;
            place(-1, 0, -3);
            if (return_) return;
            place(-2, 0, -3);
            if (return_) return;
            place(2, 0, -3);
            if (return_) return;
            place(0, 0, -4);
            if (return_) return;
            place(-2, 0, -4);
            if (return_) return;
            place(2, 0, -4);
            if (return_) return;
            place(0, 0, -5);
            if (return_) return;
            place(1, 0, -5);
            if (return_) return;
            place(-2, 0, -5);
            if (return_) return;
            place(2, 0, -5);
            if (return_) return;
        }
        if (itemUse2.get() == NewsPaperHelperRu.letter2.Я) {
            place(-1, 0, -1);
            if (return_) return;
            place(1, 0, -1);
            if (return_) return;
            place(-1, 0, -2);
            if (return_) return;
            place(1, 0, -2);
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
    }

    public enum letter1 {
        none,
        А,
        Б,
        В,
        Г,
        Д,
        Е,
        Ё,
        Ж,
        З,
        И,
        Й,
        К,
        Л,
        М,
        Н,
        О,
        П;
    }

    public enum letter2 {
        none,
        Р,
        С,
        Т,
        У,
        Ф,
        Х,
        Ц,
        Ч,
        Ш,
        Щ,
        Ъ,
        Ы,
        Ь,
        Э,
        Ю,
        Я;
    }
}

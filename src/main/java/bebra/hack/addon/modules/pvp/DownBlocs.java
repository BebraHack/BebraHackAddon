package bebra.hack.addon.modules.pvp;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Collections;
import java.util.List;

public class DownBlocs extends Module {
    public DownBlocs() {super(Addon.combat, "DownBlocs", "puts a block under the opponent, which prevents him from using the surround");}

    public enum mineMode {Normal, Instant}

    private final SettingGroup sgGeneral = settings.createGroup("General");
    private final SettingGroup sgBreak = settings.createGroup("Block Miner");

    private final Setting<Double> targetRange = sgGeneral.add(new DoubleSetting.Builder().name("target-range").description("The range at which players can be targeted.").defaultValue(4).min(0).sliderMax(7).build());
    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>().name("target-priority").description("How to filter the players to target.").defaultValue(SortPriority.LowestHealth).build());
    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder().name("block").description("What blocks to use for surround.").defaultValue(Collections.singletonList(Blocks.ENDER_CHEST)).filter(this::blockFilter).build());
    private final Setting<Boolean> autoToggle = sgGeneral.add(new BoolSetting.Builder().name("auto-toggle").description(" ").defaultValue(false).build());
    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder().name("rotate").description(" ").defaultValue(false).build());

    private final Setting<Boolean> autoBreak = sgBreak.add(new BoolSetting.Builder().name("auto-break").description("attemps to auto break").defaultValue(false).build());
    public final Setting<mineMode> breakMode = sgBreak.add(new EnumSetting.Builder<mineMode>().name("break-mode").defaultValue(mineMode.Normal).visible(() -> autoBreak.get()).build());
    private final Setting<Boolean> breakRender = sgBreak.add(new BoolSetting.Builder().name("render").description("Renders an overlay where blocks will be placed.").defaultValue(true).build());
    private final Setting<ShapeMode> breakShapeMode = sgBreak.add(new EnumSetting.Builder<ShapeMode>().name("shape-mode").description("How the shapes are rendered.").defaultValue(ShapeMode.Both).build());
    private final Setting<SettingColor> breakSideColor = sgBreak.add(new ColorSetting.Builder().name("side-color").description("The side color of the target block rendering.").defaultValue(new SettingColor(0, 0, 255, 60)).build());
    private final Setting<SettingColor> breakLineColor = sgBreak.add(new ColorSetting.Builder().name("line-color").description("The line color of the target block rendering.").defaultValue(new SettingColor(0, 0, 255, 190)).build());

    private PlayerEntity target;
    private Direction direction;
    private boolean b, placed;
    FindItemResult blockk, pickaxe;


    @EventHandler
    private void onTick(TickEvent.Post event) {

        placed = false;
        target = TargetUtils.getPlayerTarget(targetRange.get(), priority.get());
        blockk = InvUtils.findInHotbar(itemStack -> blocks.get().contains(Block.getBlockFromItem(itemStack.getItem())));
        pickaxe = InvUtils.findInHotbar(Items.NETHERITE_PICKAXE);
        if (target == null) {
            info("Target no found.");
            placed = false;
            toggle();
        } else {
            if (!blockk.found()) {
                info("Blocks no found.");
                placed = false;
                toggle();
            } else {
                if (!pickaxe.found()) {
                    info("Pickaxe no found.");
                    placed = false;
                    toggle();
                } else {

                    if (mc.world.getBlockState(target.getBlockPos().down()).getBlock() == Blocks.BEDROCK) {
                        info("Target block no found");
                        placed = false;
                        toggle();
                    } else {
                        if (!(mc.world.getBlockState(target.getBlockPos().down()).getBlock() == Blocks.BEDROCK)) {
                            if (!checkBreak(target.getBlockPos().down()) && autoBreak.get()) {
                                info("Mining block " + target.getName() + "!");
                                mine(target.getBlockPos().down(), pickaxe);
                            } else {
                                if (mc.world.getBlockState(target.getBlockPos().down()).getBlock() == Blocks.AIR) {
                                    BlockUtils.place(target.getBlockPos().down(), blockk, rotate.get(), 100, true, true, true); {if (autoToggle.get()) toggle();}
                                    placed = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void mine(BlockPos blockPos, FindItemResult item) {

        if (breakMode.get() == mineMode.Normal) {
            InvUtils.swap(item.slot(), false);
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
            mc.player.swingHand(Hand.MAIN_HAND);
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
        }
        if (breakMode.get() == mineMode.Instant) {
            InvUtils.swap(item.slot(), false);

            if (!b) {
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
                b = true;
            }
            if (rotate.get()) {
                Rotations.rotate(Rotations.getYaw(blockPos), Rotations.getPitch(blockPos), () -> mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, direction)));
            } else {
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, direction));
            }
            mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
        }
    }



    @EventHandler
    private void onRender(Render3DEvent event) {
        if (breakRender.get() && target != null && autoBreak.get()) {
            if (!checkBreak(target.getBlockPos().add(0, -1, 0))) {
                event.renderer.box(target.getBlockPos().add(0, -1, 0), breakSideColor.get(), breakLineColor.get(), breakShapeMode.get(), 0);
            }
        }
    }




    public boolean checkBreak(BlockPos Pos) {
        return mc.world.getBlockState(Pos).getBlock() == Blocks.ENDER_CHEST ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.SOUL_SAND ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.SOUL_CAMPFIRE ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.CAMPFIRE ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.DARK_OAK_SLAB ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.JUNGLE_SLAB ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.SPRUCE_SLAB ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.WARPED_SLAB ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.CRIMSON_SLAB ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.OAK_SLAB ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.COBWEB ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.AIR ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.ENCHANTING_TABLE ||
            mc.world.getBlockState(Pos).getBlock() == Blocks.ACACIA_SLAB;
    }

    private boolean blockFilter(Block block) {
        return block == Blocks.ENDER_CHEST ||
            block == Blocks.SOUL_SAND ||
            block == Blocks.SOUL_CAMPFIRE ||
            block == Blocks.CAMPFIRE ||
            block == Blocks.DARK_OAK_SLAB ||
            block == Blocks.JUNGLE_SLAB ||
            block == Blocks.SPRUCE_SLAB ||
            block == Blocks.WARPED_SLAB ||
            block == Blocks.CRIMSON_SLAB ||
            block == Blocks.OAK_SLAB ||
            block == Blocks.ENCHANTING_TABLE ||
            block == Blocks.COBWEB ||
            block == Blocks.ACACIA_SLAB;
    }
}

package bebra.hack.addon.modules.pve;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.events.entity.player.StartBreakingBlockEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.*;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class AutoExit extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgAutoBreak = settings.createGroup("Auto-Break");
    private final SettingGroup sgPause = settings.createGroup("Pause");
    private final SettingGroup sgObsidianRender = settings.createGroup("Obsidian-Render");
    private final SettingGroup sgSANDRender = settings.createGroup("Sand-Render");
    private final SettingGroup sgBreakRender = settings.createGroup("Break-Render");

    public enum mineMode {Normal, Instant;}

    //general
    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder().name("target-range").description("max range to target").defaultValue(4).build());
    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder().name("place-delay").description("How many ticks between obsidian placement").defaultValue(1).build());
    private final Setting<Boolean> noairplace = sgGeneral.add(new BoolSetting.Builder().name("noairplace").description("no air place").defaultValue(false).build());
    private final Setting<Boolean> autotrap = sgGeneral.add(new BoolSetting.Builder().name("autotrap").description("for air place").defaultValue(false).build());
    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder().name("rotate").description("Rotates towards blocks when interacting").defaultValue(false).build());
    //auto break
    //private final Setting<Boolean> autoBreak = sgAutoBreak.add(new BoolSetting.Builder().name("auto-break").description("attemps to auto break").defaultValue(false).build());
    //public final Setting<tntauraBEBRAplus.mineMode> breakMode = sgAutoBreak.add(new EnumSetting.Builder<tntauraBEBRAplus.mineMode>().name("break-mode").defaultValue(mineMode.Normal).visible(() -> autoBreak.get()).build());
    //pause
    private final Setting<Boolean> burrowPause = sgPause.add(new BoolSetting.Builder().name("pause-on-burrow").description("will pause if enemy is burrowed").defaultValue(true).build());
    private final Setting<Boolean> antiSelf = sgPause.add(new BoolSetting.Builder().name("anti-self").description("pause if enemy in your hole").defaultValue(true).build());
    private final Setting<Boolean> holePause = sgPause.add(new BoolSetting.Builder().name("only-in-hole").description("pause if enemy isnt in hole").defaultValue(false).build());
    private final Setting<Boolean> pauseOnEat = sgPause.add(new BoolSetting.Builder().name("pause-on-eat").description("Pauses while eating.").defaultValue(true).build());
    private final Setting<Boolean> pauseOnDrink = sgPause.add(new BoolSetting.Builder().name("pause-on-drink").description("Pauses while drinking.").defaultValue(true).build());
    private final Setting<Boolean> pauseOnMine = sgPause.add(new BoolSetting.Builder().name("pause-on-mine").description("Pauses while mining.").defaultValue(true).build());
    // Obsidian render
    private final Setting<Boolean> obsidianRender = sgObsidianRender.add(new BoolSetting.Builder().name("render").description("Renders an overlay where blocks will be placed.").defaultValue(true).build());
    private final Setting<ShapeMode> obsidianShapeMode = sgObsidianRender.add(new EnumSetting.Builder<ShapeMode>().name("shape-mode").description("How the shapes are rendered.").defaultValue(ShapeMode.Both).build());
    private final Setting<SettingColor> obsidianSideColor = sgObsidianRender.add(new ColorSetting.Builder().name("side-color").description("The side color of the target block rendering.").defaultValue(new SettingColor(0, 255, 0, 60)).build());
    private final Setting<SettingColor> obsidianLineColor = sgObsidianRender.add(new ColorSetting.Builder().name("line-color").description("The line color of the target block rendering.").defaultValue(new SettingColor(0, 255, 0, 190)).build());
    private final Setting<SettingColor> obsidianNextSideColor = sgObsidianRender.add(new ColorSetting.Builder().name("next-side-color").description("The side color of the next block to be placed.").defaultValue(new SettingColor(255, 0, 0, 60)).build());
    private final Setting<SettingColor> obsidianNextLineColor = sgObsidianRender.add(new ColorSetting.Builder().name("next-line-color").description("The line color of the next block to be placed.").defaultValue(new SettingColor(255, 0, 0, 190)).build());
    //mine render
    private final Setting<Boolean> breakRender = sgBreakRender.add(new BoolSetting.Builder().name("render").description("Renders an overlay where blocks will be placed.").defaultValue(true).build());
    private final Setting<ShapeMode> breakShapeMode = sgBreakRender.add(new EnumSetting.Builder<ShapeMode>().name("shape-mode").description("How the shapes are rendered.").defaultValue(ShapeMode.Both).build());
    private final Setting<SettingColor> breakSideColor = sgBreakRender.add(new ColorSetting.Builder().name("side-color").description("The side color of the target block rendering.").defaultValue(new SettingColor(0, 0, 255, 60)).build());
    private final Setting<SettingColor> breakLineColor = sgBreakRender.add(new ColorSetting.Builder().name("line-color").description("The line color of the target block rendering.").defaultValue(new SettingColor(0, 0, 255, 190)).build());

    private PlayerEntity target;
    private final List<BlockPos> obsidianPos = new ArrayList<>();
    private int ticks;
    private Direction direction;
    private boolean rofl;
    private boolean toggled;

    public AutoExit() {
        super(Addon.combat, "AutoExit", "Crash maincraft");
    }

    @Override
    public void onActivate() {
        obsidianPos.clear();
        ticks = 0;
        rofl = false;
        toggled = false;
    }

    @Override
    public void onDeactivate() {
        obsidianPos.clear();
    }

    private void onStartBreakingBlock(StartBreakingBlockEvent event) {
        direction = event.direction;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {

        //auto dissable
        FindItemResult obsidian = InvUtils.findInHotbar(Items.OBSIDIAN);

        if (!obsidian.isHotbar() && !toggled) {
            obsidianPos.clear();
            ChatUtils.error("No obsidian found");
            toggle();
            toggled = true;
        }

        FindItemResult flint = InvUtils.findInHotbar(Items.SAND);

        if (!flint.isHotbar() && !toggled) {
            obsidianPos.clear();
            ChatUtils.error("No sand found");
            toggle();
            toggled = true;
        }

        FindItemResult pickaxe = InvUtils.find(itemStack -> itemStack.getItem() == Items.DIAMOND_PICKAXE || itemStack.getItem() == Items.NETHERITE_PICKAXE);

        if (!pickaxe.isHotbar() && !toggled) {
            obsidianPos.clear();
            ChatUtils.error("No pickaxe found");
            toggle();
            toggled = true;
        }

        if (TargetUtils.isBadTarget(target, range.get())) {
            target = TargetUtils.getPlayerTarget(range.get(), SortPriority.LowestDistance);
        }

        if (target == null) return;

        if (burrowPause.get() && isBurrowed(target) && !toggled) {
            obsidianPos.clear();
            ChatUtils.error("Target is burrowed");
            toggle();
            toggled = true;
        }

        if (antiSelf.get() && antiSelf(target) && !toggled) {
            obsidianPos.clear();
            ChatUtils.error("Target in your hole!");
            toggle();
            toggled = true;
        }

        if (holePause.get() && !isSurrounded(target) && !toggled) {
            obsidianPos.clear();
            ChatUtils.error("Target isnt surrounded");
            toggle();
            toggled = true;
        }

        if (TargetUtils.isBadTarget(target, range.get()) && !toggled) {
            ChatUtils.error("Enemy is too daleko");
            toggle();
            toggled = true;
        }

        if (PlayerUtils.shouldPause(pauseOnMine.get(), pauseOnEat.get(), pauseOnDrink.get())) return;

        //if (!mineBlockstate(target.getBlockPos().up(2)) && autoBreak.get()) {
        //    mine(target.getBlockPos().up(2), pickaxe);
        //}

        placeObsidian(target);

        if (ticks >= delay.get() && obsidianPos.size() > 0) {
            BlockPos blockPos = obsidianPos.get(obsidianPos.size() - 1);

            if (BlockUtils.place(blockPos, obsidian, rotate.get(), 50, true)) {
                obsidianPos.remove(blockPos);
            }
            ticks = 0;
        } else {
            ticks++;
        }
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        //obsidian render
        if (obsidianRender.get() && !obsidianPos.isEmpty()) {
            for (BlockPos pos : obsidianPos) {
                boolean isFirst = pos.equals(obsidianPos.get(obsidianPos.size() - 1));

                Color side = isFirst ? obsidianNextSideColor.get() : obsidianSideColor.get();
                Color line = isFirst ? obsidianNextLineColor.get() : obsidianLineColor.get();

                event.renderer.box(pos, side, line, obsidianShapeMode.get(), 0);
            }
        }

        //if (breakRender.get() && target != null && autoBreak.get()) {
            if (!mineBlockstate(target.getBlockPos().add(0, 2, 0))) {
                event.renderer.box(target.getBlockPos().add(0, 2, 0), breakSideColor.get(), breakLineColor.get(), breakShapeMode.get(), 0);
            }
        }
    //}

    private void placeObsidian(PlayerEntity target) {
        FindItemResult obsidian = InvUtils.findInHotbar(Items.OBSIDIAN);
        obsidianPos.clear();
        BlockPos targetPos = target.getBlockPos();

        if(noairplace.get()) {
            add(targetPos.add(0, 3, 0));
            add(targetPos.add(-1, 3, 0));
            add(targetPos.add(0, 2, -1));
            add(targetPos.add(0, 2, 1));
            add(targetPos.add(-1, 2, 0));
            add(targetPos.add(1, 2, 0));
            add(targetPos.add(0, 1, 1));
            add(targetPos.add(1, 1, 0));
            add(targetPos.add(0, 1, -1));
            add(targetPos.add(-1, 1, 0));
        }

        add(targetPos.add(0, 3, 0));
        add(targetPos.add(0, 2, -1));
        add(targetPos.add(0, 2, 1));
        add(targetPos.add(-1, 2, 0));
        add(targetPos.add(1, 2, 0));
        add(targetPos.add(0, 1, 1));
        add(targetPos.add(1, 1, 0));
        add(targetPos.add(0, 1, -1));
        add(targetPos.add(-1, 1, 0));


    }

    private void placeTNT(PlayerEntity target) {
        FindItemResult tnt = InvUtils.findInHotbar(Items.SAND);
        BlockPos targetPos = target.getBlockPos();
        BlockUtils.place(targetPos.add(0, 2, 0), tnt, rotate.get(), 50, true, true);


    }

    private void add(BlockPos blockPos) {
        if (!obsidianPos.contains(blockPos) && BlockUtils.canPlace(blockPos)) obsidianPos.add(blockPos);
    }


    public boolean sandBlockstate(BlockPos Pos) {
        return mc.world.getBlockState(Pos).getBlock() == Blocks.AIR
            || mc.world.getBlockState(Pos).getBlock() == Blocks.SAND;
    }

    public boolean allowSAND(LivingEntity target) {
        assert mc.world != null;

        return !mc.world.getBlockState(target.getBlockPos().add(1, 2, 0)).isAir() && !mc.world.getBlockState(target.getBlockPos().add(-1, 2, 0)).isAir() && !mc.world.getBlockState(target.getBlockPos().add(0, 2, 1)).isAir() && !mc.world.getBlockState(target.getBlockPos().add(0, 2, -1)).isAir() && !mc.world.getBlockState(target.getBlockPos().add(0, 3, 0)).isAir();
    }

    public boolean mineBlockstate(BlockPos Pos) {
        return mc.world.getBlockState(Pos).getBlock() == Blocks.AIR
            || mc.world.getBlockState(Pos).getBlock() == Blocks.SAND
            || mc.world.getBlockState(Pos).getBlock() == Blocks.BEDROCK;

    }

    public void mine(BlockPos blockPos, FindItemResult item) {

        //if (breakMode.get() == mineMode.Normal) {
        //    InvUtils.swap(item.slot(), false);
        //    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
        //    mc.player.swingHand(Hand.MAIN_HAND);
        //    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
        //}
        //if (breakMode.get() == mineMode.Instant) {
        //    InvUtils.swap(item.slot(), false);

            if (!rofl) {
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
                rofl = true;
            }
            if (rotate.get()) {
                Rotations.rotate(Rotations.getYaw(blockPos), Rotations.getPitch(blockPos), () -> mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, direction)));
            } else {
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, direction));
            }
            mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
        }
    //}

    private boolean isBurrowed(LivingEntity target) {
        assert mc.world != null;

        return !mc.world.getBlockState(target.getBlockPos()).isAir();
    }

    private boolean isSurrounded(LivingEntity target) {
        assert mc.world != null;

        return !mc.world.getBlockState(target.getBlockPos().add(1, 0, 0)).isAir() && !mc.world.getBlockState(target.getBlockPos().add(-1, 0, 0)).isAir() && !mc.world.getBlockState(target.getBlockPos().add(0, 0, 1)).isAir() && !mc.world.getBlockState(target.getBlockPos().add(0, 0, -1)).isAir();
    }

    private boolean antiSelf(LivingEntity target){

        if (!(mc.player.getBlockPos().getX() == target.getBlockPos().getX() && mc.player.getBlockPos().getZ() == target.getBlockPos().getZ() && mc.player.getBlockPos().getY() == target.getBlockPos().getY())) return false;
        return true;
    }

    @Override
    public String getInfoString() {
        return EntityUtils.getName(target);
    }
}

package bebra.hack.addon.modules.pvp;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import meteordevelopment.meteorclient.events.render.Render3DEvent;


import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import java.util.ArrayList;
import java.util.List;
import bebra.hack.addon.Utils.HelperUtil;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;




//import java.util.Comparator;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.stream.StreamSupport;

public class Blocker extends Module {
    public Blocker() {
        super(Addon.combat, "Blocker", "Makes your surround harder to break");
    }

    public final SettingGroup sgGeneral = settings.getDefaultGroup();


    private final Setting<Integer> blocksPerTick = sgGeneral.add(new IntSetting.Builder()
        .name("Blocks per tick")
        .description("Amount of maximum placed blocks in 1 tick (1 tick = 0.05 seconds)")
        .defaultValue(5)
        .range(0, 5)
        .sliderRange(0, 5)
        .build()
    );



    public final Setting<Boolean> swing = sgGeneral.add(new BoolSetting.Builder()
        .name("Swing")
        .description("Swing")
        .defaultValue(false)
        .build()
    );


    public final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("Rotate")
        .description("Rotation.")
        .defaultValue(false)
        .build()
    );

    public final Setting<Boolean> face = sgGeneral.add(new BoolSetting.Builder()
        .name("Face")
        .description("Face extend")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> extendStraight = sgGeneral.add(new BoolSetting.Builder()
        .name("Straight extend")
        .description("Straight extend")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> extendDiagonal = sgGeneral.add(new BoolSetting.Builder()
        .name("Diagonal extend")
        .description("Diagonal extend")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> cev = sgGeneral.add(new BoolSetting.Builder()
        .name("Cev")
        .description("Cev extend")
        .defaultValue(false)
        .build()
    );

    public final Setting<Boolean> extracev = sgGeneral.add(new BoolSetting.Builder()
        .name("Extra cev")
        .description("Extra cev extend for better protection")
        .defaultValue(false)
        .build()
    );

    public final Setting<Boolean> civ = sgGeneral.add(new BoolSetting.Builder()
        .name("Civ")
        .description("Civ extend (cev but on sides)")
        .defaultValue(false)
        .build()
    );



    public final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Boolean> Render = sgRender.add(new BoolSetting.Builder()
        .name("Render")
        .description("Render Setting")
        .defaultValue(false)
        .build()
    );

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("How the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("The side color of the rendering.")
        .defaultValue(new SettingColor(225, 0, 0, 75))
        .build()
    );


    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The line color of the rendering.")
        .defaultValue(new SettingColor(225, 0, 0, 255))
        .build()
    );



    private final List<BlockPos> placePositions = new ArrayList<>();
    FindItemResult obsidian;
    private int counter = 0;
    private int phase = 1;
    private final ArrayList<BlockPos> poses = new ArrayList<>();
    private int range = 3;



    private boolean isObby(BlockPos bp){
        return mc.world.getBlockState(bp).getBlock() == Blocks.OBSIDIAN;
    }

    private boolean isBedrock(BlockPos bp){
        return mc.world.getBlockState(bp).getBlock() == Blocks.BEDROCK;
    }

    private boolean isAir(BlockPos bp){
        return mc.world.getBlockState(bp).getBlock() == Blocks.AIR;
    }

    public boolean validObi(BlockPos pos) {
        return !validBedrock(pos)
            && (isObby(pos.add(0, -1, 0)) || isBedrock(pos.add(0, -1, 0)))
            && (isObby(pos.add(1, 0, 0)) || isBedrock(pos.add(1, 0, 0)))
            && (isObby(pos.add(-1, 0, 0)) || isBedrock(pos.add(-1, 0, 0)))
            && (isObby(pos.add(0, 0, 1)) || isBedrock(pos.add(0, 0, 1)))
            && (isObby(pos.add(0, 0, -1)) || isBedrock(pos.add(0, 0, -1)))
            && isAir(pos)
            && isAir(pos.add(0, 1, 0))
            && isAir(pos.add(0, 2, 0));
    }

    public boolean validBedrock(BlockPos pos) {
        return isBedrock(pos.add(0, -1, 0))
            && isBedrock(pos.add(1, 0, 0))
            && isBedrock(pos.add(-1, 0, 0))
            && isBedrock(pos.add(0, 0, 1))
            && isBedrock(pos.add(0, 0, -1))
            && isAir(pos)
            && isAir(pos.add(0, 1, 0))
            && isAir(pos.add(0, 2, 0));
    }









    @Override
    public void onDeactivate() {
        placePositions.clear();
    }

    @Override
    public void onActivate() {
        placePositions.clear();
    }



    @EventHandler
    public void onTick(TickEvent.Post event) {

        obsidian = HelperUtil.obby();

        if (!obsidian.found()) {
            warning("u need obsidian.");
            toggle();
            return;
        }

        if (placePositions.isEmpty()) return;





        for (BlockPos poses : placePositions) {
            if(isAir(poses)) {
                BlockUtils.place(poses, obsidian, rotate.get(), 50, true);
                mc.player.handSwinging = swing.get(); // cancel hand swing
                if (counter > blocksPerTick.get()) {
                    counter = 0;
                    return;
                }
            }
            if (placePositions.isEmpty()) return;
            counter++;
        }

    }


    @EventHandler
    public void onRender(Render3DEvent event) {
        if (placePositions == null) return;

        for (BlockPos poses : placePositions)
            event.renderer.box(poses, sideColor.get(), lineColor.get(), shapeMode.get(), 1);
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive e) {
        //if (e.packet instanceof EntityPositionS2CPacket) {
        if (e.packet instanceof BlockBreakingProgressS2CPacket && validObi(BlockPos.ofFloored(mc.player.getPos()))) {
            //warning("first check");
            //BlockBreakingProgressS2CPacket packet = e.packet;
            BlockBreakingProgressS2CPacket packet = (BlockBreakingProgressS2CPacket) e.packet;
            BlockPos pos = packet.getPos();

            if (mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos).getBlock() == Blocks.AIR){
                return;
            }


            BlockPos playerPos = BlockPos.ofFloored(mc.player.getPos());

            placePositions.clear();

            if(extendStraight.get()){
                if(pos.equals(playerPos.north())){
                    placePositions.add(playerPos.north().north());
                }
                if(pos.equals(playerPos.south())){
                    placePositions.add(playerPos.south().south());
                }
                if(pos.equals(playerPos.east())){
                    placePositions.add(playerPos.east().east());
                }
                if(pos.equals(playerPos.west())){
                    placePositions.add(playerPos.west().west());
                }
            }

            if(extendDiagonal.get()){
                if(pos.equals(playerPos.north())){
                    placePositions.add(playerPos.north().east());
                    placePositions.add(playerPos.north().west());
                }
                if(pos.equals(playerPos.south())){
                    placePositions.add(playerPos.south().west());
                    placePositions.add(playerPos.south().east());
                }
                if(pos.equals(playerPos.east())){
                    placePositions.add(playerPos.east().north());
                    placePositions.add(playerPos.east().south());
                }
                if(pos.equals(playerPos.west())){
                    placePositions.add(playerPos.west().south());
                    placePositions.add(playerPos.west().north());
                }
            }


            if(face.get()){
                if(pos.equals(playerPos.north())){
                    placePositions.add(playerPos.north().up());
                }
                if(pos.equals(playerPos.south())){
                    placePositions.add(playerPos.south().up());
                }
                if(pos.equals(playerPos.east())){
                    placePositions.add(playerPos.east().up());
                }
                if(pos.equals(playerPos.west())){
                    placePositions.add(playerPos.west().up());
                }
            }

            if(cev.get()) {
                if (pos.equals(playerPos.up().up())) {
                    //warning("cev");
                    placePositions.add(playerPos.up().up().up());
                    placePositions.add(playerPos.up().up().up().up());
                }
            }

            if(extracev.get()){
                if (pos.equals(playerPos.up().up().up())) {
                    placePositions.add(playerPos.up().up());
                    placePositions.add(playerPos.up().up().up().up());
                }
                if (pos.equals(playerPos.up().up().up().up())) {
                    placePositions.add(playerPos.up().up().up());
                    placePositions.add(playerPos.up().up().up().up());
                }
            }

            if(civ.get()){
                if (pos.equals(playerPos.north().up())) {
                    placePositions.add(playerPos.north().up().up());
                }
                if (pos.equals(playerPos.south().up())) {
                    placePositions.add(playerPos.south().up().up());
                }
                if (pos.equals(playerPos.east().up())) {
                    placePositions.add(playerPos.east().up().up());
                }
                if (pos.equals(playerPos.west().up())) {
                    placePositions.add(playerPos.west().up().up());
                }
            }






        }
    }
}

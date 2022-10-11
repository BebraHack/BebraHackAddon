package com.example.addon.modules.BebraHackPVP;

import com.example.addon.Addon;
import com.example.addon.Utils.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import meteordevelopment.meteorclient.events.entity.EntityAddedEvent;
import meteordevelopment.meteorclient.events.entity.EntityRemovedEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.BlockUpdateEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.ClientPlayerInteractionManagerAccessor;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.utils.player.DamageUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrystalAyraBEBRA extends Module {
    public final SettingGroup sgGeneral = settings.getDefaultGroup();
    public final SettingGroup sgPrediction = settings.createGroup("Prediction");
    public final SettingGroup sgDamage = settings.createGroup("Damage");
    public final SettingGroup sgMiscellaneous = settings.createGroup("Miscellaneous");
    public final SettingGroup sgRender = settings.createGroup("Render");

    public final Setting<Place.Swap> swap = sgGeneral.add(new EnumSetting.Builder<Place.Swap>().name("swap").defaultValue(Place.Swap.Move).build());
    public final Setting<Boolean> inventory = sgGeneral.add(new BoolSetting.Builder().name("inventory").defaultValue(true).build());
    public final Setting<Integer> swapDelay = sgGeneral.add(new IntSetting.Builder().name("swap-delay").defaultValue(0).range(0, 20).visible(() -> swap.get() != Place.Swap.OFF).build());

    public final Setting<Integer> placeDelay = sgGeneral.add(new IntSetting.Builder().name("place-delay").defaultValue(0).range(0, 10).build());
    public final Setting<Double> placeRange = sgGeneral.add(new DoubleSetting.Builder().name("place-range").defaultValue(4.5).range(0, 7).build());
    public final Setting<Integer> breakDelay = sgGeneral.add(new IntSetting.Builder().name("break-delay").defaultValue(2).range(0, 10).build());
    public final Setting<Double> breakRange = sgGeneral.add(new DoubleSetting.Builder().name("break-range").defaultValue(4.5).range(0, 7).build());
    public final Setting<FastBreak> fastBreak = sgGeneral.add(new EnumSetting.Builder<FastBreak>().name("fast-break").defaultValue(FastBreak.Instant).build());
    public final Setting<Frequency> frequency = sgGeneral.add(new EnumSetting.Builder<Frequency>().name("frequency").defaultValue(Frequency.Divide).build());
    public final Setting<Integer> value = sgGeneral.add(new IntSetting.Builder().name("value").defaultValue(8).range(0, 20).visible(() -> frequency.get() != Frequency.OFF).build());
    public final Setting<Integer> age = sgGeneral.add(new IntSetting.Builder().name("age").defaultValue(0).range(0, 5).build());
    public final Setting<Boolean> oneTwelve = sgGeneral.add(new BoolSetting.Builder().name("1.12").defaultValue(false).build());
    public final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder().name("rotate").defaultValue(false).build());
    public final Setting<Boolean> ignoreTerrain = sgGeneral.add(new BoolSetting.Builder().name("ignore-terrain").defaultValue(false).build());
    public final Setting<Integer> blockUpdate = sgGeneral.add(new IntSetting.Builder().name("block-update").defaultValue(0).range(0, 200).build());
    public final Setting<Priority> priority = sgGeneral.add(new EnumSetting.Builder<Priority>().name("priority").defaultValue(Priority.Break).build());

    public final Setting<Boolean> predict = sgPrediction.add(new BoolSetting.Builder().name("prediction").defaultValue(false).build());
    public final Setting<Boolean> collision = sgPrediction.add(new BoolSetting.Builder().name("collision").defaultValue(false).visible(predict::get).build());
    public final Setting<Double> offset = sgPrediction.add(new DoubleSetting.Builder().name("offset").defaultValue(0.50).range(0.0, 3.0).visible(predict::get).build());
    public final Setting<Boolean> predictID = sgPrediction.add(new BoolSetting.Builder().name("predict-ID").defaultValue(false).build());
    public final Setting<Integer> delayID = sgPrediction.add(new IntSetting.Builder().name("delay-ID").defaultValue(0).range(0, 5).visible(predictID::get).build());

    public final Setting<DoPlace> doPlace = sgDamage.add(new EnumSetting.Builder<DoPlace>().name("place").defaultValue(DoPlace.BestDMG).build());
    public final Setting<DoBreak> doBreak = sgDamage.add(new EnumSetting.Builder<DoBreak>().name("break").defaultValue(DoBreak.BestDMG).build());
    public final Setting<Double> minDmg = sgDamage.add(new DoubleSetting.Builder().name("min-dmg").defaultValue(7.5).range(0, 36).build());
    public final Setting<Double> safety = sgDamage.add(new DoubleSetting.Builder().name("safety").defaultValue(25).range(0, 100).build());
    public final Setting<Boolean> antiSelfPop = sgDamage.add(new BoolSetting.Builder().name("anti-self-pop").defaultValue(false).build());
    public final Setting<Boolean> antiFriendDamage = sgDamage.add(new BoolSetting.Builder().name("anti-friend-damage").defaultValue(false).build());
    public final Setting<Double> friendMaxDmg = sgDamage.add(new DoubleSetting.Builder().name("friend-max-dmg").defaultValue(25).range(0, 100).visible(antiFriendDamage::get).build());

    public final Setting<SurroundBreak> surroundBreak = sgMiscellaneous.add(new EnumSetting.Builder<SurroundBreak>().name("surround-break").defaultValue(SurroundBreak.OnMine).build());
    public final Setting<Boolean> eatPause = sgMiscellaneous.add(new BoolSetting.Builder().name("eat-pause").defaultValue(false).build());
    public final Setting<Boolean> minePause = sgMiscellaneous.add(new BoolSetting.Builder().name("mine-pause").defaultValue(false).build());

    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder().name("render").defaultValue(true).build());
    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>().name("shape-mode").defaultValue(ShapeMode.Both).visible(render::get).build());
    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder().name("side-color").defaultValue(new SettingColor(255, 0, 170, 10)).visible(render::get).build());
    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder().name("line-color").defaultValue(new SettingColor(255, 0, 170, 90)).visible(render::get).build());

    public CrystalAyraBEBRA() {
        super(Addon.combat, "CrystalAuraBEBRA", "Automatically places and blows up crystals.");
    }

    private final ExecutorService thread = Executors.newCachedThreadPool();

    public BlockPosX updatedBlock, renderPos, bestPos;
    public int attacks, ticksPassed;

    public double bestDamage = 0;

    public int lastEntityId, last;

    public final int[] second = new int[20];
    public static int cps;
    public int tick, i, lastSpawned = 20;

    private final IntSet brokenCrystals = new IntOpenHashSet();

    private final List<Location> currLocation = new ArrayList<>();
    private final List<Location> prevLocation = new ArrayList<>();

    private final AAABLYA
        placeTimer = new AAABLYA(),
        breakTimer = new AAABLYA(),
        blockTimer = new AAABLYA(),
        swapTimer = new AAABLYA(),
        idTimer = new AAABLYA();

    @Override
    public void onActivate() {
        updatedBlock = null;
        renderPos = null;
        bestPos = null;

        tick = 0;
        Arrays.fill(second, 0);
        i = 0;

        brokenCrystals.clear();

        placeTimer.reset();
        blockTimer.reset();
        swapTimer.reset();
        idTimer.reset();
    }

    // Predict
    @EventHandler
    private void onPre(TickEvent.Pre event) {
        prevLocation.clear();

        for (PlayerEntity player : mc.world.getPlayers()) {
            prevLocation.add(new Location(player));
        }
    }

    @EventHandler
    private void onPost(TickEvent.Post event) {
        currLocation.clear();

        for (PlayerEntity player : mc.world.getPlayers()) {
            currLocation.add(new Location(player));
        }
    }


    // Main code
    @EventHandler
    public void onTick(TickEvent.Post event) {
        getCPS();
        updateBlock();

        if (ticksPassed >= 0) ticksPassed--;
        else {
            ticksPassed = 20;
            attacks = 0;
        }

        if (eatPause.get() && mc.player.isUsingItem() && (mc.player.getMainHandStack().isFood() || mc.player.getOffHandStack().isFood()))
            return;
        if (minePause.get() && mc.interactionManager.isBreakingBlock()) return;

        thread.execute(this::doSurroundBreak);
        thread.execute(this::doCalculate);

        if (priority.get() == Priority.Place) {
            doPlace();
            doBreak();
        } else {
            doBreak();
            doPlace();
        }
    }

    @EventHandler
    public void onAdded(EntityAddedEvent event) {
        if (!(event.entity instanceof EndCrystalEntity)) return;

        if (fastBreak.get() == FastBreak.Instant || fastBreak.get() == FastBreak.Both) {
            if (bestPos == null) return;
            BlockPosX entityPos = new BlockPosX(event.entity.getBlockPos().down());

            if (bestPos.equals(entityPos)) {
                doBreak(event.entity, false);
            }
        }

        last = event.entity.getId() - lastEntityId;
        lastEntityId = event.entity.getId();
    }

    @EventHandler
    public void onRemove(EntityRemovedEvent event) {
        if (!(event.entity instanceof EndCrystalEntity)) return;

        if (brokenCrystals.contains(event.entity.getId())) {
            lastSpawned = 20;
            tick++;

            removeId(event.entity);
        }
    }

    @EventHandler
    private void onSend(PacketEvent.Send event) {
        if (event.packet instanceof UpdateSelectedSlotC2SPacket) {
            swapTimer.reset();
        }
    }

    @EventHandler
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (event.newState.isAir()) {
            updatedBlock = new BlockPosX(event.pos);
            blockTimer.reset();
        }
    }

    private void doPlace() {
        doPlace(bestPos);
    }

    private void doPlace(BlockPosX blockPos) {
        if (blockPos == null) bestDamage = 0;
        if (blockPos == null || !placeTimer.passedTicks(placeDelay.get())) return;

        FindItemResult crystal = InvUtils.find(Items.END_CRYSTAL);
        if (!crystal.found()) return;

        Hand hand = mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL ? Hand.OFF_HAND : Hand.MAIN_HAND;
        if (blockPos.distance() > placeRange.get()) return;
        BlockHitResult hitResult = new BlockHitResult(blockPos.closestVec3d(), blockPos.getY() == 255 ? Direction.DOWN : Direction.UP, blockPos.get(), false);

        if (swap.get() != Place.Swap.OFF && swap.get() != Place.Swap.Move && hand != Hand.OFF_HAND && !crystal.isMainHand())
            InvUtils.swap(crystal.slot(), true);

        boolean moveSwap = ((swap.get() == Place.Swap.Move && crystal.isHotbar()) || inventory.get()) && hand != Hand.OFF_HAND;
        boolean holdsCrystal = (hand == Hand.OFF_HAND || crystal.isMainHand()) || moveSwap;
        if (holdsCrystal) {
            if (rotate.get()) Rotations.rotate(Rotations.getYaw(blockPos.get()), Rotations.getPitch(blockPos.get()));

            if (moveSwap) {
                Util.move(crystal, () -> mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, hitResult, 0)));
            } else mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, hitResult, 0));
            mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
        }

        if (predictID.get() && idTimer.passedTicks(delayID.get())) {
            EndCrystalEntity endCrystal = new EndCrystalEntity(mc.world, blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5);
            endCrystal.setShowBottom(false);
            endCrystal.setId(lastEntityId + last);

            doBreak(endCrystal, false);
            endCrystal.kill();
            idTimer.reset();
        }

        if (swap.get() == Place.Swap.Silent) InvUtils.swapBack();
        placeTimer.reset();
        setRender(blockPos);
    }

    private void doBreak() {
        doBreak(getCrystal(), true);
    }

    private void doBreak(Entity entity, boolean checkAge) {
        if (entity == null || !breakTimer.passedTicks(breakDelay.get()) || !swapTimer.passedTicks(swapDelay.get()) || !frequency() || (checkAge && entity.age < age.get()))
            return;

        if (Util.distanceTo(Util.closestVec3d(entity.getBoundingBox())) > breakRange.get()) return;
        mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking()));

        if (fastBreak.get() == FastBreak.Kill) {
            entity.kill();

            lastSpawned = 20;
            tick++;
        }

        addBroken(entity);
        attacks++;
        breakTimer.reset();
    }

    private void doSurroundBreak() {
        if (surroundBreak.get() == SurroundBreak.OFF) return;

        if (surroundBreak.get() == SurroundBreak.OnMine && !mc.interactionManager.isBreakingBlock()) return;
        List<BlockPosX> vulnerablePos = new ArrayList<>();

        try {
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (player == mc.player) continue;
                if (Friends.get().isFriend(player)) continue;
                if (!Util.isSurrounded(player)) continue;

                for (BlockPosX bp : Util.getSphere(player, 5)) {
                    if (Util.hasEntity(bp.box(), entity -> entity == mc.player || entity == player || entity instanceof ItemEntity))
                        continue;

                    boolean canPlace = bp.up().air() && (bp.of(Blocks.OBSIDIAN) || bp.of(Blocks.BEDROCK));

                    if (!canPlace) continue;
                    Vec3d vec3d = new Vec3d(bp.getX(), bp.getY() + 1, bp.getZ());
                    Box endCrystal = new Box(vec3d.x - 0.5, vec3d.y, vec3d.z - 0.5, vec3d.x + 1.5, vec3d.y + 2, vec3d.z + 1.5);

                    for (BlockPosX surround : Util.getSurroundBlocks(player, true)) {
                        if (surround.hardness() <= 0) return;

                        if (surroundBreak.get() == SurroundBreak.OnMine && mc.player.getMainHandStack().getItem() instanceof PickaxeItem) {
                            BlockPosX breakingPos = new BlockPosX(((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getCurrentBreakingBlockPos());

                            if (!surround.equals(breakingPos)) continue;
                        }
                        Box box = surround.box();

                        if (endCrystal.intersects(box)) vulnerablePos.add(bp);
                    }
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        if (vulnerablePos.isEmpty()) return;
        vulnerablePos.sort(Comparator.comparingDouble(Util::distanceTo));
        BlockPosX blockPos = vulnerablePos.get(0);

        if (Util.hasEntity(blockPos.up().box()) || Util.distanceTo(Util.closestVec3d(blockPos.box())) > placeRange.get())
            return;
        doPlace(blockPos);
    }

    private boolean someoneIntersects(List<PlayerEntity> list, BlockPosX blockPosX, double increase) {
        for (PlayerEntity player : list) {
            double distance = mc.player.distanceTo(player);
            double searchFactor = 3;

            if (distance > 6) searchFactor = (distance - 3) + increase;

            if (Util.hasEntity(blockPosX.box().stretch(0, oneTwelve.get() ? 1 : 2, 0), entity -> !(entity instanceof EndCrystalEntity)))
                continue;

            if (!mc.player.getBlockPos().isWithinDistance(blockPosX, placeRange.get())) return false;
            if (player.getBlockPos().isWithinDistance(blockPosX, searchFactor)) return true;
        }

        return false;
    }

    private List<PlayerEntity> getPlayers() {
        List<PlayerEntity> playerEntities = new ArrayList<>();

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player.isCreative() || player.isSpectator()) continue;
            if (Friends.get().isFriend(player)) continue;
            if (player == mc.player) continue;
            if (player.isDead()) continue;


            playerEntities.add(player);
        }

        playerEntities.sort(Comparator.comparingDouble(mc.player::distanceTo));
        return playerEntities;
    }

    private void doCalculate() {
        List<BlockPosX> sphere = Util.getSphere(mc.player, Math.ceil(placeRange.get()));
        List<PlayerEntity> players = getPlayers();
        if (!players.isEmpty()) {
            sphere = sphere.stream().filter(bp -> someoneIntersects(players, bp, 0)).toList();
        }

        BlockPosX bestPos = null;
        double bestDamage = 0.0;
        double safety;

        if (!players.isEmpty()) {
            try {
                for (BlockPosX bp : sphere) {
                    if (bp.distance() > placeRange.get()) continue;

                    boolean canPlace = bp.up().air() && (bp.of(Blocks.OBSIDIAN) || bp.of(Blocks.BEDROCK));
                    if (!canPlace) continue;

                    if (updatedBlock != null && updatedBlock.equals(bp.up())) continue;
                    if (oneTwelve.get() && !bp.up(2).air()) continue;

                    double targetDamage = getHighestDamage(Util.roundVec(bp));
                    double selfDamage = DamageUtils.crystalDamage(mc.player, Util.roundVec(bp));

                    safety = (targetDamage / 36 - selfDamage / 36) * 100;
                    if (safety < this.safety.get() || antiSelfPop.get() && selfDamage > mc.player.getHealth() + mc.player.getAbsorptionAmount())
                        continue;

                    boolean validPos = true;
                    if (antiFriendDamage.get()) {
                        for (PlayerEntity friend : mc.world.getPlayers()) {
                            if (!Friends.get().isFriend(friend)) continue;

                            double friendDamage = DamageUtils.crystalDamage(friend, Util.roundVec(bp));
                            if (friendDamage > friendMaxDmg.get()) {
                                validPos = false;
                                break;
                            }
                        }
                    }
                    if (!validPos) continue;
                    if (intersectsWithEntity(bp)) continue;

                    if (targetDamage > bestDamage) {
                        bestDamage = targetDamage;
                        bestPos = bp;
                    }
                }
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }

        this.bestPos = bestPos;
        this.bestDamage = bestDamage;
    }

    private boolean intersectsWithEntity(BlockPosX blockPos) {
        return Util.hasEntity(blockPos.box().stretch(0, oneTwelve.get() ? 1 : 2, 0), entity -> !(entity instanceof EndCrystalEntity));
    }

    private void updateBlock() {
        if (updatedBlock != null && blockTimer.passedMillis(blockUpdate.get())) {
            updatedBlock = null;
        }
    }

    private double getHighestDamage(Vec3d vec3d) {
        if (mc.world == null || mc.player == null) return 0;
        double highestDamage = 0;

        for (PlayerEntity target : mc.world.getPlayers()) {
            if (Friends.get().isFriend(target)) continue;
            if (target == mc.player) continue;
            if (target.isDead() || target.getHealth() == 0) continue;

            double targetDamage = 0;
            boolean skipPredict = false;
            if (predict.get() && target.isAlive()) {
                if (!Util.isSurrounded(target)) {
                    OtherClientPlayerEntity predictedTarget = Util.predictedTarget(target);
                    double x = getPredict(target, offset.get())[0];
                    double z = getPredict(target, offset.get())[1];
                    predictedTarget.setPosition(x, target.getY(), z);

                    if (collision.get()) {
                        if (collidesWithBlocks(predictedTarget)) skipPredict = true;
                    }

                    targetDamage = skipPredict ? targetDamage : DamageUtils.crystalDamage(predictedTarget, vec3d, false, null, ignoreTerrain.get());
                }
            }
            if (!predict.get() || skipPredict)
                targetDamage = DamageUtils.crystalDamage(target, vec3d, false, null, ignoreTerrain.get());

            if (targetDamage < minDmg.get()) continue;

            if (doPlace.get() == DoPlace.BestDMG) {
                if (targetDamage > highestDamage) {
                    highestDamage = targetDamage;
                }
            } else highestDamage += targetDamage;
        }

        return highestDamage;
    }

    public boolean collidesWithBlocks(PlayerEntity player) {
        Box box = player.getBoundingBox();
        List<BlockPosX> list = collisionBlocks(new BlockPosX(box.getCenter()));
        for (BlockPos bp : list) {
            Box pos = new Box(bp);
            if (pos.intersects(box)) return true;
        }

        return false;
    }

    public List<BlockPosX> collisionBlocks(BlockPosX bp) {
        List<BlockPosX> array = new ArrayList<>();

        if (bp.blastRes()) array.add(bp);
        for (Direction direction : Direction.values()) {
            if (direction == Direction.UP || direction == Direction.DOWN) continue;
            BlockPosX pos = bp.offset(direction);
            if (pos.blastRes()) array.add(pos);
        }

        return array;
    }

    private double[] getPredict(PlayerEntity player, double offset) {
        double x = player.getX(), z = player.getZ();

        if (currLocation.isEmpty() || prevLocation.isEmpty()) return new double[]{0, 0};
        Location cLoc = getLocation(player, currLocation);
        Location pLoc = getLocation(player, prevLocation);

        if (cLoc == null || pLoc == null) return new double[]{0, 0};
        double distance = cLoc.vec3d.distanceTo(pLoc.vec3d);

        if (cLoc.x > pLoc.x) {
            x += distance + offset;
        } else if (cLoc.x < pLoc.x) {
            x -= distance + offset;
        }

        if (cLoc.z > pLoc.z) {
            z += distance + offset;
        } else if (cLoc.z < pLoc.z) {
            z -= distance + offset;
        }

        return new double[]{x, z};
    }

    private EndCrystalEntity getCrystal() {
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof EndCrystalEntity)) continue;
            if (mc.player.distanceTo(entity) > breakRange.get()) continue;

            if (doBreak.get() == DoBreak.All) return (EndCrystalEntity) entity;

            double tempDamage = getHighestDamage(Util.roundVec(entity));
            if (tempDamage > minDmg.get()) return (EndCrystalEntity) entity;
        }

        if (bestPos == null) return null;
        return Util.getEntity(bestPos.up());
    }

    private double getBestDamage() {
        return ((double) Math.round(bestDamage * 100) / 100);
    }

    private boolean frequency() {
        switch (frequency.get()) {
            case EachTick -> {
                if (attacks > value.get()) return false;
            }
            case Divide -> {
                if (!divide(value.get()).contains(ticksPassed)) return false;
            }
            case OFF -> {
                return true;
            }
        }

        return true;
    }

    public void getCPS() {
        i++;
        if (i >= second.length) i = 0;

        second[i] = tick;
        tick = 0;

        cps = 0;
        for (int i : second) cps += i;

        lastSpawned--;
        if (lastSpawned >= 0 && cps > 0) cps--;
        if (cps == 0) bestDamage = 0.0;
    }

    public ArrayList<Integer> divide(int frequency) {
        ArrayList<Integer> freqAttacks = new ArrayList<>();
        int size = 0;

        if (20 < frequency) return freqAttacks;
        else if (20 % frequency == 0) {
            for (int i = 0; i < frequency; i++) {
                size += 20 / frequency;
                freqAttacks.add(size);
            }
        } else {
            int zp = frequency - (20 % frequency);
            int pp = 20 / frequency;

            for (int i = 0; i < frequency; i++) {
                if (i >= zp) {
                    size += pp + 1;
                    freqAttacks.add(size);
                } else {
                    size += pp;
                    freqAttacks.add(size);
                }
            }
        }

        return freqAttacks;
    }

    private void addBroken(Entity entity) {
        if (!brokenCrystals.contains(entity.getId())) brokenCrystals.add(entity.getId());
    }

    private void removeId(Entity entity) {
        if (brokenCrystals.contains(entity.getId())) brokenCrystals.remove(entity.getId());
    }

    public Location getLocation(PlayerEntity player, List<Location> locations) {
        return locations.stream().filter(location -> location.player == player).findFirst().orElse(null);
    }

    public void setRender(BlockPosX blockPos) {
        renderPos = blockPos;
    }

    @EventHandler
    public void onRender(Render3DEvent event) {
        if (!render.get()) return;
        if (renderPos == null) return;

        event.renderer.box(renderPos.box(), sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }

    @Override
    public String getInfoString() {
        return getBestDamage() + ", " + cps;
    }

    public enum FastBreak {
        Kill, Instant, Both, OFF
    }

    public enum Frequency {
        EachTick, Divide, OFF
    }

    public enum Priority {
        Place, Break
    }

    public enum DoPlace {
        BestDMG, MostDMG
    }

    public enum DoBreak {
        All, PlacePos, BestDMG
    }

    public enum SurroundBreak {
        Always, OnMine, OFF
    }
}

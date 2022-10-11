package com.example.addon.modules.misc;

import com.example.addon.Addon;

import com.example.addon.Utils.DamageHelper;
import com.example.addon.Utils.bbc.world.BlockHelper;
import com.example.addon.Utils.bbc.world.BlockPosX;
//import com.example.addon.Utils.bbc.world.DamageHelper;//
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.DamageUtils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

import static com.example.addon.Utils.bbc.world.BlockHelper.distance;

public class AutoLogTotems extends Module {
    public AutoLogTotems(){
        super(Addon.misc, "AutoLogTotems", "Top autolog");
    }

    private final SettingGroup sgPing = settings.createGroup("Ping");
    private final SettingGroup sgTotem = settings.createGroup("Totem");
    private final SettingGroup sgNoTotem = settings.createGroup("No Totem");
    private final SettingGroup sgMisc = settings.createGroup("Misc");

    private final Setting<Boolean> logOnHighPing = sgPing.add(new BoolSetting.Builder().name("log-on-high-ping").defaultValue(true).build());
    private final Setting<Integer> pingValue = sgPing.add(new IntSetting.Builder().name("ping-value").defaultValue(1000).range(0, 10000).sliderRange(0, 10000).visible(logOnHighPing::get).build());

    private final Setting<Boolean> logOnTotemPop = sgTotem.add(new BoolSetting.Builder().name("log-on-totem-pop").defaultValue(false).build());
    private final Setting<Integer> totemPopValue = sgTotem.add(new IntSetting.Builder().name("totem-pop-value").defaultValue(6).range(0, 100).sliderRange(0, 20).visible(logOnTotemPop::get).build());

    private final Setting<Boolean> logOnTotemCount = sgTotem.add(new BoolSetting.Builder().name("log-on-totem-count").defaultValue(true).build());
    private final Setting<Integer> totemCountValue = sgTotem.add(new IntSetting.Builder().name("totem-count-value").defaultValue(3).range(0, 40).sliderRange(0, 30).visible(logOnTotemCount::get).build());

    private final Setting<Integer> minHealth = sgNoTotem.add(new IntSetting.Builder().name("min-health").defaultValue(10).range(0, 36).sliderRange(0, 36).build());
    private final Setting<Boolean> logOnDamageSource = sgNoTotem.add(new BoolSetting.Builder().name("log-on-damage-source").defaultValue(true).build());

    private final Setting<Boolean> toggleAfter = sgMisc.add(new BoolSetting.Builder().name("toggle-after").defaultValue(true).build());

    private int popCount;

    @Override
    public void onActivate() {

    }

    @EventHandler
    private void onTick(TickEvent.Post event){
        if (mc == null || mc.world == null || mc.player == null) return;

        disconnectOnHighPing();
        disconnectOnNoTotem();
        disconnectOnTotem();
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!logOnTotemPop.get() || !(event.packet instanceof EntityStatusS2CPacket entityStatusS2CPacket)) return;
        if (entityStatusS2CPacket.getStatus() != 35) return;
        Entity entity = entityStatusS2CPacket.getEntity(mc.world);
        if (entity == null) return;
        if (entity == mc.player) {
            popCount++;
        }
    }

    private void disconnectOnTotem(){
        if (InvUtils.find(Items.TOTEM_OF_UNDYING).count() > 0) return;
        if (logOnTotemCount.get()) {
            int count = InvUtils.find(Items.TOTEM_OF_UNDYING).count();
            if (count <= totemCountValue.get()) disconnect(Text.of("Totem count [" + count + "] -> LOG"));
        }
        if (logOnTotemPop.get()){
            if (popCount >= totemPopValue.get()) disconnect(Text.of("Totem pop count [" + popCount + "] -> LOG"));
        }
    }

    private void disconnectOnNoTotem(){
        if (InvUtils.find(Items.TOTEM_OF_UNDYING).found()) return;
        double totalHealth = PlayerUtils.getTotalHealth();
        if (totalHealth <= minHealth.get()) {
            disconnect(Text.of("Low HP[" + String.format("%.1f", totalHealth) + "] -> LOG"));
            return;
        }

        if (logOnDamageSource.get()){
            if (isThereLethalCrystals(totalHealth)) disconnect(Text.of("Damage Source [End Crystal] -> LOG"));
            if (isThereLethalBeds(totalHealth)) disconnect(Text.of("Damage Source [Bed] -> LOG"));
            if (isThereLethalAnchors(totalHealth)) disconnect(Text.of("Damage Source [Respawn Anchor] -> LOG"));
            if (isThereLethalCreepers(totalHealth)) disconnect(Text.of("Damage Source [Green dick] -> LOG"));
            if (isFallDamage(totalHealth)) disconnect(Text.of("Damage Source [Fall] -> LOG"));
        }
    }

    private void disconnect(Text text){
        if (mc.getNetworkHandler() == null) return;
        mc.getNetworkHandler().getConnection().disconnect(text);

        popCount = 0;
        if (toggleAfter.get()) toggle();
    }

    private void disconnectOnHighPing(){
        if (!logOnHighPing.get()) return;
        if (mc.getNetworkHandler() == null || mc.player == null) return;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());

        int ping = playerListEntry.getLatency();

        if (ping >= pingValue.get()) disconnect(Text.of("High ping[" + ping + "] -> LOG"));
    }

    private boolean isThereLethalCrystals(double totalHealth){
        for (Entity entity : mc.world.getEntities()){
            if (entity instanceof EndCrystalEntity endCrystalEntity && DamageUtils.crystalDamage(mc.player, endCrystalEntity.getPos()) >= totalHealth) return true;
        }
        return false;
    }

    private boolean isThereLethalBeds(double totalHealth){
        if (mc.world.getDimension().bedWorks()) return false;
        for (BlockEntity blockEntity : Utils.blockEntities()){
            if (blockEntity instanceof BedBlockEntity bedBlock && DamageHelper.bedDamage(mc.player, bedBlock.getPos(), true) >= totalHealth) return true;
        }
        return false;
    }

    private boolean isThereLethalAnchors(double totalHealth){
        if (mc.world.getDimension().respawnAnchorWorks()) return false;
        List<BlockPos> sphere = getSphere(mc.player.getBlockPos(), 10, 7);
        int size = sphere.size();
        for (BlockPos blockPos : sphere) {
            BlockPosX posX = new BlockPosX(blockPos);
            if (DamageHelper.anchorDamage(mc.player, posX, true) >= totalHealth) return true;
        }
        return false;
    }

    private boolean isThereLethalCreepers(double totalHealth){
        for (Entity entity : mc.world.getEntities()){
            if (entity instanceof CreeperEntity creeperEntity){
                if (creeperEntity.getFuseSpeed() == 1 && DamageHelper.creeperDamage(mc.player, creeperEntity.getPos(), creeperEntity.shouldRenderOverlay()) >= totalHealth) return true;
            }
        }
        return false;
    }

    private boolean isFallDamage(double totalHealth){
        if (mc.player.fallDistance > 3) {
            double damage = mc.player.fallDistance * 0.5;
            return damage >= totalHealth;
        }
        return false;
    }

    public static List<BlockPos> getSphere(BlockPos centerPos, int radius, int height) {
        ArrayList<BlockPos> blocks = new ArrayList<>();
        for (int i = centerPos.getX() - radius; i < centerPos.getX() + radius; i++) {
            for (int j = centerPos.getY() - height; j < centerPos.getY() + height; j++) {
                for (int k = centerPos.getZ() - radius; k < centerPos.getZ() + radius; k++) {
                    BlockPos pos = new BlockPos(i, j, k);
                    if (distance(centerPos, pos) <= radius && !blocks.contains(pos) && BlockHelper.isOf(pos, Blocks.RESPAWN_ANCHOR)) blocks.add(pos);
                }
            }
        }
        return blocks;
    }
}

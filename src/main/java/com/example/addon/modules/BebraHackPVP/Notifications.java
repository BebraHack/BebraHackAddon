package com.example.addon.modules.BebraHackPVP;

import com.example.addon.Addon;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import com.example.addon.Utils.Task;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.world.CardinalDirection;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

import static com.example.addon.Utils.BlockInfo.*;
import static com.example.addon.Utils.EntityInfo.getBlockPos;

public class Notifications extends Module {
    private final SettingGroup sgArmour = settings.createGroup("Breaks");
    private final SettingGroup sgPlayers = settings.createGroup("Players");
    private final SettingGroup sgSurround = settings.createGroup("Surround");

    private final SettingGroup sgNone = settings.createGroup("");
    private final Setting<Notifications.Mode> notifications = sgNone.add(new EnumSetting.Builder<Notifications.Mode>().name("notifications").defaultValue(Notifications.Mode.Toast).build());

    // Armor
    private final Setting<Boolean> armor = sgArmour.add(new BoolSetting.Builder().name("armor").description("Sends notifications while armor is low.").defaultValue(true).build());
    private final Setting<Integer> percentage = sgArmour.add(new IntSetting.Builder().name("percentage").description("Precentage of armor to trigger notifier.").defaultValue(40).sliderRange(1, 100) .visible(armor::get).build());
    private final Setting<String> message = sgArmour.add(new StringSetting.Builder().name("message").description("Messages for armor notify.").defaultValue("Your {armor}({value}%) is lower than {%}%!").build());

    // Players
    private final Setting<Boolean> totemNotif = sgPlayers.add(new BoolSetting.Builder().name("totem-pops").description("Sends notification for totem pops.").defaultValue(false).build());
    private final Setting<Boolean> deaths = sgPlayers.add(new BoolSetting.Builder().name("deaths").description("Sends notification for deaths.").defaultValue(false).build());

    // Surround
    private final Setting<Boolean> surroundBreak = sgSurround.add(new BoolSetting.Builder().name("surround-break").description("Notifies you while someone breaking your surround.").defaultValue(false).build());

    public Notifications() {
        super(Addon.combat, "Notifications", "Sends messages in hud about different events.");
    }

    private BlockPos prevBreakPos;
    private final Task bootsTask = new Task();
    private final Task leggingsTask = new Task();
    private final Task chestplateTask = new Task();
    private final Task helmetTask = new Task();

    private final Object2IntMap<UUID> totemPopMap = new Object2IntOpenHashMap<>();
    private final Object2IntMap<UUID> chatIdMap = new Object2IntOpenHashMap<>();

    @Override
    public void onActivate() {
        totemPopMap.clear();
        chatIdMap.clear();

        bootsTask.reset();
        leggingsTask.reset();
        chestplateTask.reset();
        helmetTask.reset();
    }

    @EventHandler
    private void onGameJoin(GameJoinedEvent event) {
        totemPopMap.clear();
        chatIdMap.clear();
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!(event.packet instanceof EntityStatusS2CPacket p)) return;
        if (p.getStatus() != 35) return;
        Entity entity = p.getEntity(mc.world);
        if (!(entity instanceof PlayerEntity)) return;
        if ((entity.equals(mc.player))) return;

        synchronized (totemPopMap) {
            int pops = totemPopMap.getOrDefault(entity.getUuid(), 0);
            totemPopMap.put(entity.getUuid(), ++pops);
            if (totemNotif.get()) send(entity.getEntityName() + " popped " + pops + " time`s!", notifications);
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        synchronized (totemPopMap) {
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (!totemPopMap.containsKey(player.getUuid())) continue;

                if (player.deathTime > 0 || player.getHealth() <= 0) {
                    int pops = totemPopMap.getOrDefault(player.getUuid(), 0);
                    if (deaths.get()) send(player.getEntityName() + " just died after " + pops +" pops!", notifications);
                    totemPopMap.removeInt(player.getUuid());
                    chatIdMap.removeInt(player.getUuid());
                }
            }
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (!armor.get()) return;
        ItemStack boots = mc.player.getInventory().getArmorStack(0);
        ItemStack leggings = mc.player.getInventory().getArmorStack(1);
        ItemStack chestplate = mc.player.getInventory().getArmorStack(2);
        ItemStack helmet = mc.player.getInventory().getArmorStack(3);

        if (boots.isEmpty() && leggings.isEmpty() && chestplate.isEmpty() && helmet.isEmpty()) return;

        if (getPercentage(boots) < percentage.get()) {
            bootsTask.run(() -> notifyArmor("Boots", getPercentage(boots)));
        } else bootsTask.reset();

        if (getPercentage(leggings) < percentage.get()) {
            leggingsTask.run(() -> notifyArmor("Leggings", getPercentage(leggings)));
        } else leggingsTask.reset();

        if (getPercentage(chestplate) < percentage.get()) {
            chestplateTask.run(() -> notifyArmor("Chestplate", getPercentage(chestplate)));
        } else chestplateTask.reset();

        if (getPercentage(helmet) < percentage.get()) {
            helmetTask.run(() -> notifyArmor("Helmet", getPercentage(helmet)));
        } else helmetTask.reset();
    }

    @EventHandler
    public void onBreakPacket(PacketEvent.Receive event) {
        if (surroundBreak.get()) {
            if (event.packet instanceof BlockBreakingProgressS2CPacket bbpp) {
                BlockPos bbp = bbpp.getPos();

                if (bbp.equals(prevBreakPos) && bbpp.getProgress() > 0) return;

                PlayerEntity breakingPlayer = (PlayerEntity) mc.world.getEntityById(bbpp.getEntityId());
                BlockPos playerBlockPos = getBlockPos(mc.player);
                boolean validBlock = isCombatBlock(bbp);

                assert breakingPlayer != null;
                if (breakingPlayer.equals(mc.player)) return;

                for (CardinalDirection direction : CardinalDirection.values()) {
                    if (validBlock && bbp.equals(playerBlockPos.offset(direction.toDirection()))) notifySurroundBreak(breakingPlayer);
                }

                prevBreakPos = bbp;
            }
        }
    }

    public static void send(String msg, Setting<Notifications.Mode> notify) {
        switch (notify.get()) {
            case Chat: ChatUtils.info(msg);
            case Toast: addToast(msg);
        }
    }

    private static void addToast(String msg) {

    }

    private void notifyArmor(String armor, int percentage) {
        String msg = message.get();

        msg = msg.replace("{armor}", armor);
        msg = msg.replace("{value}", String.valueOf(percentage));
        msg = msg.replace("{%}", String.valueOf(this.percentage.get()));

        if (percentage != 0) send(msg, notifications);
    }

    private int getPercentage(ItemStack itemStack) {
        return Math.round(((itemStack.getMaxDamage() - itemStack.getDamage()) * 100f) / (float) itemStack.getMaxDamage());
    }

    private void notifySurroundBreak(PlayerEntity player) {
        send("Your surround is being broken by " + player.getEntityName(), notifications);
    }

    public enum Mode {
        Toast, Chat
    }
}

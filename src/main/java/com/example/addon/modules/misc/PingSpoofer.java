package com.example.addon.modules.misc;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.s2c.play.KeepAliveS2CPacket;

@Environment(EnvType.CLIENT)
public class PingSpoofer extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> strict = sgGeneral.add(new BoolSetting.Builder()
        .name("strict")
        .description("Responds as fast as possible to keep alive packets send by the server.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> ping = sgGeneral.add(new IntSetting.Builder()
        .name("ping")
        .description("The ping to set.")
        .defaultValue(250)
        .min(0)
        .sliderMin(100)
        .sliderMax(1000)
        .noSlider()
        .visible(() -> !strict.get())
        .build()
    );

    private final Setting<Integer> deviation = sgGeneral.add(new IntSetting.Builder()
        .name("deviation")
        .description("Randomize the ping by this amount.")
        .defaultValue(0)
        .min(0)
        .sliderMin(0)
        .sliderMax(50)
        .noSlider()
        .visible(() -> !strict.get())
        .build()
    );

    public PingSpoofer() {
        super(Addon.misc, "ping-spoof", "Modify your ping.");
    }

    private long id;
    private long timer;
    private int nextPing;

    @Override
    public void onActivate() {
        if (isDisallowed()) {
            this.toggle();
            ChatUtils.warning("Ping Spoofer is not allowed on this server.");
        }

        id = -1;
        timer = System.currentTimeMillis();
        nextPing = getPing();
    }

    private boolean isDisallowed() {
        ServerInfo info = mc.getCurrentServerEntry();
        if (info != null) {
            String ip = info.address;
            return info.online && (ip.contains("hypixel") || ip.contains("cubecraft") || ip.contains("mineplex") || ip.contains("bedwarspractice") || ip.contains("mineverse"));
        }
        return false;
    }


    private int getPing() {
        if (deviation.get() == 0) return ping.get();
        return (int) (Math.random() * (deviation.get() * 2) - deviation.get() + ping.get());
    }

    @EventHandler
    private void onServerJoin(GameJoinedEvent event) {
        if (isDisallowed()) {
            this.toggle();
            ChatUtils.warning("Ping Spoofer is not allowed on this server.");
        }
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (System.currentTimeMillis() - timer >= nextPing && id >= 0 && !strict.get()) {
            mc.getNetworkHandler().sendPacket(new KeepAliveC2SPacket(id));
            nextPing = getPing();
        }
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (event.packet instanceof KeepAliveC2SPacket packet && id != packet.getId()) {
            if (strict.get()) {
                event.cancel();
            } else if (ping.get() != 0) {
                id = packet.getId();
                timer = System.currentTimeMillis();

                event.cancel();
            }
        }
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (strict.get() && event.packet instanceof KeepAliveS2CPacket packet) {
            id = packet.getId();
            mc.getNetworkHandler().sendPacket(new KeepAliveC2SPacket(id));
        }
    }

    @Override
    public String getInfoString() {
        if (mc != null && mc.player != null && mc.getNetworkHandler() != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            return entry != null ? entry.getLatency() + "ms" : null;
        }

        return null;
    }
}

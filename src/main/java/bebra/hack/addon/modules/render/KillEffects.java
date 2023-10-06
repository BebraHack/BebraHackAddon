package bebra.hack.addon.modules.render;

import bebra.hack.addon.Addon;
import bebra.hack.addon.Utils.BWorldUtils;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;

import static bebra.hack.addon.Utils.BEntityUtils.deadEntity;
import static bebra.hack.addon.Utils.BEntityUtils.isDeathPacket;

public class KillEffects extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    // General
    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .description("How far away the lightning is allowed to spawn from you.")
        .defaultValue(16)
        .sliderRange(0,256)
        .build()
    );

    private final Setting<Boolean> avoidSelf = sgGeneral.add(new BoolSetting.Builder()
        .name("avoid-self")
        .description("Will not render your own deaths.")
        .defaultValue(true)
        .build()
    );


    public KillEffects() {
        super(Addon.render, "kill-effects", "Spawns a lightning where a player dies.");
    }


    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (isDeathPacket(event)) {
            Entity player = deadEntity;
            if (player == mc.player && avoidSelf.get()) return;
            if (mc.player.distanceTo(player) > range.get()) return;

            double playerX = player.getX();
            double playerY = player.getY();
            double playerZ = player.getZ();

            BWorldUtils.spawnLightning(playerX, playerY, playerZ);
        }
    }

}

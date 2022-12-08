package com.example.addon.modules.misc;


import com.example.addon.Addon;
import com.google.common.collect.Lists;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

@Environment(EnvType.CLIENT)
public class SpamInChat extends Module {
    public enum Text {
        new_Rap_by_Minerica
    }

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Enum<Text>> text = sgGeneral.add(new EnumSetting.Builder<Enum<Text>>()
        .name("text")
        .defaultValue(Text.new_Rap_by_Minerica)
        .onChanged(e -> messageI = 0)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(20)
        .min(0)
        .sliderMax(200)
        .build()
    );

    private int messageI, timer;

    public SpamInChat() {
        super(Addon.misc, "SpamInChat", "Better than spam.");
    }

    @Override
    public void onActivate() {
        timer = delay.get();
        messageI = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (timer <= 0) {
            String line = "";
            if (text.get().equals(Text.new_Rap_by_Minerica)) line = new_Rap_by_Minerica.get(messageI);
            mc.player.sendChatMessage (line,null);
            timer = delay.get();
            if ((text.get().equals(Text.new_Rap_by_Minerica) && messageI++ == new_Rap_by_Minerica.size() - 1)) {
                messageI = 0;
            }
        } else {
            timer--;
        }
    }
    /* Я ПОМНЮ ПЕНИС БОЛЬШОЙ */
    private static final List<String> new_Rap_by_Minerica = Lists.newArrayList(
        "ей ей ей йоу реп новый",
        "насрал в гуся",
        "насрал в рот пеликану",
        "йоу йоу Я ебал птиц на птицеферме",
        "это очень хорошо",
        "йоу йоу ееее."

    );

}

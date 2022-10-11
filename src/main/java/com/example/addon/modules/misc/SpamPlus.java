package com.example.addon.modules.misc;


import com.example.addon.Addon;
import com.google.common.collect.Lists;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class SpamPlus extends Module {
    public enum Text {
        BeeMovie
    }

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Enum<Text>> text = sgGeneral.add(new EnumSetting.Builder<Enum<Text>>()
        .name("text")
        .defaultValue(Text.BeeMovie)
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

    public SpamPlus() {
        super(Addon.misc, "spam-plus", "Better than spam.");
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
            if (text.get().equals(Text.BeeMovie)) line = BEE_MOVIE.get(messageI);
            mc.player.sendChatMessage (line,null);
            timer = delay.get();
            if ((text.get().equals(Text.BeeMovie) && messageI++ == BEE_MOVIE.size() - 1)) {
                messageI = 0;
            }
        } else {
            timer--;
        }
    }
    /* Я ПОМНЮ ПЕНИС БОЛЬШОЙ */
    private static final List<String> BEE_MOVIE = Lists.newArrayList(
        "ей ей ей йоу реп новый",
        "насрал в гуся",
        "насрал в рот пеликану",
        "йоу йоу Я ебал птиц на птицеферме",
        "это очень хорошо",
        "йоу йоу ееее."

    );

}

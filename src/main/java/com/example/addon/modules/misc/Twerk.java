package com.example.addon.modules.misc;

import com.example.addon.Utils.TimerUtilsbanana;
import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

//Я помню. Я помню пенис большой//
// Я помню пенис большой //
// Я помню пенис большой //
// Я помню у-о-о. Я знаю пенис большой //
// Я знаю пенис большой //
// Я помню пенис большой //
// Я помню пенис большо-у-о-у-о-у-о//

public class Twerk extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    // General
    private final Setting<Double> twerkDelay = (Setting<Double>) sgGeneral.add(new DoubleSetting.Builder()
        .name("Twerk Delay")
        .description("In Millis.")
        .defaultValue(4)
        .min(1)
        .sliderRange(2,100)
        .build()
    );


    public Twerk() {
        super(Addon.misc, "twerk", "Twerk like the true queen Miley Cyrus.");
    }


    private boolean upp = false;
    private final com.example.addon.Utils.TimerUtilsbanana onTwerk = new TimerUtilsbanana();


    @EventHandler
    private void onTick(TickEvent.Pre event) {
        mc.options.sneakKey.setPressed(upp);

        if (onTwerk.passedMillis(twerkDelay.get().longValue()) && !upp) {
            onTwerk.reset();
            upp = true;
        }

        if (onTwerk.passedMillis(twerkDelay.get().longValue()) && upp) {
            onTwerk.reset();
            upp = false;
        }

    }

    @Override
    public void onDeactivate() {
        upp = false;
        mc.options.sneakKey.setPressed(false);
        onTwerk.reset();
    }
}

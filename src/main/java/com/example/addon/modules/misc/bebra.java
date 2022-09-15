package com.example.addon.modules.misc;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.text.Text;

public class bebra extends Module {
    public bebra() {
        super(Addon.misc, "BEBRA", "Authors: Fucking slaves#7603 , MrFiNka#8113");
    }
    @Override
    public void onActivate (){
        ChatUtils.sendMsg(Text.of("Authors: Fucking slaves#7603 , MrFiNka#8113 , Cahekska#9358"));
        toggle();
    }
}

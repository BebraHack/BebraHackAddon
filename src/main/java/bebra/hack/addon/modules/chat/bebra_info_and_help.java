package bebra.hack.addon.modules.chat;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.text.Text;

public class bebra_info_and_help extends Module {
    public bebra_info_and_help() {
        super(Addon.chat, "BEBRA", "Authors: Fucking slaves#7603 , MrFiNka#8113");
    }
    @Override
    public void onActivate (){
        ChatUtils.sendMsg(Text.of("Authors: Fucking slaves#7603 , MrFiNka#8113 , Cahekska#9358"));
        ChatUtils.sendMsg(Text.of("BebraHack is a free addon for the meteor client for version 1.19.4"));
        ChatUtils.sendMsg(Text.of("Eu: if you have a question about the addon, then go to the discord server of the addon and call me in private messages. My nickname - jabronyyy https://discord.gg/8zpzhvq3U7 / Ru: по вопросам пишите мне в дс, ник - jabronyyy, найти можно в дс бебрахака (ссылка выше)"));
        try {
            String url = "https://github.com/BebraHack/BebraHackAddon";
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
        toggle();
    }
}

package bebra.hack.addon.modules.chat;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.MinecraftVersion;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

public class BebraInfoAndHelp extends Module {
    private static final List<String> names = Arrays.asList("Fucking slaves#7603", "MrFiNka#8113", "Cahekska#9358");
    private static final String authorName = "jabronyyy";

    public BebraInfoAndHelp() {
        super(Addon.chat, "BEBRA", "Authors: " + String.join(", ", names));
    }

    @Override
    public void onActivate() {
        MutableText authors = Text.empty();
        boolean ru = mc.getLanguageManager().getLanguage().startsWith("ru");
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            Style authorStyle = Style.EMPTY.withColor(Formatting.GREEN).withUnderline(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(ru ? "Нажмите, чтобы скопировать в буфер обмена" : "Click to copy to clipboard")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, name));
            authors.append(Text.literal(name).setStyle(authorStyle));
            if (i != names.size() - 1) authors.append(Text.literal(", ").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
        }

        info(Text.literal((ru ? "BebraHack это бесплатный аддон для Meteor Client %s и Minecraft %s" : "BebraHack is a free addon for the Meteor Client %s and Minecraft %s").formatted(MeteorClient.VERSION, MinecraftVersion.CURRENT.getName())).setStyle(Style.EMPTY.withColor(Formatting.AQUA))
            .append("\n")
            .append(Text.literal(ru ? "Авторы: " : "Authors: ").setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
            .append(authors)
            .append("\n")
            .append(Text.literal(ru ? "Если у вас есть какие-либо вопросы по аддону, напишите мне в Discord. Мой ник " : "If you have any questions about the addon, please DM me on Discord. My nickname is ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))
            .append(Text.literal(authorName).setStyle(Style.EMPTY.withColor(Formatting.GREEN).withUnderline(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(ru ? "Нажмите, чтобы скопировать в буфер обмена" : "Click to copy to clipboard")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, authorName))
            ))
        );

        toggle();
    }
}

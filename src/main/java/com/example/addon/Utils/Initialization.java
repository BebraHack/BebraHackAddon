package com.example.addon.Utils;

import com.example.addon.Utils.DamageHelper;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//a
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import static meteordevelopment.meteorclient.MeteorClient.mc;
//a
public class Initialization {
    private static final List<Module> moduleList = new ArrayList<>();

    // executor.stop() breaks everything
    public static Thread executor = Thread.currentThread();

    public static void init(){
        modules();
        utils();
        prefix();
        addCPvP();
        add2g2c();
    }

    private static void prefix(){
        ChatUtils.registerCustomPrefix("bbcdevelopment.addon.bbcaddon", Initialization::getPrefix);
    }

    private static void utils(){
        DamageHelper.init();
    }

    private static void modules(){
        Set<Class<? extends Module>> reflections = new Reflections("bbcdevelopment.addon.bbcaddon.modules").getSubTypesOf(Module.class);

        reflections.forEach(aClass -> {
            try {
                moduleList.add(aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                Initialization.executor.stop();
                throw new RuntimeException(e);
            }
        });

        moduleList.forEach(module -> Modules.get().add(module));
    }

    public static Text getPrefix() {
        MutableText logo = Text.literal("BBC");
        MutableText prefix = Text.literal("");
        logo.setStyle(logo.getStyle().withFormatting(Formatting.DARK_GRAY));
        prefix.setStyle(prefix.getStyle().withFormatting(Formatting.DARK_GRAY));
        prefix.append("[");
        prefix.append(logo);
        prefix.append("] ");
        return prefix;
    }

    public static void addCPvP(){
        ServerList servers = new ServerList(mc);
        servers.loadFile();

        boolean found = false;
        for (int i = 0; i < servers.size(); i++) {
            ServerInfo server = servers.get(i);

            if (server.address.contains("eu.cpvp.me")) {
                found = true;
                break;
            }
        }

        if (!found) {
            servers.add(new ServerInfo("cpvp.me", "eu.cpvp.me", false), false);
            servers.saveFile();
        }
    }
    public static void add2g2c(){
        ServerList servers = new ServerList(mc);
        servers.loadFile();

        boolean found = false;
        for (int i = 0; i < servers.size(); i++) {
            ServerInfo server = servers.get(i);

            if (server.address.contains("2g2c.org")) {
                found = true;
                break;
            }
        }

        if (!found) {
            servers.add(new ServerInfo("2g2c", "2g2c.org", false), false);
            servers.saveFile();
        }
    }
}

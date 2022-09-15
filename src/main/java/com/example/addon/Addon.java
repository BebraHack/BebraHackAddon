package com.example.addon;
import com.example.addon.modules.BebraHackPVP.*;
import com.example.addon.modules.hud.Killfeed;
import com.example.addon.modules.hud.LogoHud;
import com.example.addon.modules.misc.*;
import com.example.addon.modules.BebraHackRender.*;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {
    public static String VERSION = "BebraHack V3";
    public static String INVITE_LINK = "https://discord.gg/FP8jcqTZyG";

    public static final Logger LOG = LogUtils.getLogger();

    public static final Category combat = new Category("BebraHackPVP");
    public static final Category misc = new Category("BebraHackPVE");
    public static final Category render = new Category("BebraHackRender");
    public static final HudGroup HUD_GROUP = new HudGroup("BebraHac+");


    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        // HUD
//		Hud.get().register(ItemCounter.INFO);
        Hud.get().register(Killfeed.INFO);
        Hud.get().register(LogoHud.INFO);
//        Hud.get().register(WelcomeHud.INFO);
//        Hud.get().register(TextPresets.INFO);

        // BebraHackPVP
        Modules.get().add(new AntiCrystal());
        Modules.get().add(new InstaMinePlus());
        Modules.get().add(new Burrow());
        Modules.get().add(new AntiSurroundBlocks());
        Modules.get().add(new SurroundBEBRA());
        Modules.get().add(new BedAuraBEBRA());

        // Misc
        Modules.get().add(new Twerk());
        Modules.get().add(new ShulkerDupe());
        Modules.get().add(new AutoBuild());
        Modules.get().add(new NoDesync());
        Modules.get().add(new SpamPlus());
        Modules.get().add(new Lavacast());
        Modules.get().add(new AutoPortalMine());
        Modules.get().add(new bebra());
        Modules.get().add(new AutoCraft());
        Modules.get().add(new GhostBlockFixer());
        Modules.get().add(new VillagerRoller());
        Modules.get().add(new PingSpoofer());
        Modules.get().add(new CoordLogger());
        Modules.get().add(new AutoBuildSwastika());
        Modules.get().add(new AutoSex());
        Modules.get().add(new OldAnvil());
        Modules.get().add(new TreeAura());
        Modules.get().add(new AutoTNT());
        Modules.get().add(new AutoLogin());
        Modules.get().add(new SlotClick());
        Modules.get().add(new EFly());
        Modules.get().add(new MEGA_DUPE());

        // Render
        Modules.get().add(new Confetti());
        Modules.get().add(new HoleESPPlus());
        Modules.get().add(new TimeAnimator());
        Modules.get().add(new NewChunks());
        Modules.get().add(new AntiVanish());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(combat);
        Modules.registerCategory(misc);
        Modules.registerCategory(render);
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }
}

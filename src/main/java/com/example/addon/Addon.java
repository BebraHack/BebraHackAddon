package com.example.addon;
import meteordevelopment.meteorclient.systems.modules.Categories;
import com.example.addon.modules.BebraHackPVP.*;
import com.example.addon.modules.hud.CustomImage;
import com.example.addon.modules.hud.Killfeed;
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
    public static String VERSION = "BebraHack V5";
    public static String INVITE_LINK = "https://discord.gg/FP8jcqTZyG";

    public static final Logger LOG = LogUtils.getLogger();

    public static final Category combat = new Category("BebraHackPVP");
    public static final Category misc = new Category("BebraHackPVE");
    public static final Category render = new Category("BebraHackRender");
    public static final HudGroup HUD_GROUP = new HudGroup("BebraHackHUD");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon BebraHack V5");

        // HUD
//		Hud.get().register(ItemCounter.INFO);
        Hud.get().register(Killfeed.INFO);
        Hud.get().register(CustomImage.INFO);
//        Hud.get().register(WelcomeHud.INFO);
//        Hud.get().register(TextPresets.INFO);

        // BebraHackPVP
        Modules.get().add(new AntiCrystal());
        Modules.get().add(new Burrow());
        Modules.get().add(new AntiSurroundBlocks());
        Modules.get().add(new FanSurroundBEBRA());
        Modules.get().add(new CevBreaker());
        Modules.get().add(new Notifications());
        Modules.get().add(new tntauraBEBRAplus());
        Modules.get().add(new PacketHoleFill());
        Modules.get().add(new sphere());
        Modules.get().add(new AutoEz());
        Modules.get().add(new SurroundBEBRA());
        Modules.get().add(new SelfAnvilBEBRA());
        Modules.get().add(new AnchorAuraBEBRA());
        Modules.get().add(new AntiBurrow());


        // BebraHackPVE
        Modules.get().add(new Twerk());
        Modules.get().add(new AutoBuild());
        Modules.get().add(new NoDesync());
        Modules.get().add(new SpamInChat());
        Modules.get().add(new Lavacast());
        Modules.get().add(new bebra_info_and_help());
        Modules.get().add(new AutoCraft());
        Modules.get().add(new VillagerRoller());
        Modules.get().add(new PingSpoofer());
        Modules.get().add(new CoordLogger());
        Modules.get().add(new AutoSex());
        Modules.get().add(new TreeAura());
        Modules.get().add(new AutoTNT());
        Modules.get().add(new AutoLogin());
        Modules.get().add(new EFly());
        Modules.get().add(new MEGA_DUPEprank());
        //Modules.get().add(new ChatEncryption());
        Modules.get().add(new AutoLogTotems());
        Modules.get().add(new SilentItem());
        Modules.get().add(new Spinner());

        // BebraHack Render
        //Modules.get().add(new Confetti());
        //Modules.get().add(new HoleESPPlus());
        Modules.get().add(new TimeAnimator());
        //Modules.get().add(new NewChunks());
        Modules.get().add(new AntiVanish());
        ///Modules.get().add(new Prefix());
        Modules.get().add(new KillEffects());
        //Modules.get().add(new BurrowESP());
        //Modules.get().add(new BebraESP());

        Modules.get().get(bebra_info_and_help.class).toggle();
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

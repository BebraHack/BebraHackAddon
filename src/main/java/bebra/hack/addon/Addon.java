package bebra.hack.addon;

import bebra.hack.addon.hud.CustomImage;
import bebra.hack.addon.modules.chat.*;
import bebra.hack.addon.modules.pve.*;
import bebra.hack.addon.modules.pvp.*;
import bebra.hack.addon.modules.render.*;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.Surround;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {

    public static String VERSION = "BebraHackV6";
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category combat = new Category("BebraHackPVP");

    public static final Category misc = new Category("BebraHackPVE");
    public static final Category render = new Category("BebraHackRender");
    public static final Category chat = new Category("BebraHackChat");
    public static final HudGroup HUD_GROUP = new HudGroup("BebraHackHUD");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon BebraHackV6");

        // PVP
        Modules.get().add(new sphere());
        Modules.get().add(new Blocker());
        Modules.get().add(new HitboxDesync());
        Modules.get().add(new tntauraBEBRAplus());
        Modules.get().add(new DownBlocs());
        Modules.get().add(new AutoTrapPlus());
        Modules.get().add(new FanSurroundBEBRA());
        Modules.get().add(new AutoExit());
        Modules.get().add(new AntiCrystal());
        Modules.get().add(new AntiSurroundBlocks());
        Modules.get().add(new Burrow());
        Modules.get().add(new PIRAMIDKA());
        Modules.get().add(new ButtonPlace());
        Modules.get().add(new PacketHoleFill());
        Modules.get().add(new SelfAnvilBEBRA());
        Modules.get().add(new SurroundBEBRA());
        Modules.get().add(new DeleteItems());

        // PVE
        Modules.get().add(new AutoDrop());
        Modules.get().add(new Platforms());
        Modules.get().add(new AutoBuild());
        //Modules.get().add(new AutoCraft());
        Modules.get().add(new AutoSand());
        Modules.get().add(new TreeAura());
        Modules.get().add(new NoDesync());
        Modules.get().add(new EFly());
        Modules.get().add(new SilentItem());
        Modules.get().add(new VillagerRoller());
        Modules.get().add(new DupeWithFrame());
        Modules.get().add(new AutoSex());
        Modules.get().add(new AutoTNT());
        Modules.get().add(new Twerk());
        Modules.get().add(new Spinner());
        Modules.get().add(new Lavacast());
        Modules.get().add(new PingSpoofer());
        Modules.get().add(new Chests_farmer());
        Modules.get().add(new NewsPaperHelperRu());
        Modules.get().add(new NewsPaperHelperEu());

        //RENDER
        Modules.get().add(new BebraESP());
        Modules.get().add(new NewChunks());
        Modules.get().add(new TimeAnimator());
        Modules.get().add(new Bubbles());
        Modules.get().add(new Bubbles2());
        Modules.get().add(new KillEffects());
        Modules.get().add(new HoleESPPlus());
        //Modules.get().add(new Linia());

        //CHAT
        Modules.get().add(new bebra_info_and_help());
        Modules.get().add(new ChatEncryption());
        Modules.get().add(new Prefix());
        Modules.get().add(new SpamInChat());
        Modules.get().add(new AutoLogin());
        //Modules.get().add(new AutoEz());


        // HUD
        Hud.get().register(CustomImage.INFO);
    }

    @Override
    public void onRegisterCategories() {

        Modules.registerCategory(combat);
        Modules.registerCategory(render);
        Modules.registerCategory(misc);
        Modules.registerCategory(chat);
    }

    @Override
    public String getPackage() {
        return "bebra.hack.addon";
    }
}

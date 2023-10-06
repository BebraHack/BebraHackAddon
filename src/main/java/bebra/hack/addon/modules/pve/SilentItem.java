package bebra.hack.addon.modules.pve;

import bebra.hack.addon.Addon;
import bebra.hack.addon.Utils.ArmorUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

//SilentItem by ranele (copper hack addon) https://discord.gg/tqRPu6Pudg     (upgrade by jabronyyy)

public class SilentItem extends Module {
    public SilentItem() {
        super(Addon.misc, "silent-item", " ");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Item> itemUse = sgGeneral.add(new EnumSetting.Builder<Item>().name("item").defaultValue(Item.Rocket).build());
    private final Setting<Boolean> notify = sgGeneral.add(new BoolSetting.Builder().name("notify").defaultValue(true).build());

    private int mainSlot;
    private boolean invMove;

    @Override
    public void onActivate(){
        invMove = false;
        mainSlot = mc.player.getInventory().selectedSlot;
    }

    @Override
    public void onDeactivate(){
        invMove = false;
        mainSlot = -1;
    }

    @EventHandler
    private void onTickEvent(TickEvent.Pre event) {
        FindItemResult fP = InvUtils.find(Items.ENDER_PEARL);
        if (!fP.found() && itemUse.get() == Item.Pearl) toggle();
        FindItemResult fR = InvUtils.find(Items.FIREWORK_ROCKET);
        if (!fR.found() && itemUse.get() == Item.Rocket) toggle();
        if (itemUse.get() == Item.Pearl) {
            FindItemResult pearl = InvUtils.find(Items.ENDER_PEARL);
            int pearlSlot = pearl.slot();
            if (!pearl.found() && notify.get()) {
                info("No pearl found!");
                return;
            }
            InvUtils.move().from(pearlSlot).to(mainSlot);
            if (mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                invMove = true;
            }
            if (invMove) {
                InvUtils.move().from(mainSlot).to(pearlSlot);
            }
            toggle();
        }

        //

        if (itemUse.get() == Item.Rocket) {
            if (!ArmorUtils.isFallFlying()) toggle();
            FindItemResult rocket = InvUtils.find(Items.FIREWORK_ROCKET);
            int rocketSlot = rocket.slot();
            if (!rocket.found() && notify.get()) {
                info("No rocket found!");
                toggle();
                return;
            }
            if (!rocket.found()) toggle();
            InvUtils.move().from(rocketSlot).to(mainSlot);
            if (mc.player.getMainHandStack().getItem() == Items.FIREWORK_ROCKET) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                invMove = true;
            }
            if (invMove) {
                InvUtils.move().from(mainSlot).to(rocketSlot);
            }
            toggle();
        }

        //

        if (!fP.found() && itemUse.get() == Item.Egg) toggle();
        if (itemUse.get() == Item.Egg) {
            FindItemResult Egg = InvUtils.find(Items.EGG);
            int egglSlot = Egg.slot();
            if (!Egg.found() && notify.get()) {
                info("No Egg found!");
                return;
            }
            InvUtils.move().from(egglSlot).to(mainSlot);
            if (mc.player.getMainHandStack().getItem() == Items.EGG) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                invMove = true;
            }
            if (invMove) {
                InvUtils.move().from(mainSlot).to(egglSlot);
            }
            toggle();
        }

        //

        if (!fP.found() && itemUse.get() == Item.Snow) toggle();
        if (itemUse.get() == Item.Snow) {
            FindItemResult Egg = InvUtils.find(Items.SNOWBALL);
            int SnowlSlot = Egg.slot();
            if (!Egg.found() && notify.get()) {
                info("No Snow found!");
                return;
            }
            InvUtils.move().from(SnowlSlot).to(mainSlot);
            if (mc.player.getMainHandStack().getItem() == Items.SNOWBALL) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                invMove = true;
            }
            if (invMove) {
                InvUtils.move().from(mainSlot).to(SnowlSlot);
            }
            toggle();
        }

        //

        if (!fP.found() && itemUse.get() == Item.SplashPotion) toggle();
        if (itemUse.get() == Item.SplashPotion) {
            FindItemResult SplashPotion = InvUtils.find(Items.SPLASH_POTION);
            int SplashPotionSlot = SplashPotion.slot();
            if (!SplashPotion.found() && notify.get()) {
                info("No Splash-Potion found!");
                return;
            }
            InvUtils.move().from(SplashPotionSlot).to(mainSlot);
            if (mc.player.getMainHandStack().getItem() == Items.SPLASH_POTION) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                invMove = true;
            }
            if (invMove) {
                InvUtils.move().from(mainSlot).to(SplashPotionSlot);
            }
            toggle();
        }

        //

        /*if (!fP.found() && itemUse.get() == Item.Lava) toggle();
        if (itemUse.get() == Item.Lava) {
            FindItemResult Lava = InvUtils.find(Items.LAVA_BUCKET);
            int LavaSlot = Lava.slot();
            if (!Lava.found() && notify.get()) {
                info("No lava-bucket found!");
                return;
            }
            InvUtils.move().from(LavaSlot).to(mainSlot);
            if (mc.player.getMainHandStack().getItem() == Items.LAVA_BUCKET) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                invMove = true;
            }
            if (invMove) {
                InvUtils.move().from(mainSlot).to(LavaSlot);
            }
            toggle();
        }*/// перенесено в DeleteItems
        }
    public enum Item {
        Pearl,
        Egg,
        Snow,
        Rocket,
        //Lava,
        SplashPotion;
    }
}

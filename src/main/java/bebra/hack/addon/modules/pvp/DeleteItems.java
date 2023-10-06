package bebra.hack.addon.modules.pvp;

import bebra.hack.addon.Addon;
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

//by jabronyyy
public class DeleteItems extends Module {
    public DeleteItems() {
        super(Addon.combat, "Delete-Items", "In order not to give things to the enemy, turn on this function before death (you need to carry a bucket of lava with you)");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Item> itemUse = sgGeneral.add(new EnumSetting.Builder<Item>().name("item").defaultValue(Item.Lava).build());
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
        FindItemResult fP = InvUtils.find(Items.LAVA_BUCKET);
        if (!fP.found() && itemUse.get() == Item.Lava) toggle();
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
        }
    }
    public enum Item {
        Lava,
    }
}

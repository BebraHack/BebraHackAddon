package bebra.hack.addon.modules.pvp;
//created by jabronyyy
import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class ButtonPlace extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("target-range")
        .description("The maximum distance to target players.")
        .defaultValue(4)
        .range(0, 5)
        .sliderMax(5)
        .build()
    );

    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>()
        .name("target-priority")
        .description("How to filter targets within range.")
        .defaultValue(SortPriority.LowestDistance)
        .build()
    );
    private final Setting<Boolean> AntiBurrow = sgGeneral.add(new BoolSetting.Builder()
        .name("AntiBurrow")
        .description("Puts buttons in the burrow.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> in_Surround = sgGeneral.add(new BoolSetting.Builder()
        .name("in Surround")
        .description("Puts buttons in the surround.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Rotates towards the buttons when placing.")
        .defaultValue(true)
        .build()
    );

    private PlayerEntity target = null;

    public ButtonPlace() {
        super(Addon.combat, "ButtonPlace", "Automatically places button in surround and burrow to your opponent.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (TargetUtils.isBadTarget(target, range.get())) {
            target = TargetUtils.getPlayerTarget(range.get(), priority.get());
            if (TargetUtils.isBadTarget(target, range.get())) return;
        }

        //BlockUtils.place(target.getBlockPos(), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        if (AntiBurrow.get()) {
            BlockUtils.place(target.getBlockPos().add(0, 0, 0), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        }

        if (in_Surround.get()) {
            BlockUtils.place(target.getBlockPos().add(1, 0, 0), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        }
        if (in_Surround.get()) {
            BlockUtils.place(target.getBlockPos().add(-1, 0, 0), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        }
        if (in_Surround.get()) {
            BlockUtils.place(target.getBlockPos().add(0, 0, 1), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        }
        if (in_Surround.get()) {
            BlockUtils.place(target.getBlockPos().add(0, 0, -1), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        }
    }
}

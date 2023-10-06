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
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;


public class AutoSand extends Module {
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
    private final Setting<Boolean> PlaceSand = sgGeneral.add(new BoolSetting.Builder()
        .name("Putting sand")
        .description("Puts sands on head.")
        .defaultValue(false)
        .build()
    );
    private final Setting<Boolean> PlaceObsidian = sgGeneral.add(new BoolSetting.Builder()
        .name("Putting obsidian")
        .description("Puts obsidians.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Rotates towards the sands when placing.")
        .defaultValue(true)
        .build()
    );

    private PlayerEntity target = null;

    public AutoSand() {
        super(Addon.combat, "AutoSand", "Automatically places sands to your opponent (rofl module).");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (TargetUtils.isBadTarget(target, range.get())) {
            target = TargetUtils.getPlayerTarget(range.get(), priority.get());
            if (TargetUtils.isBadTarget(target, range.get())) return;
        }

        if (PlaceObsidian.get()) {
            BlockUtils.place(target.getBlockPos().add(1, 1, 0), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
            BlockUtils.place(target.getBlockPos().add(0, 1, 1), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
            BlockUtils.place(target.getBlockPos().add(-1, 1, 0), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
            BlockUtils.place(target.getBlockPos().add(0, 1, -1), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
            BlockUtils.place(target.getBlockPos().add(0, 3, 0), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
        }
        if (PlaceSand.get()) {
            BlockUtils.place(target.getBlockPos().add(0, 2, 0), InvUtils.findInHotbar(Items.SAND), rotate.get(), 0, false);
        }
    }
}

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

public class AutoTrapPlus extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("target-range")
        .description("The maximum distance to target players.")
        .defaultValue(5)
        .range(0, 6)
        .sliderMax(6)
        .build()
    );

    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>()
        .name("target-priority")
        .description("How to filter targets within range.")
        .defaultValue(SortPriority.LowestDistance)
        .build()
    );
    private final Setting<Boolean> dont_click_here = sgGeneral.add(new BoolSetting.Builder()
        .name("dont_click_here")
        .description("dont_click_here.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> AntiSurround = sgGeneral.add(new BoolSetting.Builder()
        .name("AntiSurround")
        .description("puts buttons")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Rotates towards the obsidian when placing.")
        .defaultValue(true)
        .build()
    );

    private PlayerEntity target = null;

    public AutoTrapPlus() {
        super(Addon.combat, "AutoTrapPlus", "Automatically trap your opponent.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (TargetUtils.isBadTarget(target, range.get())) {
            target = TargetUtils.getPlayerTarget(range.get(), priority.get());
            if (TargetUtils.isBadTarget(target, range.get())) return;
        }

        if (dont_click_here.get()) {
            BlockUtils.place(target.getBlockPos().add(1, 1, 0), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
        }
        if (dont_click_here.get()) {
            BlockUtils.place(target.getBlockPos().add(-1, 1, 0), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
        }
        if (dont_click_here.get()) {
            BlockUtils.place(target.getBlockPos().add(0, 1, 1), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
        }
        if (dont_click_here.get()) {
            BlockUtils.place(target.getBlockPos().add(0, 1, -1), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
        }
        if (dont_click_here.get()) {
            BlockUtils.place(target.getBlockPos().add(0, 2, 0), InvUtils.findInHotbar(Items.OBSIDIAN), rotate.get(), 0, false);
        }
        if (AntiSurround.get()) {
            BlockUtils.place(target.getBlockPos().add(1, 0, 0), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        }
        if (AntiSurround.get()) {
            BlockUtils.place(target.getBlockPos().add(0, 0, 1), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        }
        if (AntiSurround.get()) {
            BlockUtils.place(target.getBlockPos().add(-1, 0, 0), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        }
        if (AntiSurround.get()) {
            BlockUtils.place(target.getBlockPos().add(0, 0, -1), InvUtils.findInHotbar(Items.STONE_BUTTON), rotate.get(), 0, false);
        }
    }
}

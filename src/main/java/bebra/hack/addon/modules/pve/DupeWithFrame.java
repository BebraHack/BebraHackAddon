package bebra.hack.addon.modules.pve;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.events.entity.player.AttackEntityEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import net.minecraft.item.Item;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.minecraft.util.Hand;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.List;


public class DupeWithFrame extends Module {
    public DupeWithFrame() {
        super(Addon.misc, "DupeWithFrame", "dupe with frames");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");
    private final SettingGroup sgAutoSelect = settings.createGroup("AutoSelect");

    // General settings
    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("Range")
        .description("Maximum range to the frame")
        .defaultValue(2)
        .range(1, 6)
        .sliderRange(1, 6)
        .build()
    );

    private final Setting<Integer> ticks = sgGeneral.add(new IntSetting.Builder()
        .name("TickDelay")
        .description("Delay in ticks (1 tick = 0.05 seconds)")
        .defaultValue(5)
        .range(1, 50)
        .sliderRange(1, 50)
        .build()
    );

    // Render settings
    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("How the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("The color of the sides of the blocks being rendered.")
        .defaultValue(new SettingColor(204, 0, 0, 10))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The color of the lines of the blocks being rendered.")
        .defaultValue(new SettingColor(204, 0, 0, 255))
        .build()
    );

    // AutoSelect settings

    private final Setting<Boolean> autoSelectEnabled = sgAutoSelect.add(new BoolSetting.Builder()
        .name("auto-select-enabled")
        .description("nu blat, beret predmeti v ruku sam")
        .defaultValue(true)
        .build()
    );

    private final Setting<List<Item>> whitelist = sgAutoSelect.add(new ItemListSetting.Builder()
        .name("whitelist")
        .description("The items you want to dupe")
        .visible(autoSelectEnabled::get)
        .build()
    );

    private int timeout_ticks = 0;
    private GlowItemFrameEntity itemFrame;
    private Box itemFrameBox;
    private boolean interactAccepted = false;

    @Override
    public void onActivate() {
        reset();
        info("Attack the item frame");
        super.onActivate();
    }

    @Override
    public void onDeactivate() {
        reset();
        super.onDeactivate();
    }

    @EventHandler
    public void onInteractEntity(AttackEntityEvent event){
        if (itemFrame == null && event.entity instanceof GlowItemFrameEntity){
            itemFrame = (GlowItemFrameEntity) event.entity;

            itemFrameBox = itemFrame.getBoundingBox();
            Direction.Axis axis = itemFrame.getHorizontalFacing().getAxis();
            itemFrameBox = itemFrameBox.expand(axis == Direction.Axis.X ? 0 : 0.125, axis == Direction.Axis.Y ? 0 : 0.125, axis == Direction.Axis.Z ? 0 : 0.125);

            event.cancel();
        }
    }

    @EventHandler
    public void onWorldRender(Render3DEvent event) {
        if (itemFrame != null) {
            event.renderer.box(itemFrameBox, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }

    @EventHandler
    public void onTick(TickEvent.Post event){
        if (itemFrame != null) {
            if (!itemFrame.isAlive()) {
                reset();
                warning("Item frame is broken");
                info("Attack the item frame");
            }
            else if (timeout_ticks >= ticks.get() && PlayerUtils.distanceTo(itemFrame) <= range.get()) {
                FindItemResult item = InvUtils.find(itemStack -> whitelist.get().contains(itemStack.getItem()));
                InvUtils.move().from(item.slot()).to(mc.player.getInventory().selectedSlot);
                if (itemFrame.getHeldItemStack().isEmpty() && !mc.player.getMainHandStack().isEmpty()) {
                    interactAccepted = mc.interactionManager.interactEntity(mc.player, itemFrame, Hand.MAIN_HAND).isAccepted();
                }

                if (!itemFrame.getHeldItemStack().isEmpty() && interactAccepted) {
                    mc.interactionManager.attackEntity(mc.player, itemFrame);
                    interactAccepted = false;
                }

                timeout_ticks = 0;
            }
        }
        ++timeout_ticks;
    }

    private void reset(){
        itemFrame = null;
        itemFrameBox = null;
        timeout_ticks = 0;
        interactAccepted = false;
    }
}

package com.example.addon.modules.BebraHackPVP;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.systems.modules.Categories;
import com.example.addon.Utils.BEntityUtils;
import com.example.addon.Utils.BWorldUtils;
import meteordevelopment.meteorclient.events.entity.player.FinishUsingItemEvent;
import meteordevelopment.meteorclient.events.entity.player.ItemUseCrosshairTargetEvent;
import meteordevelopment.meteorclient.events.entity.player.StoppedUsingItemEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.player.AutoEat;
import meteordevelopment.meteorclient.systems.modules.player.AutoGap;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;

public class AntiTrap extends Module {
    public enum Mode {
        VClip,
        Chorus
    }

    public enum VClipDirection {
        Up,
        Down
    }

    public enum TrapType {
        BothTrapped,
        AnyTrapped,
        TopTrapped,
        FaceTrapped
    }


    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    // General
    private final Setting<TrapType> trappedWhen = sgGeneral.add(new EnumSetting.Builder<TrapType>()
        .name("activate-on")
        .description("How you must be trapped in order to activate.")
        .defaultValue(TrapType.BothTrapped)
        .build()
    );

    private final Setting<Boolean> onlyOnGround = sgGeneral.add(new BoolSetting.Builder()
        .name("only-on-ground")
        .description("Only activates when you are on the ground.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> onlyInHole = sgGeneral.add(new BoolSetting.Builder()
        .name("only-in-hole")
        .description("Only activates when you are in a hole.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .defaultValue(Mode.Chorus)
        .description("What to do when you are trapped.")
        .build()
    );

    private final Setting<VClipDirection> vClipDirection = sgGeneral.add(new EnumSetting.Builder<VClipDirection>()
        .name("v-clip-direction")
        .description("Direction to VClip towards.")
        .defaultValue(VClipDirection.Up)
        .visible(() -> mode.get() == Mode.VClip)
        .build()
    );

    private final Setting<Integer> minVClipHeight = sgGeneral.add(new IntSetting.Builder()
        .name("min-height")
        .description("Minimum height it will VClip you.")
        .sliderMin(3)
        .min(3)
        .defaultValue(3)
        .visible(() -> mode.get() == Mode.VClip)
        .build()
    );

    private final Setting<Integer> maxVClipHeight = sgGeneral.add(new IntSetting.Builder()
        .name("max-height")
        .description("Maximum height it will VClip you.")
        .sliderMin(3)
        .min(3)
        .defaultValue(4)
        .visible(() -> mode.get() == Mode.VClip)
        .build()
    );

    private final Setting<Boolean> autoMove = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-move")
        .description("Puts a chorus into a selected slot if you don't have one in your hotbar.")
        .defaultValue(true)
        .visible(() -> mode.get() == Mode.Chorus)
        .build()
    );

    private final Setting<Integer> autoMoveSlot = sgGeneral.add(new IntSetting.Builder()
        .name("auto-move-slot")
        .description("The slot auto move moves chorus to.")
        .defaultValue(9)
        .range(1,9)
        .sliderRange(1,9)
        .visible(() -> mode.get() == Mode.Chorus && autoMove.get())
        .build()
    );

    private final Setting<Boolean> autoSwitch = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-switch")
        .description("Switches to chorus automatically.")
        .visible(() -> mode.get() == Mode.Chorus)
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> autoEat = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-eat")
        .description("Eats the chorus automatically.")
        .visible(() -> mode.get() == Mode.Chorus)
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> debug = sgGeneral.add(new BoolSetting.Builder()
        .name("debug")
        .description("Show the module's processes in chat.")
        .defaultValue(false)
        .build()
    );


    public AntiTrap() {
        super(Addon.combat, "anti-trap", "Tries to save you after getting trapped.");
    }


    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();

    private boolean eating;
    private boolean swapped;


    @EventHandler
    private void onTick(TickEvent.Post event) {
        assert mc.player != null;

        blockPos.set(BWorldUtils.roundBlockPos(mc.player.getPos()));

        if (onlyOnGround.get() && !mc.player.isOnGround()) return;
        if (onlyInHole.get() && !BEntityUtils.isInHole(mc.player, true, BEntityUtils.BlastResistantType.Any)) return;

        if (isTrapped()) {
            if (mode.get() == Mode.VClip) doVClip();
            else doChorus();
        }

    }

    private boolean isTrapped() {
        switch (trappedWhen.get()) {
            case BothTrapped -> {
                return BEntityUtils.isBothTrapped(mc.player, BEntityUtils.BlastResistantType.Any);
            }
            case AnyTrapped -> {
                return BEntityUtils.isAnyTrapped(mc.player, BEntityUtils.BlastResistantType.Any);
            }
            case TopTrapped -> {
                return BEntityUtils.isTopTrapped(mc.player, BEntityUtils.BlastResistantType.Any);
            }
            case FaceTrapped -> {
                return BEntityUtils.isFaceSurrounded(mc.player, BEntityUtils.BlastResistantType.Any);
            }
        }

        return false;
    }

    private void doVClip() {
        if (vClipDirection.get() == VClipDirection.Up && upperSpace() != 0) {
            mc.player.setPosition(mc.player.getX(), mc.player.getY() + upperSpace(), mc.player.getZ());
            if (debug.get()) info(String.valueOf(upperSpace()));
        } else if (vClipDirection.get() == VClipDirection.Down && lowerSpace() != 0){
            mc.player.setPosition(mc.player.getX(), mc.player.getY() + lowerSpace(), mc.player.getZ());
            if (debug.get()) info(String.valueOf(lowerSpace()));
        }
    }

    private int upperSpace() {
        for (int dy = minVClipHeight.get(); dy <= maxVClipHeight.get(); dy++) {
            BlockState blockState1 = mc.world.getBlockState(blockPos.set(mc.player.getX(), mc.player.getY() + dy, mc.player.getZ()));
            BlockState blockState2 = mc.world.getBlockState(blockPos.set(mc.player.getX(), mc.player.getY() + dy + 1, mc.player.getZ()));

            boolean air1 = blockState1.getMaterial().isReplaceable();
            boolean air2 = blockState2.getMaterial().isReplaceable();

            if (air1 & air2) return dy;
        }

        return 0;
    }

    private int lowerSpace() {
        for (int dy = -minVClipHeight.get(); dy >= -maxVClipHeight.get(); dy--) {
            BlockState blockState1 = mc.world.getBlockState(blockPos.set(mc.player.getX(), mc.player.getY() + dy, mc.player.getZ()));
            BlockState blockState2 = mc.world.getBlockState(blockPos.set(mc.player.getX(), mc.player.getY() + dy + 1, mc.player.getZ()));

            boolean air1 = blockState1.getMaterial().isReplaceable();
            boolean air2 = blockState2.getMaterial().isReplaceable();

            if (air1 & air2) return dy;
        }

        return 0;
    }

    private void doChorus() {
        FindItemResult hotbarChorus = InvUtils.findInHotbar(Items.CHORUS_FRUIT);
        FindItemResult chorus = InvUtils.find(Items.CHORUS_FRUIT);

        if (!hotbarChorus.found() && autoMove.get()) {
            if (chorus.found() && chorus.slot() != autoMoveSlot.get() - 1) {
                InvUtils.move().from(chorus.slot()).toHotbar(autoMoveSlot.get() - 1);
            }
        }

        if (Modules.get().get(AutoGap.class).isEating() || Modules.get().get(AutoEat.class).eating) return;

        if (hotbarChorus.found() && autoSwitch.get() && !swapped) {
            InvUtils.swap(hotbarChorus.slot(), false);
            swapped = true;
        }

        if (hotbarChorus.found() && autoEat.get()) {
            if (!eating){
                eat();
            }
        }
    }

    @EventHandler
    private void onItemUseCrosshairTarget(ItemUseCrosshairTargetEvent event) {
        if (eating) event.target = null;
    }

    private void eat() {
        if (mc.player.getMainHandStack().getItem() == Items.CHORUS_FRUIT){
            setPressed(true);
            if (!mc.player.isUsingItem()) Utils.rightClick();
            eating = true;
        }
    }

    private void stopEating() {
        eating = false;
        setPressed(false);
        swapped = false;
    }

    private void setPressed(boolean pressed) {
        mc.options.useKey.setPressed(pressed);
    }

    @Override
    public void onDeactivate()
    {
        stopEating();
    }

    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof UpdateSelectedSlotC2SPacket) {
            stopEating();
        }
    }

    @EventHandler
    private void onFinishUsingItem(FinishUsingItemEvent event) {
        stopEating();
    }

    @EventHandler
    private void onStoppedUsingItem(StoppedUsingItemEvent event) {
        stopEating();
    }
}

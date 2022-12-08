package com.example.addon.modules.BebraHackRender;
/*
import com.example.addon.Addon;
import meteordevelopment.meteorclient.systems.modules.Categories;
import com.example.addon.Utils.BEntityUtils;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class BurrowESP extends Module {
    private final SettingGroup sgRender = settings.createGroup("Render");


    // Render
    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("How the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("The side color of the rendering.")
        .defaultValue(new SettingColor(230, 0, 255, 5))
        .visible(() -> shapeMode.get() != ShapeMode.Lines)
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The line color of the rendering.")
        .defaultValue(new SettingColor(250, 0, 255, 255))
        .visible(() -> shapeMode.get() != ShapeMode.Sides)
        .build()
    );

    private final Setting<Boolean> renderWebbed = sgRender.add(new BoolSetting.Builder()
        .name("Render webbed")
        .description("Will render if the target is webbed.")
        .defaultValue(true)
        .build()
    );

    private final Setting<SettingColor> webSideColor = sgRender.add(new ColorSetting.Builder()
        .name("web-side-color")
        .description("The side color of the rendering for webs.")
        .defaultValue(new SettingColor(240, 250, 65, 35))
        .visible(() -> shapeMode.get() != ShapeMode.Lines && renderWebbed.get())
        .build()
    );

    private final Setting<SettingColor> webLineColor = sgRender.add(new ColorSetting.Builder()
        .name("web-line-color")
        .description("The line color of the rendering for webs.")
        .defaultValue(new SettingColor(0, 0, 0, 0))
        .visible(() -> shapeMode.get() != ShapeMode.Sides && renderWebbed.get())
        .build()
    );


    public BurrowESP() {
        super(Addon.render, "Burrow-ESP", "Displays if the closest target to you is burrowed / webbed.");
    }


    public BlockPos target;
    public boolean isTargetWebbed;
    public boolean isTargetBurrowed;

    @EventHandler
    private void onTick(TickEvent.Post event) {
        PlayerEntity targetEntity = TargetUtils.getPlayerTarget(mc.interactionManager.getReachDistance() + 2, SortPriority.LowestDistance);

        if (TargetUtils.isBadTarget(targetEntity, mc.interactionManager.getReachDistance() + 2)) {
            target = null;
        } else if (renderWebbed.get() && BEntityUtils.isWebbed(targetEntity)) {
            target = targetEntity.getBlockPos();
        } else if (BEntityUtils.isBurrowed(targetEntity, BEntityUtils.BlastResistantType.Any)) {
            target = targetEntity.getBlockPos();
        } else target = null;

        isTargetWebbed = (target != null && BEntityUtils.isWebbed(targetEntity));
        isTargetBurrowed = (target != null && BEntityUtils.isBurrowed(targetEntity, BEntityUtils.BlastResistantType.Any));
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (target == null) return;
        if (isTargetWebbed) event.renderer.box(target, webSideColor.get(), webLineColor.get(), shapeMode.get(), 0);
        else if (isTargetBurrowed) event.renderer.box(target, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }
}

 */

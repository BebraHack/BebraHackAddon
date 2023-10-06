package bebra.hack.addon.modules.render;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

//by red14
public class Bubbles extends Module {

    public Bubbles()  {
        super(Addon.render, "Bubbles", "Create bubbles on the blocks you're looking at.");
    }

    boolean savePrev;
    BlockPos bpp;
    BlockPos bp;

    @Override
    public void onActivate() {
        savePrev = true;
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (mc.crosshairTarget instanceof BlockHitResult block) {
            bp = block.getBlockPos();

            if (savePrev) {
                bpp = bp;
                savePrev = false;
            }

            Color line = new Color(180, 0, 0, 255);
            Color side = new Color(225, 0, 0, 50);
            ShapeMode sm = ShapeMode.Both;

            event.renderer.box(bp, side, line, sm, 0);

            if (bpp.getX() != bp.getX() || bpp.getZ() != bp.getZ() || bpp.getY() != bp.getY()) {
                RenderUtils.renderTickingBlock(bpp, new Color(225, 0, 0, 50), line, sm, 0, 12, false, true);
                savePrev = true;
            }
        }
    }

}

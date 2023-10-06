package bebra.hack.addon.modules.render;

import bebra.hack.addon.Addon;
import meteordevelopment.meteorclient.systems.modules.Module;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.combat.Surround;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
public class Bubbles2 extends Module {
    public Bubbles2() {
        super(Addon.render, "Bubbles2", "Bubbles, but 2 version.");
    }



    @EventHandler
    public void onRender(Render3DEvent event)
    {
        BlockPos playerPos;

        {
            assert mc.player != null;
            playerPos = mc.player.getBlockPos();
        }

        int blockX = playerPos.getX();
        int blockY = playerPos.getY();
        int blockZ = playerPos.getZ();

        BlockPos blockPos = new BlockPos(blockX, blockY-1, blockZ);

        Color line = new Color(180, 0, 0, 255);
        Color side = new Color(225, 0, 0, 50);
        ShapeMode sm = ShapeMode.Both;

        RenderUtils.renderTickingBlock(blockPos, new Color(225, 0, 0, 50), line, sm, 0, 20, false, true);    }
}

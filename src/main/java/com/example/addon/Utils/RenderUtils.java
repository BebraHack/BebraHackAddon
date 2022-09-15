package com.example.addon.Utils;

import com.example.addon.Utils.RenderInfo;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.systems.modules.render.Breadcrumbs;
import meteordevelopment.meteorclient.utils.misc.Vec3;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import static com.example.addon.Utils.BlockInfo.*;

public class RenderUtils {

    // Скрытие сеттинга
    // пример: .visible(() -> visibleHeight(renderMode.get()));
    public static boolean visibleHeight(RenderMode renderMode) {
        return renderMode == RenderMode.UpperSide || renderMode == RenderMode.LowerSide;
    }

    public static boolean visibleSide(ShapeMode shapeMode) {
        return shapeMode == ShapeMode.Both || shapeMode == ShapeMode.Sides;
    }

    public static boolean visibleLine(ShapeMode shapeMode) {
        return shapeMode == ShapeMode.Both || shapeMode == ShapeMode.Lines;
    }

    // Рендер метеора
    // пример: RenderInfo ri = new RenderInfo(event, RenderMode.Shape, ShapeMode.Both);
    // height: если не используется в модуле - 1;
    public static void render(RenderInfo ri, BlockPos blockPos, Color sideColor, Color lineColor, double height) {
        if (isNull(blockPos)) return;

        switch (ri.renderMode) {
            case Box -> box(ri, blockPos, sideColor, lineColor);
            case UpperSide -> side(ri, blockPos, sideColor, lineColor, Side.Upper, height);
            case LowerSide -> side(ri, blockPos, sideColor, lineColor, Side.Lower, height);
            case Shape -> shape(ri, blockPos, sideColor, lineColor);
            case Romb -> romb(ri, blockPos, sideColor, lineColor, Side.Default, height);
            case UpperRomb -> romb(ri, blockPos, sideColor, lineColor, Side.Upper, height);
        }
    }

    // Рендер ромба
    private static void romb(RenderInfo ri, BlockPos blockPos, Color sideColor, Color lineColor, Side side, double height) {
        switch (side) {
            case Default -> {
                // North
                render(ri, blockPos, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.5, 0, 0, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 0.0, 0.0, 0.5, 0.5, 0.0, 0.5, 0, 0, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.0, 0.5, 0.0, 0.0, 0.5, 0.0, 0.5, 0.5, 0, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.5, 0.5, 0, 0.5, 0, 0, 0.5, 0, 0, sideColor, lineColor, ri.shapeMode);

                // South
                render(ri, blockPos, 0.0, 0.0, 1.0, 0.0, 0.5, 0.0, 0.5, 0, 0, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 0.0, 1.0, 0.5, 0.5, 0.0, 0.5, 0, 0, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.0, 0.5, 1.0, 0.0, 0.5, 0.0, 0.5, 0.5, 0, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 0.5, 1.0, 0.0, 0.5, 0.0, 0.5, 0.5, 0, 0.5, 0, 0, 0.5, 0, 0, sideColor, lineColor, ri.shapeMode);

                // East
                render(ri, blockPos, 1.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 1.0, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0, 0.5, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 1.0, 0.5, 0.5, 0.0, 0.5, 0.0, 0.0, 0.5, 0.5, 0, 0, 0.5, 0, 0.5, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 1.0, -0.5, 0.5, 0.0, 0.5, 0.0, 0.0, 1, 0.5, 0, 0.5, 0.5, 0, 0.5, 0, sideColor, lineColor, ri.shapeMode);

                // West
                render(ri, blockPos, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.0, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0, 0.5, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.0, 0.5, 0.5, 0.0, 0.5, 0.0, 0.0, 0.5, 0.5, 0, 0, 0.5, 0, 0.5, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.0, -0.5, 0.5, 0.0, 0.5, 0.0, 0.0, 1, 0.5, 0, 0.5, 0.5, 0, 0.5, 0, sideColor, lineColor, ri.shapeMode);

                // Up
                render(ri, blockPos, 0.0, 1, 0.0, 0.5, 0.0, 0.0, 0.0, 0, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 1, 0.0, 0.5, 0.0, 0.0, 0.5, 0, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 1, 0.5, 0.5, 0.0, 0.0, 0.5, 0, 0.5, 0, 0, 0.5, 0.5, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.0, 1, 0.5, 0.5, 0.0, 0.5, 0.0, 0, 0.5, 0, 0, 0.5, 0.0, 0, 0, sideColor, lineColor, ri.shapeMode);

                // Down
                render(ri, blockPos, 0.0, 0, 0.0, 0.5, 0.0, 0.0, 0.0, 0, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 0, 0.0, 0.5, 0.0, 0.0, 0.5, 0, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 0, 0.5, 0.5, 0.0, 0.0, 0.5, 0, 0.5, 0, 0, 0.5, 0.5, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.0, 0, 0.5, 0.5, 0.0, 0.5, 0.0, 0, 0.5, 0, 0, 0.5, 0.0, 0, 0, sideColor, lineColor, ri.shapeMode);
            }
            case Upper -> {
                // Up
                render(ri, blockPos, 0.0, 1, 0.0, 0.5, 0.0, 0.0, 0.0, 0, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 1, 0.0, 0.5, 0.0, 0.0, 0.5, 0, 0.5, 0, 0, 0, 0, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.5, 1, 0.5, 0.5, 0.0, 0.0, 0.5, 0, 0.5, 0, 0, 0.5, 0.5, 0, 0, sideColor, lineColor, ri.shapeMode);
                render(ri, blockPos, 0.0, 1, 0.5, 0.5, 0.0, 0.5, 0.0, 0, 0.5, 0, 0, 0.5, 0.0, 0, 0, sideColor, lineColor, ri.shapeMode);
            }
        }
    }

    private static void render(RenderInfo ri, BlockPos blockPos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Color sideColor, Color lineColor, ShapeMode shapeMode) {
        Vec3d vec3d = new Vec3d(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);

        ri.event.renderer.side(vec3d.x + x1, vec3d.y + y1, vec3d.z + z1, vec3d.x + x2, vec3d.y + y2, vec3d.z + z2, vec3d.x + x3, vec3d.y + y3, vec3d.z + z3, vec3d.x + x4, vec3d.y + y4, vec3d.z + z4, sideColor, lineColor, shapeMode);
    }

    private static void line(RenderInfo ri, BlockPos blockPos, double x, double y, double z, double x1, double y1, double z1, Color lineColor) {
        Vec3d vec3d = new Vec3d(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);

        ri.event.renderer.line(vec3d.x + x, vec3d.y + y, vec3d.z + z,x1,y1,z1, lineColor);
    }

    // Рендер по обводке блока
    private static void shape(RenderInfo ri, BlockPos blockPos, Color sideColor, Color lineColor) {
        if (getShape(blockPos).isEmpty()) return;

        render(ri, blockPos, getBox(blockPos), sideColor, lineColor);
    }

    // Обычный рендер блока
    private static void box(RenderInfo ri, BlockPos blockPos, Color sideColor, Color lineColor) {
        ri.event.renderer.box(blockPos, sideColor, lineColor, ri.shapeMode, 0);
    }

    // Рендер верхней и нижней части блока
    private static void side(RenderInfo ri, BlockPos blockPos, Color sideColor, Color lineColor, Side side, double height) {
        double y = side == Side.Upper ? blockPos.getY() + 1 : blockPos.getY();
        ri.event.renderer.box(
            blockPos.getX(), blockPos.getY() + height, blockPos.getZ(),
            blockPos.getX() + 1, y, blockPos.getZ() + 1,
            sideColor, lineColor, ri.shapeMode, 0);
    }

    // Часть рендера метода shape
    private static void render(RenderInfo ri, BlockPos blockPos, Box box, Color sideColor, Color lineColor) {
        ri.event.renderer.box(blockPos.getX() + box.minX, blockPos.getY() + box.minY, blockPos.getZ() + box.minZ, blockPos.getX() + box.maxX, blockPos.getY() + box.maxY, blockPos.getZ() + box.maxZ, sideColor, lineColor, ri.shapeMode, 0);
    }

    // Кастомный рендер блока
    public static void thickRender(Render3DEvent event, BlockPos pos, ShapeMode mode, Color sideColor, Color sideColor2, Color lineColor, Color lineColor2, double lineSize) {
        double low = lineSize;
        double high = 1 - low;

        if (mode == ShapeMode.Lines || mode == ShapeMode.Both) {
            // Sides
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY() + 1, pos.getZ() + low, lineColor, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + low, pos.getY() + 1, pos.getZ(), lineColor, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX() + 1, pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + low, lineColor, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX() + 1, pos.getY(), pos.getZ(), pos.getX() + high, pos.getY() + 1, pos.getZ(), lineColor, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY(), pos.getZ() + 1, pos.getX(), pos.getY() + 1, pos.getZ() + high, lineColor, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY(), pos.getZ() + 1, pos.getX() + low, pos.getY() + 1, pos.getZ() + 1, lineColor, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX() + 1, pos.getY(), pos.getZ() + 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + high, lineColor, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX() + 1, pos.getY(), pos.getZ() + 1, pos.getX() + high, pos.getY() + 1, pos.getZ() + 1, lineColor, lineColor2);

            // Up
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + high, pos.getZ(), lineColor, lineColor);
            event.renderer.quadHorizontal(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getZ() + low, lineColor);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX(), pos.getY() + high, pos.getZ() + 1, lineColor, lineColor);
            event.renderer.quadHorizontal(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + low, pos.getZ() + 1, lineColor);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY() + 1, pos.getZ() + 1, pos.getX() + 1, pos.getY() + high, pos.getZ() + 1, lineColor, lineColor);
            event.renderer.quadHorizontal(pos.getX(), pos.getY() + 1, pos.getZ() + 1, pos.getX() + 1, pos.getZ() + high, lineColor);
            event.renderer.gradientQuadVertical(pos.getX() + 1, pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + high, pos.getZ() + 1, lineColor, lineColor);
            event.renderer.quadHorizontal(pos.getX() + 1, pos.getY() + 1, pos.getZ(), pos.getX() + high, pos.getZ() + 1, lineColor);

            // Down
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + low, pos.getZ(), lineColor2, lineColor2);
            event.renderer.quadHorizontal(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getZ() + low, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY() + low, pos.getZ() + 1, lineColor2, lineColor2);
            event.renderer.quadHorizontal(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + low, pos.getZ() + 1, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY(), pos.getZ() + 1, pos.getX() + 1, pos.getY() + low, pos.getZ() + 1, lineColor2, lineColor2);
            event.renderer.quadHorizontal(pos.getX(), pos.getY(), pos.getZ() + 1, pos.getX() + 1, pos.getZ() + high, lineColor2);
            event.renderer.gradientQuadVertical(pos.getX() + 1, pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + low, pos.getZ() + 1, lineColor2, lineColor2);
            event.renderer.quadHorizontal(pos.getX() + 1, pos.getY(), pos.getZ(), pos.getX() + high, pos.getZ() + 1, lineColor2);
        }

        if (mode == ShapeMode.Sides || mode == ShapeMode.Both) {
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ(), sideColor, sideColor2);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY() + 1, pos.getZ() + 1, sideColor, sideColor2);
            event.renderer.gradientQuadVertical(pos.getX() + 1, pos.getY(), pos.getZ() + 1, pos.getX() + 1, pos.getY() + 1, pos.getZ(), sideColor, sideColor2);
            event.renderer.gradientQuadVertical(pos.getX() + 1, pos.getY(), pos.getZ() + 1, pos.getX(), pos.getY() + 1, pos.getZ() + 1, sideColor, sideColor2);
            event.renderer.quadHorizontal(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getZ() + 1, sideColor);
            event.renderer.quadHorizontal(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getZ() + 1, sideColor2);
        }
    }

    // Кастомный рендер верхней части блока
    public static void thickUpperSide(Render3DEvent event, BlockPos pos, ShapeMode mode, Color sideColor, Color lineColor, double lineSize) {
        double low = lineSize;
        double high = 1 - low;

        if (mode == ShapeMode.Lines || mode == ShapeMode.Both) {
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + high, pos.getZ(), lineColor, lineColor);
            event.renderer.quadHorizontal(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getZ() + low, lineColor);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX(), pos.getY() + high, pos.getZ() + 1, lineColor, lineColor);
            event.renderer.quadHorizontal(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + low, pos.getZ() + 1, lineColor);
            event.renderer.gradientQuadVertical(pos.getX(), pos.getY() + 1, pos.getZ() + 1, pos.getX() + 1, pos.getY() + high, pos.getZ() + 1, lineColor, lineColor);
            event.renderer.quadHorizontal(pos.getX(), pos.getY() + 1, pos.getZ() + 1, pos.getX() + 1, pos.getZ() + high, lineColor);
            event.renderer.gradientQuadVertical(pos.getX() + 1, pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + high, pos.getZ() + 1, lineColor, lineColor);
            event.renderer.quadHorizontal(pos.getX() + 1, pos.getY() + 1, pos.getZ(), pos.getX() + high, pos.getZ() + 1, lineColor);
        }

        if (mode == ShapeMode.Sides || mode == ShapeMode.Both) {
            event.renderer.quadHorizontal(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getZ() + 1, sideColor);
        }
    }

    public enum Render {
        Meteor, BedTrap, None
    }

    public enum RenderMode {
        Box, UpperSide, LowerSide, Shape, Romb, UpperRomb, None
        // Shape не работает с кастомным рендером
        // None чтоб не создавать дополнительный булеан для работы рендера
    }

    public enum Side {
        Default, Upper, Lower
    }
}

package com.example.addon.Utils;

import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IRenderer2D {
    void texQuadHFlip(double x, double y, double width, double height, Color color);
    void texQuadVFlip(double x, double y, double width, double height, Color color);
    void texQuadHVFlip(double x, double y, double width, double height, Color color);
}

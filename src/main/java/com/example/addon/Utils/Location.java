package com.example.addon.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Location {
    public PlayerEntity player;
    public Vec3d vec3d;
    public double x, y, z;

    public Location(PlayerEntity player) {
        this.player = player;
        this.vec3d = player.getPos();
        this.x = this.vec3d.x;
        this.y = this.vec3d.y;
        this.z = this.vec3d.z;
    }
}

package com.example.addon.Utils;

import meteordevelopment.meteorclient.systems.friends.Friends;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class TargetHelper {
    public static List<PlayerEntity> getTargetsInRange(double enemyRange) {
        List<PlayerEntity> stream = mc.world.getPlayers()
            .stream()
            .filter(e -> e != mc.player)
            .filter(e -> e.isAlive())
            .filter(e -> !Friends.get().isFriend(e))
            .filter(e -> ((PlayerEntity) e).getHealth() > 0)
            .filter(e -> mc.player.distanceTo(e) < enemyRange)
            .sorted(Comparator.comparing(e -> mc.player.distanceTo(e)))
            .collect(Collectors.toList());
        return stream;
    }

    public static List<PlayerEntity> getFriendsInRange(double enemyRange){
        List<PlayerEntity> stream = mc.world.getPlayers()
            .stream()
            .filter(e -> e != mc.player)
            .filter(e -> e.isAlive())
            .filter(e -> Friends.get().isFriend(e))
            .filter(e -> ((PlayerEntity) e).getHealth() > 0)
            .filter(e -> mc.player.distanceTo(e) < enemyRange)
            .sorted(Comparator.comparing(e -> mc.player.distanceTo(e)))
            .collect(Collectors.toList());
        return stream;
    }
}

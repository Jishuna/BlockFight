package me.jishuna.blockfight;

import org.bukkit.block.BlockFace;

public class Utils {
    public static float getYaw(BlockFace face) {
        return switch (face) {
        case NORTH -> -180;
        case EAST -> -90;
        case WEST -> 90;
        default -> 0;
        };
    }
}

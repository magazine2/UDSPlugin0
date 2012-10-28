package com.undeadscythes.udsplugin.utilities;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;

public final class BlockUtils {
    static Material[] airBlocks = new Material[]{Material.AIR, Material.WATER, Material.VINE, Material.LADDER, Material.WOODEN_DOOR};
    static Material[] deathBlocks = new Material[]{Material.WEB, Material.LAVA, Material.STATIONARY_LAVA, Material.CACTUS};
    static Material[] floatyBlocks = new Material[]{Material.AIR, Material.LOG, Material.LEAVES, Material.LONG_GRASS, Material.RED_ROSE, Material.YELLOW_FLOWER};
    static Material[] ignoredBlocks = new Material[]{Material.FENCE};

    public static boolean isAirBlock(Material material) {
        return ArrayUtils.contains(airBlocks, material);
    }

    public static boolean isAirBlock(Block block) {
        return isAirBlock(block.getType());
    }

    public static boolean isDeathBlock(Block block) {
        return ArrayUtils.contains(deathBlocks, block.getType());
    }

    public static boolean isFloatyBlock(Material material) {
        return ArrayUtils.contains(floatyBlocks, material);
    }

    public static boolean isFloatyBlock(Block block) {
        return isFloatyBlock(block.getType());
    }

    public static boolean isIgnoredBlock(Material material) {
        return ArrayUtils.contains(ignoredBlocks, material);
    }

    public static boolean isIgnoredBlock(Block block) {
        return isIgnoredBlock(block.getType());
    }
}

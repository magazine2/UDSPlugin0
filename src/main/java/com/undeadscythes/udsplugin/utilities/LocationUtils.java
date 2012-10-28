package com.undeadscythes.udsplugin.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * A collection of tools to find various objects within hash maps.
 * @author UndeadScythes
 */
public final class LocationUtils {
    /**
     * Find a safe location above this location, safe meaning two consecutive air blocks.
     * @param location Location.
     * @return Nearest safe location or spawn of world.
     */
    public static Location findSafePlace(final Location location) {
        int x = location.getBlockX();
        int z = location.getBlockZ();
        int yTestUp = location.getBlockY();
        int yTestDown = location.getBlockY() - 1;
        final World world = location.getWorld();
        while(yTestUp < 254 && yTestDown > 1) {
            if(!BlockUtils.isDeathBlock(world.getBlockAt(x, yTestUp - 1, z)) &&
               BlockUtils.isAirBlock(world.getBlockAt(x, yTestUp, z)) &&
               BlockUtils.isAirBlock(world.getBlockAt(x, yTestUp + 1, z))) {
                Location safePlace = new Location(world, location.getX(), yTestUp, location.getZ());
                safePlace.setPitch(location.getPitch());
                safePlace.setYaw(location.getYaw());
                return safePlace;
            }
            if(!BlockUtils.isDeathBlock(world.getBlockAt(x, yTestDown - 1, z)) &&
               BlockUtils.isAirBlock(world.getBlockAt(x, yTestDown, z)) &&
               BlockUtils.isAirBlock(world.getBlockAt(x, yTestDown + 1, z))) {
                Location safePlace = new Location(world, location.getX(), yTestDown, location.getZ());
                safePlace.setPitch(location.getPitch());
                safePlace.setYaw(location.getYaw());
                return safePlace;
            }
            yTestUp++;
            yTestDown--;
        }
        return world.getSpawnLocation();
    }

    public static Location parseLocation(String string) {
        String[] split = string.split(",");
        if(split[0].equals("null")) return null; // Remove this line once all 'edits' are done and location defaults are set properly.
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        Location location = new Location(Bukkit.getWorld(split[0]), x, y, z);
        location.setPitch(Float.parseFloat(split[4]));
        location.setYaw(Float.parseFloat(split[5]));
        return location;
    }

    public static String getString(Location location) {
        if(location == null) return "null"; // Also remove this line for the same reason as above.
        return location.getWorld().getName() + "," +
               location.getX() + "," +
               location.getY() + "," +
               location.getZ() + "," +
               location.getPitch() + "," +
               location.getYaw();
    }

    public static Location findSurface(Location location) {
        int x = location.getBlockX();
        int testY = location.getBlockY() + 1;
        int z = location.getBlockZ();
        World world = location.getWorld();
        while (testY > 1) {
            if(BlockUtils.isFloatyBlock(world.getBlockAt(x, testY, z))) {
                testY--;
                continue;
            } else if(BlockUtils.isIgnoredBlock(world.getBlockAt(x, testY - 1, z))) {
                testY++;
                continue;
            } else {
                return new Location(world, x, testY, z);
            }
        }
        return location;
    }

    public static Location findSurface(World world, int x, int z) {
        return findSurface(world.getHighestBlockAt(x, z).getLocation());
    }
}

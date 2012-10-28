package com.undeadscythes.udsplugin.utilities;

import com.undeadscythes.udsplugin.BaseBlock;
import com.undeadscythes.udsplugin.Cuboid;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.WESession;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 *
 * @author Dave
 */
public class WEUtils {
    public static void placeSurfaceCorner(World world, Material material, int x, int dX, int z, int dZ) {
        if(dX > 0) {
            placeSurfaceLineX(world, material, x + 1, x + dX, z);
        } else {
            placeSurfaceLineX(world, material, x - 1, x + dX, z);
        }
        placeSurfaceLineZ(world, material, x, z, z + dZ);
    }

    public static void placeSurfaceLineX(World world, Material material, int x1, int x2, int z) {
        int min = Math.min(x1, x2);
        int max = Math.max(x1, x2);
        int dX = max - min;
        for(int i = 0; i <= dX; i++) {
            world.getBlockAt(LocationUtils.findSurface(world, min + i, z)).getRelative(BlockFace.UP).setType(material);
        }
    }

    public static void placeSurfaceLineZ(World world, Material material, int x, int z1, int z2) {
        int min = Math.min(z1, z2);
        int max = Math.max(z1, z2);
        int dZ = max - min;
        for(int i = 0; i <= dZ; i++) {
            world.getBlockAt(LocationUtils.findSurface(world, x, min + i)).getRelative(BlockFace.UP).setType(material);
        }
    }

    public static Location placeTower(World world, Material material, int x, int z, int height) {
        Location location = LocationUtils.findSurface(world.getHighestBlockAt(x, z).getLocation());
        for(int i = 0; i < height; i++) {
            Location step = location.add(0, i, 0);
            world.getBlockAt(step).setType(material);
        }
        return location.add(0, height, 0);
    }

    public static WESession forceSession(Player player) {
        WESession session = getSession(player);
        if(session == null) {
            session = new WESession(player);
            addSession(session);
        }
        return session;
    }

    public static WESession getSession(Player player) {
        return UDSPlugin.getWESessions().get(player.getName());
    }

    public static void addSession(WESession session) {
        UDSPlugin.getWESessions().put(session.getName(), session);
    }

    public static void put(Cuboid cuboid) {
        final Vector min = cuboid.getV1();
        final Vector max = cuboid.getV2();
        BaseBlock[][][] blocks = cuboid.getBlocks();
        World world = cuboid.getWorld();
        int dX = max.getBlockX() - min.getBlockX();
        int dY = max.getBlockY() - min.getBlockY();
        int dZ = max.getBlockZ() - min.getBlockZ();
        for(int x = 0; x <= dX; x++) {
            for(int y = 0; y <= dY; y++) {
                for(int z = 0; z <= dZ; z++) {
                    world.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).setTypeIdAndData(blocks[x][y][z].getType(), blocks[x][y][z].getData(), true);
                }
            }
        }
    }

    public static void put(Cuboid cuboid, Location location) {
        int dX = (int)(location.getX() - cuboid.getPOV().getX());
        int dY = (int)(location.getY() - cuboid.getPOV().getY());
        int dZ = (int)(location.getZ() - cuboid.getPOV().getZ());
        put(cuboid.getWorld(), cuboid.getBlocks(), cuboid.getV1().clone().add(new Vector(dX, dY, dZ)), cuboid.getV2().clone().add(new Vector(dX, dY, dZ)));
    }

    public static void put(World world, Vector min, Vector max, Material material) {
        for(int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for(int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for(int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }

    public static void put(World world, BaseBlock[][][] blocks, Vector v1, Vector v2) {
        Vector min = VectorUtils.findMin(v1, v2);
        Vector max = VectorUtils.findMax(v1, v2);
        int dX = max.getBlockX() - min.getBlockX();
        int dY = max.getBlockY() - min.getBlockY();
        int dZ = max.getBlockZ() - min.getBlockZ();
        for(int x = 0; x <= dX; x++) {
            for(int y = 0; y <= dY; y++) {
                for(int z = 0; z <= dZ; z++) {
                    world.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).setTypeIdAndData(blocks[x][y][z].getType(), blocks[x][y][z].getData(), true);
                }
            }
        }
    }
}

package com.undeadscythes.udsplugin;

import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.VectorUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * A selection of blocks in a 3D grid used for world edits.
 * @author Dave
 */
public class Cuboid {
    private BaseBlock[][][] blocks;
    private Vector v1;
    private Vector v2;
    private Location POV;

    public Cuboid(World world, Player player) {
        POV = player.getLocation();
        UDSPlayer udsPlayer = PlayerUtils.getUDS(player);
        v1 = VectorUtils.findMin(udsPlayer.getEditPoint1(), udsPlayer.getEditPoint2());
        v2 = VectorUtils.findMax(udsPlayer.getEditPoint1(), udsPlayer.getEditPoint2());
        int dX = v2.getBlockX() - v1.getBlockX();
        int dY = v2.getBlockY() - v1.getBlockY();
        int dZ = v2.getBlockZ() - v1.getBlockZ();
        blocks = new BaseBlock[dX + 1][dY + 1][dZ + 1];
        for(int x = 0; x <= dX; x++) {
            for(int y = 0; y <= dY; y++) {
                for(int z = 0; z <= dZ; z++) {
                    blocks[x][y][z] = new BaseBlock(world.getBlockAt(v1.getBlockX() + x, v1.getBlockY() + y, v1.getBlockZ() + z));
                }
            }
        }
    }

    public Cuboid(World world, Vector v1, Vector v2) {
        POV = world.getSpawnLocation();
        this.v1 = VectorUtils.findMin(v1, v2);
        this.v2 = VectorUtils.findMax(v1, v2);
        int dX = v2.getBlockX() - v1.getBlockX();
        int dY = v2.getBlockY() - v1.getBlockY();
        int dZ = v2.getBlockZ() - v1.getBlockZ();
        blocks = new BaseBlock[dX + 1][dY + 1][dZ + 1];
        for(int x = 0; x <= dX; x++) {
            for(int y = 0; y <= dY; y++) {
                for(int z = 0; z <= dZ; z++) {
                    blocks[x][y][z] = new BaseBlock(world.getBlockAt(v1.getBlockX() + x, v1.getBlockY() + y, v1.getBlockZ() + z));
                }
            }
        }
    }

    public World getWorld() {
        return POV.getWorld();
    }

    public BaseBlock[][][] getBlocks() {return blocks;}
    public Vector getV1() {return v1;}
    public Vector getV2() {return v2;}
    public Location getPOV() {return POV;}
}

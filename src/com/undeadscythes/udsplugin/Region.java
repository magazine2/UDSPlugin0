package com.undeadscythes.udsplugin;

import com.undeadscythes.udsplugin.exceptions.NoFlagException;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import com.undeadscythes.udsplugin.utilities.VectorUtils;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Represents 3D regions and its properties.
 * @author UndeadScythes
 */
public class Region {
    private String name;
    private Vector minVector;
    private Vector maxVector;
    private String type;
    private String owner;
    private List<String> members;
    private boolean[] flags = new boolean[]{false, false, true, true, false, false, true, false, false};;
    private Location warp;

    private static String[] fields = {"name", "v1", "v2", "warp", "owner", "flags", "members"};

    /**
     * Standard region constructor.
     * @param name Region name.
     * @param vector1 Vector 1.
     * @param vector2 Vector 2.
     * @param world World name.
     * @param owner Player name or group (g:group).
     */
    public Region(final String name, final Vector vector1, final Vector vector2, final Location warp, final String owner) {
        this.name = name;
        this.warp = warp;
        this.minVector = VectorUtils.findMin(vector1, vector2);
        this.maxVector = VectorUtils.findMax(vector1, vector2);
        this.owner = owner;
        this.members = new LinkedList<String>();
    }

    public Region(String[] record) {
        name = record[ArrayUtils.indexOf(fields, "name")];
        minVector = VectorUtils.parseVector(record[ArrayUtils.indexOf(fields, "v1")]);
        maxVector = VectorUtils.parseVector(record[ArrayUtils.indexOf(fields, "v2")]);
        warp = LocationUtils.parseLocation(record[ArrayUtils.indexOf(fields, "warp")]);
        if(warp == null) {
            warp = Bukkit.getWorld(UDSPlugin.getUDSConfig().getWorldName()).getSpawnLocation();
        }
        owner = record[ArrayUtils.indexOf(fields, "owner")];
        if(owner.equals("null")) {
            owner = "";
        }
        for(int i = 0; i < flags.length; i++) {
            if(flags[i] != Boolean.parseBoolean(record[ArrayUtils.indexOf(fields, "flags")].split(",")[i])) {
                flags[i] ^= true;
            }
        }
        if(record.length == 7) {                // ** More code to remove once these little issues are resolved.
            members = new LinkedList<String>(Arrays.asList(record[ArrayUtils.indexOf(fields, "members")].split(",")));
        } else {                                // **
            members = new LinkedList<String>(); // **
        }                                       // **
    }

    public String getRecord() {
        String flagList = Boolean.toString(flags[0]);
        for(int i = 1; i < flags.length; i++) {
            flagList = flagList.concat("," + Boolean.toString(flags[i]));
        }
        return name + "\t" +
                VectorUtils.getString(minVector) + "\t" +
                VectorUtils.getString(maxVector) + "\t" +
                LocationUtils.getString(warp) + "\t" +
                owner + "\t" +
                flagList + "\t" +
                StringUtils.join(members.toArray(), ",");
    }

    public final void setWarp(Location warp) {
        this.warp = warp;
    }

    public final Location getWarp() {
        if(warp == null) {
            return null;
        }
        return warp;
    }

    /**
     * Redefine the region with new vectors.
     * @param vector1 Vector 1.
     * @param vector2 Vector 2.
     */
    public final void reset(final Vector vector1, final Vector vector2) {
        this.minVector = VectorUtils.findMin(vector1, vector2);
        this.maxVector = VectorUtils.findMax(vector1, vector2);
    }

    /**
     * Get the container of this region.
     * @return Container of region or <code>null</code>.
     */
    public final String getType() {
        return ("".equals(type) || type == null) ? null : type;
    }

    /**
     * Add a member to the region.
     * @param name New member name.
     * @return <code>true</code> if member was added.
     */
    public final boolean addMember(final String name) {
        return this.members.add(name);
    }

    /**
     * Remove a region member.
     * @param name Member name to remove.
     * @return <code>true</code> if member was removed.
     */
    public final boolean delMember(final String name) {
        return this.members.remove(name);
    }

    /**
     * User requests a region flag toggle.
     * @param flag Flag name.
     * @return New value of region flag.
     * @throws NoFlagException If flag does not exist.
     * @see RegionFlag#flags
     */
    public final boolean toggleFlagUser(final String flag) throws NoFlagException {
        int index = RegionUtils.getFlag(flag.toLowerCase());
        if(index == -1) {
            throw new NoFlagException("Flag " + flag + " does not exist.");
        }
        flags[index] ^= true;
        return flags[index];
    }

    /**
     * Toggle a region flag.
     * @param flag Flag name.
     * @return New value of region flag.
     * @see RegionFlag#flags
     */
    public final boolean toggleFlag(final String flag) {
        int index = RegionUtils.getFlag(flag.toLowerCase());
        flags[index] ^= true;
        return flags[index];
    }

    /**
     * Set a region flag.
     * @param flag Flag name.
     * @see RegionFlag#flags
     */
    public final void setFlag(final String flag, final boolean setting) {
        int index = RegionUtils.getFlag(flag.toLowerCase());
        flags[index] = setting;
    }

    /**
     * User requests to check the value of a region flag.
     * @param flag Flag name.
     * @return Flag value.
     * @throws NoFlagException If flag does not exist.
     * @see RegionFlag#flags
     */
    public final boolean getFlagUser(final String flag) throws NoFlagException {
        int index = RegionUtils.getFlag(flag.toLowerCase());
        if(index == -1) {
            throw new NoFlagException("Flag " + flag + " does not exist.");
        }
        return flags[index];
    }

    /**
     * Check the value of a region flag.
     * @param flag Flag name.
     * @return Flag value.
     * @see RegionFlag#flags
     */
    public final boolean getFlag(final String flag) {
        int index = RegionUtils.getFlag(flag.toLowerCase());
        return flags[index];
    }

    public final void addMinVector(int x, int y, int z) {
        minVector = minVector.add(new Vector(x, y, z));
    }

    public final void addMaxVector(int x, int y, int z) {
        maxVector = maxVector.add(new Vector(x, y, z));
    }

    public static String[] getFields() {return fields;}
    public final void setName(final String name) {this.name = name;}
    public final String getName() {return name;}
    public final World getWorld() {return warp.getWorld();}
    public final Vector getMinVector() {return minVector;}
    public final Vector getMaxVector() {return maxVector;}
    public final void setType(String name) {this.type = name;}
    public final void setOwner(final String owner) {this.owner = owner;}
    public final String getOwner() {return this.owner;}
    public final List<String> getMembers() {return this.members;}
}

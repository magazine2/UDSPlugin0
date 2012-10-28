package com.undeadscythes.udsplugin.utilities;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSString;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * A collection of useful 'checking' tools, generally returning boolean values.
 * @author UndeadScythes
 */
public final class RegionUtils {
    private final transient UDSPlugin plugin;
    public static final String[] flags = new String[]{"fire", "pvp", "protect", "lock", "snow", "mobs", "mushroom", "vine", "safe"};

    public static void remove(String name) {
        UDSPlugin.getRegions().remove(name);
    }

    public static Region get(String name) {
        return UDSPlugin.getRegions().remove(name);
    }

    public static int getFlag(String flag) {
        return ArrayUtils.indexOf(flags, flag);
    }

    public static String getFlag(int index) {
        return flags[index];
    }

    public static int getNoFlags() {
        return flags.length;
    }

    /**
     * Can a player build here?
     * @param location Location.
     * @param player Player.
     * @return
     */
    public static boolean canBuildHere(final Location location, final Player player) {
        final UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(location);
        if(!testRegions.isEmpty()) {
            for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                Region region = i.getValue();
                String owner = region.getOwner();
                List<String> members = region.getMembers();
                String name = player.getName();
                if(owner.equalsIgnoreCase(name)) {
                    return true;
                }
                if(members.contains(name)) {
                    return true;
                }
                if(owner.contains("g:") &&
                   player.hasPermission("udsplugin.group." + owner.replace("g:",""))) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     *
     * @param plugin Instance of the running UDSPlugin.
     */
    public RegionUtils(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Is this location inside this region?
     * @param location Location.
     * @param regionName Name of region.
     * @return
     */
    public static boolean isInRegion(final Location location, final String regionName) {
        final Region region = UDSPlugin.getRegions().get(regionName);
        return region != null
            && isInside(location, region.getMinVector(), region.getMaxVector());
    }

    /**
     * Is this location inside this region?
     * @param location Location.
     * @param region Region.
     * @return
     */
    public static boolean isInRegion(final Location location, final Region region) {
        return isInside(location, region.getMinVector(), region.getMaxVector());
    }

    /**
     * Is this location within the cuboid defined by these two vectors?
     * @param location Location.
     * @param vector1 Minimum vector.
     * @param vector2 Maximum vector.
     * @return
     */
    public static boolean isInside(final Location location, final Vector vector1, final Vector vector2) {
        return location != null
            && !(location.getX() < vector1.getX())
            && !(location.getX() > vector2.getX())
            && !(location.getZ() < vector1.getZ())
            && !(location.getZ() > vector2.getZ())
            && !(location.getY() < vector1.getY())
            && !(location.getY() > vector2.getY());
    }

    /**
     * Does this region have no overlaps with any other region?
     * @param region Region.
     * @return
     */
    public static boolean isInOpenArea(final Region region) {
        for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
            if(hasOverlap(region, i.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Do these regions overlap?
     * @param region1 Region.
     * @param region2 Region.
     * @return
     */
    public static boolean hasOverlap(final Region region1, final Region region2) {
        return !(region1.getMaxVector().getX() < region2.getMinVector().getX()
            || region1.getMinVector().getX() > region2.getMaxVector().getX()
            || region1.getMaxVector().getY() < region2.getMinVector().getY()
            || region1.getMinVector().getY() > region2.getMaxVector().getY()
            || region1.getMaxVector().getZ() < region2.getMinVector().getZ()
            || region1.getMinVector().getZ() > region2.getMaxVector().getZ());
    }

    /**
     * Is this location not contained by any region? NOTE: CALL TO isInside()
     * @param location Location.
     * @return
     */
    public static boolean isInOpenArea(final Location location) {
        for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
            final Region test = i.getValue();
            if(location.getBlockX() < test.getMinVector().getX()) {
                continue;
            }
            if(location.getBlockX() > test.getMaxVector().getX()) {
                continue;
            }
            if(location.getBlockY() < test.getMinVector().getY()) {
                continue;
            }
            if(location.getBlockY() > test.getMaxVector().getY()) {
                continue;
            }
            if(location.getBlockZ() < test.getMinVector().getZ()) {
                continue;
            }
            if(location.getBlockZ() > test.getMaxVector().getZ()) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Is this vector not contained within any region?
     * @param vector Vector.
     * @return
     */
    public static boolean isInOpenArea(final Vector vector) {
        for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
            final Region test = i.getValue();
            if(vector.getX() < test.getMinVector().getX()) {
                continue;
            }
            if(vector.getX() > test.getMaxVector().getX()) {
                continue;
            }
            if(vector.getY() < test.getMinVector().getY()) {
                continue;
            }
            if(vector.getY() > test.getMaxVector().getY()) {
                continue;
            }
            if(vector.getZ() < test.getMinVector().getZ()) {
                continue;
            }
            if(vector.getZ() > test.getMaxVector().getZ()) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Is this location locked by a region?
     * @param location Location.
     * @return
     */
    public static boolean isLocked(final Location location) {
        final UDSHashMap<Region> testRegions = findRegionsHere(location);
        if(!testRegions.isEmpty()) {
            for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                if(i.getValue().getFlag("lock")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Can a player PVP at this location?
     * @param location Location.
     * @return
     */
    public static boolean canPvp(final Location location) {
        UDSHashMap<Region> testRegions1 = findRegionsHere(location);
        if(testRegions1.isEmpty()) {
            return true;
        }
        for(Map.Entry<String, Region> i : testRegions1.entrySet()) {
            if(i.getValue().getFlag("pvp")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is this player a worker in the shop at this location?
     * @param name Player name.
     * @param location Location.
     * @return
     */
    public static boolean isWorkingIn(final String name, final Location location) {
        UDSHashMap<Region> testRegions1 = findRegionsHere(location);
        if(testRegions1.isEmpty()) {
            return false;
        }
        for(Map.Entry<String, Region> i : testRegions1.entrySet()) {
            if(i.getValue().getMembers().contains(name) && i.getValue().getName().endsWith("shop")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is this location contained within a quarry?
     * @param location Location.
     * @param plugin Instance of the running UDSPlugin.
     * @return
     */
    public static boolean isInQuarry(final Location location) {
        final UDSHashMap<Region> testRegions1 = findRegionsHere(location);
        for(Map.Entry<String, Region> i : testRegions1.entrySet()) {
            if(i.getKey().startsWith("quarry:")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSafe(final Location location) {
        final UDSHashMap<Region> testRegions1 = findRegionsHere(location);
        for(Map.Entry<String, Region> i : testRegions1.entrySet()) {
            if(i.getValue().getFlag("safe")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find all regions that contain a location.
     * @param location Location.
     * @return
     */
    public static UDSHashMap<Region> findRegionsHere(final Location location) {
        final UDSHashMap<Region> returnMap = new UDSHashMap<Region>();
        for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
            if(RegionUtils.isInRegion(location, i.getValue())) {
                returnMap.put(i.getKey(), i.getValue());
            }
        }
        return returnMap;
    }

    /**
     * Find all regions that overlap a region.
     * @param region Region.
     * @return
     */
    public static UDSHashMap<Region> findRegionsHere(final Region region) {
        UDSHashMap<Region> returnMap = new UDSHashMap<Region>();
        for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
            Region test = i.getValue();
            if(region.getMaxVector().getX() < test.getMinVector().getX()) {
                continue;
            }
            if(region.getMinVector().getX() > test.getMaxVector().getX()) {
                continue;
            }
            if(region.getMaxVector().getY() < test.getMinVector().getY()) {
                continue;
            }
            if(region.getMinVector().getY() > test.getMaxVector().getY()) {
                continue;
            }
            if(region.getMaxVector().getZ() < test.getMinVector().getZ()) {
                continue;
            }
            if(region.getMinVector().getZ() > test.getMaxVector().getZ()) {
                continue;
            }
            returnMap.put(i.getKey(), i.getValue());
        }
        return returnMap;
    }

    /**
     * Find the volume of a region.
     * @param v1 Vector.
     * @param v2 Vector.
     * @return
     */
    public static int findVolume(final Vector v1, final Vector v2) {
        if(v1 != null && v2 != null) {
            return (int) ((Math.abs(v1.getX() - v2.getX()) + 1) * (Math.abs(v1.getY() - v2.getY()) + 1) * (Math.abs(v1.getZ() - v2.getZ()) + 1));
        }
        return 0;
    }

        /**
     * Find a region by partial name containing a location.
     * @param partial Partial name.
     * @param location Location.
     * @return Closest match region or null.
     */
    public static Region findRegionHere(final String partial, final Location location) {
        for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
            if(RegionUtils.isInRegion(location, i.getValue()) &&
               i.getKey().contains(partial.toLowerCase(Locale.ENGLISH))) {
                return i.getValue();
            }
        }
        return null;
    }

    /**
     * Find a region by partial name.
     * @param partial Partial name.
     * @return Closest match region or null.
     */
    public static Region findRegion(final String partial) {
        for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
            if(i.getKey().contains(partial.toLowerCase(Locale.ENGLISH))) {
                return i.getValue();
            }
        }
        return null;
    }

    /**
     * Find first region by owner.
     * @param name Owner name.
     * @return Region or null.
     */
    public String findRegionByOwner(final String name) {
        for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
            if(i.getValue().getOwner().equalsIgnoreCase(name.toLowerCase(Locale.ENGLISH))) {
                return i.getKey();
            }
        }
        return null;
    }

    /**
     * Find the center of a region.
     * @param region Region.
     * @return
     */
    public static Location findRegionCenter(final Region region) {
        if(region == null) {
            return null;
        }
        final World world = region.getWarp().getWorld();
        double x = (region.getMinVector().getX() + region.getMaxVector().getX()) / 2;
        double y = (region.getMinVector().getY() + region.getMaxVector().getY()) / 2;
        double z = (region.getMinVector().getZ() + region.getMaxVector().getZ()) / 2;
        return new Location(world, x, y, z);
    }

    /**
     * Find the name of the owner of the shop containing this location.
     * @param location Location.
     * @return Region or null.
     */
    public static String findShopOwner(final Location location) {
        UDSHashMap<Region> testRegions1 = findRegionsHere(location);
        if(testRegions1.isEmpty()) {
            return null;
        }
        for(Map.Entry<String, Region> i : testRegions1.entrySet()) {
            if(i.getValue().getName().endsWith("shop")) {
                return i.getValue().getName().replace("shop", "");
            }
        }
        return null;
    }

    public static String regionRename(final String oldName, final String newName) {
        if(!(new UDSString(newName).censor())) {
            if(!UDSPlugin.getRegions().containsKey(newName)) {
                final Region region = UDSPlugin.getRegions().get(oldName);
                if(region != null) {
                    UDSPlugin.getRegions().remove(region.getName());
                    region.setName(newName);
                    UDSPlugin.getRegions().put(newName, region);
                    return Color.MESSAGE + "Region renamed.";
                } return UDSMessage.NO_REGION;
            } else {
                return UDSMessage.BAD_NAME;
            }
        } else {
            return UDSMessage.BAD_STRING;
        }
    }

    public static void streamlineRegions() {

    }
}

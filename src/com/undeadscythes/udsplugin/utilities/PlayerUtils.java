package com.undeadscythes.udsplugin.utilities;

import com.undeadscythes.udsplugin.Direction;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A collection of tools to find various objects within hash maps.
 * @author UndeadScythes
 */
public final class PlayerUtils {
    public static UDSPlayer matchUDS(Player player) {
        return matchUDS(player.getName());
    }
    
    public static String completeName(String partialName) {
        return matchOnlinePlayer(partialName).getName();
    }
    
    public static boolean hasRequest(String playerName) {
        return UDSPlugin.getRequests().containsKey(playerName);
    }
    
    
    public static Player getOnline(String playerName) {
        return Bukkit.getPlayerExact(playerName);
    }
    
    public static Direction getDirection8(Player player) {
        Float yaw = player.getLocation().getYaw() % 360;
        if(yaw < 22.5) {
            return Direction.SOUTH;
        } else if (yaw < 67.5) {
            return Direction.SOUTH_WEST;
        } else if (yaw < 112.5) {
            return Direction.WEST;
        } else if (yaw < 157.5) {
            return Direction.NORTH_WEST;
        } else if (yaw < 202.5) {
            return Direction.NORTH;
        } else if (yaw < 247.5) {
            return Direction.NORTH_EAST;
        } else if (yaw < 292.5) {
            return Direction.EAST;
        } else {
            return Direction.SOUTH_EAST;
        }
    }

    /**
     * Number of players with bounties.
     * @return
     */
    public static int countBounties(final UDSHashMap<UDSPlayer> players) {
        int bounties = 0;
        for(Map.Entry<String, UDSPlayer> i : players.entrySet()) {
            if(i.getValue().hasBounty()) {
                bounties++;
            }
        }
        return bounties;
    }

    /**
     * Number of a particular item in a player's inventory.
     * @param player Player.
     * @param search Item.
     * @return
     */
    public static int countItems(final Player player, final ItemStack search) {
        final ItemStack[] inventory = player.getInventory().getContents();
        int count = 0;
        for(int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if(item != null && item.getType() == search.getType() && item.getData().getData() == search.getData().getData()) {
                count += item.getAmount();
            }
        }
        return count;
    }

    /**
     * Find an online player by either partial MC name or partial nickname.
     * @param partialName Partial name.
     * @param plugin Instance of running UDSPlugin.
     * @return Closest match player or null.
     */
    public static Player matchOnlinePlayer(final String partialName) {
        final Server server = Bukkit.getServer();
        List<Player> players = server.matchPlayer(partialName);
        if(players.isEmpty()) {
            for(Map.Entry<String, UDSPlayer> i : UDSPlugin.getPlayers().entrySet()) {
                if(i.getValue().getNick().toLowerCase(Locale.ENGLISH).contains(partialName.toLowerCase(Locale.ENGLISH))) {
                    players = server.matchPlayer(i.getKey());
                    break;
                }
            }
        }
        return players.isEmpty() ? null : players.get(0);
    }

    public static UDSPlayer getUDS(final Player player) {
        return UDSPlugin.getPlayers().get(player.getName());
    }
    
    public static UDSPlayer getUDSPlayer(String playerName) {
        return UDSPlugin.getPlayers().get(playerName);
    }

    /**
     * Find a server player by either partial MC name or partial nickname.
     * @param partialName Partial name.
     * @param plugin Instance of running UDSPlugin.
     * @return Closest match server player or null.
     */
    public static UDSPlayer matchUDS(final String partialName) {
        Player player = matchOnlinePlayer(partialName);
        if(player != null) {
            return UDSPlugin.getPlayers().get(player.getName());
        }
        for(Map.Entry<String, UDSPlayer> i : UDSPlugin.getPlayers().entrySet()) {
            if(i.getValue().getNick().toLowerCase(Locale.ENGLISH).contains(partialName.toLowerCase(Locale.ENGLISH))) {
                return i.getValue();
            }
            if(i.getKey().toLowerCase(Locale.ENGLISH).contains(partialName.toLowerCase(Locale.ENGLISH))) {
                return i.getValue();
            }
        }
        return null;
    }

    /**
     * Find an offline player on the banned list by either partial MC name or partial nickname.
     * @param partialName Partial name.
     * @return Closest match offline player or null.
     */
    public static OfflinePlayer matchBannedPlayer(final String partialName) {
        UDSPlayer player = matchUDS(partialName);
        if(player != null) {
            for(Iterator<OfflinePlayer> i = Bukkit.getServer().getBannedPlayers().iterator(); i.hasNext();) {
                OfflinePlayer offlinePlayer = i.next();
                if(offlinePlayer.getName().toLowerCase(Locale.ENGLISH).contains(partialName.toLowerCase(Locale.ENGLISH)) ||
                   offlinePlayer.getName().toLowerCase(Locale.ENGLISH).contains(player.getNick().toLowerCase(Locale.ENGLISH))) {
                    return offlinePlayer;
                }
            }
        }
        return null;
    }

    /**
     * Find an offline player by partial MC name.
     * @param partialName Partial name.
     * @return Closest match offline player or null.
     */
    public static OfflinePlayer matchOfflinePlayer(final String partialName) {
        UDSPlayer player = matchUDS(partialName);
        if(player != null) {
            OfflinePlayer[] offlinePlayers = Bukkit.getServer().getOfflinePlayers();
            for(int i = 0; i < offlinePlayers.length; i++) {
                OfflinePlayer offlinePlayer = offlinePlayers[i];
                if(offlinePlayer.getName().toLowerCase(Locale.ENGLISH).contains(partialName.toLowerCase(Locale.ENGLISH)) ||
                   offlinePlayer.getName().toLowerCase(Locale.ENGLISH).contains(player.getNick().toLowerCase(Locale.ENGLISH))) {
                    return offlinePlayer;
                }
            }
        }
        return null;
    }
}

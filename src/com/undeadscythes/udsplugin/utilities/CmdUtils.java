package com.undeadscythes.udsplugin.utilities;

import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.entity.Player;

/**
 * A collection of commands to assist in executing commands.
 * @author Scythe
 */
public class CmdUtils {
    public static boolean isPlayer(Player player, String playerName) {
        if(PlayerUtils.matchUDS(playerName) != null) {
            return true;
        } else {
            player.sendMessage(UDSMessage.NO_PLAYER);
            return false;
        }
    }

    public static boolean isClan(Player player, String clanName) {
        if(ClanUtils.get(clanName) != null) {
            return true;
        } else {
            player.sendMessage(UDSMessage.NO_CLAN);
            return false;
        }
    }

    public static boolean isLeader(Player player) {
        if(inClan(player)) {
            if(player.getName().equals(ClanUtils.get(PlayerUtils.getUDS(player).getClan()).getLeader())) {
                return true;
            } else {
                player.sendMessage(UDSMessage.NOT_CLAN_LEADER);
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean inClan(Player player) {
        if(PlayerUtils.getUDS(player).isInClan()) {
            return true;
        } else {
            player.sendMessage(UDSMessage.NOT_IN_CLAN);
            return false;
        }
    }

    public static boolean isOnline(Player player, String playerName) {
        if(PlayerUtils.matchOnlinePlayer(playerName) != null) {
            return true;
        } else {
            player.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
            return false;
        }
    }

    public static boolean hasRequest(Player player, String playerName) {
        if(isOnline(player, playerName)) {
            if(PlayerUtils.hasRequest(PlayerUtils.completeName(playerName))) {
                return true;
            } else {
                if(player.getName().equals(playerName)) {
                    player.sendMessage(UDSMessage.NO_REQUESTS);
                } else {
                    player.sendMessage(UDSMessage.HAS_REQUEST);
                }
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean perm(Player player, String perm) {
        if(player.hasPermission("udsplugin." + perm)) {
            return true;
        } else {
            player.sendMessage(UDSMessage.NO_PERM);
            return false;
        }
    }

    public static boolean inBase(Player player, String baseName) {
        if(RegionUtils.isInRegion(player.getLocation(), baseName)) {
            return true;
        } else {
            player.sendMessage(UDSMessage.NOT_IN_BASE);
            return false;
        }
    }
}

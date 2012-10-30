package com.undeadscythes.udsplugin.utilities;

import com.undeadscythes.udsplugin.ChatRoom;
import com.undeadscythes.udsplugin.Clan;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
//import org.dynmap.DynmapAPI;

public final class ChatUtils {
    public static void sendClanMessage(final Player player, final String message) {
        final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(player.getName());
        final Clan clan = UDSPlugin.getClans().get(serverPlayer.getClan());
        if(clan != null) {
            for(String i : clan.getMembers()) {
                final Player clanMember = Bukkit.getPlayerExact(i);
                if(clanMember != null) {
                    clanMember.sendMessage(Color.CLAN + serverPlayer.getNick() + ": " + message);
                }
            }
            final Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
            for (int i = 0; i < onlinePlayers.length; i++) {
                if(onlinePlayers[i].hasPermission("udsplugin.spy") && !PlayerUtils.matchUDS(onlinePlayers[i].getName()).getClan().equals(clan.getName())) {
                    onlinePlayers[i].sendMessage(Color.GROUP_ADMIN + "SPY: " + Color.CLAN + serverPlayer.getNick() + ": " + message);
                }
            }
        }
    }

    public static void sendClanBroadcast(final Clan clan, final String message) {
        for(String i : clan.getMembers()) {
            final Player clanMember = Bukkit.getPlayerExact(i);
            if(clanMember != null) {
                clanMember.sendMessage(Color.CLAN + "[" + clan.getName() + "] " + message);
            }
        }
    }

    public static void sendNormMessage(final String playerName, final String message) {
        final Player[] players = Bukkit.getOnlinePlayers();
        for(int i = 0; i < players.length; i++) {
            if(!UDSPlugin.getPlayers().get(players[i].getName()).getIgnores().contains(playerName)) {
                players[i].sendMessage(message);
                //Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
                //DynmapAPI api = (DynmapAPI)dynmap;
                //api.postPlayerMessageToWeb("", "", message);
            }
        }
    }

    public static void sendPrivMessage(final Player player, final String message) {
        for(Map.Entry<String, ChatRoom> i : UDSPlugin.getChatRooms().entrySet()) {
            final String senderName = player.getName();
            final ChatRoom chatRoom = i.getValue();
            if(chatRoom.getMembers().contains(senderName)) {
                for(final Iterator<String> j = chatRoom.getMembers().iterator(); j.hasNext();) {
                    final String mName = j.next();
                    final Player member = Bukkit.getPlayerExact(mName);
                    member.sendMessage(Color.PRIVATE + "[" + chatRoom.getName() + "] " + UDSPlugin.getPlayers().get(senderName).getNick() + ": " + message);
                }
            }
        }
    }

    public static void sendAdminMessage(final Player player, final String message) {
        final Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
        for (int i = 0; i < onlinePlayers.length; i++) {
            if(onlinePlayers[i].hasPermission("udsplugin.a")) {
                onlinePlayers[i].sendMessage(Color.GROUP_ADMIN + "[ADMIN] " + UDSPlugin.getPlayers().get(player.getName()).getNick() + ": " + message);
            }
        }

    }
}

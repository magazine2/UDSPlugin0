package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Warp;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoin implements Listener {
    private final transient UDSPlugin plugin;

    public PlayerJoin(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public final void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        UDSPlayer serverPlayer = serverPlayers.get(playerName);
        if(serverPlayer == null) {
            final Server server = plugin.getServer();
            serverPlayers.put(playerName, new UDSPlayer(playerName));
            server.broadcastMessage(Color.BROADCAST + "A new player, gifts for everyone!");
            final Player[] players = server.getOnlinePlayers();
            for(int i = 0; i < players.length; i++) {
                players[i].getInventory().addItem(ItemUtils.findItem(plugin.getConfig().getString("gift-item")));
            }
            player.getInventory().addItem(new ItemStack(Material.MAP, 1, (short)plugin.getConfig().getInt("map")));
            final Warp warp = UDSPlugin.getWarps().get("spawn");
            if(warp != null) {
                player.teleport(warp.getLocation());
            }
        }
        serverPlayer = serverPlayers.get(playerName);
        serverPlayer.setChatChannel("norm");
        serverPlayer.logOn(System.currentTimeMillis());
        if(UDSPlugin.getServerInLockdown() && !serverPlayer.hasLockdownPass() && !player.hasPermission("udsplugin.pass")) {
            player.kickPlayer(UDSMessage.MSG_LOCKDOWN);
        } else {
            player.sendMessage(plugin.getConfig().getString("welcome.norm"));
            if(player.hasPermission("udsplugin.newbie")) {
                player.sendMessage("Kill monsters or trade with players to earn 3000 credits then type /acceptrules in chat.");
            }
            if(player.hasPermission("udsplugin.welcome.admin")) {
                player.sendMessage(Color.GROUP_ADMIN + plugin.getConfig().getString("welcome.admin"));
            }
            event.setJoinMessage(Color.BROADCAST + serverPlayer.getNick() + ((serverPlayer.isInClan()) ? (" of ") + serverPlayer.getClan() + " clan" : "") + " has joined.");
        }
    }
}

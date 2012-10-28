package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    @EventHandler
    public final void onPlayerQuit(final PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(playerName);
        serverPlayer.setEditPoint1(null);
        serverPlayer.setEditPoint2(null);
        UDSPlugin.getRequests().remove(playerName);
        serverPlayer.setCheckPoint(null);
        serverPlayer.setGodMode(false);
        serverPlayer.setLockdownPass(false);
        serverPlayer.setLastChat("");
        serverPlayer.logOff(System.currentTimeMillis());
        event.setQuitMessage(Color.BROADCAST + serverPlayer.getNick() + ((serverPlayer.isInClan()) ? (" of ") + serverPlayer.getClan() + " clan" : "") + " has left.");
    }
}

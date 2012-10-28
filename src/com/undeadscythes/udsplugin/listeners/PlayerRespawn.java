package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSInventory;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerRespawn implements Listener {
    private final transient UDSPlugin plugin;

    public PlayerRespawn(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public final void onPlayerRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(player.getName());
        Location location = serverPlayer.getHome();
        if(location != null) {
            event.setRespawnLocation(LocationUtils.findSafePlace(location));
        } else {
            if(!event.isBedSpawn()) {
                location = event.getRespawnLocation();
                final UDSConfig config = UDSPlugin.getUDSConfig();
                location.setPitch((float) config.getSpawnPitch());
                location.setYaw((float) config.getSpawnYaw());
            }
        }
        if(serverPlayer.hasChallenge()) {
            serverPlayer.endChallenge();
            if(serverPlayer.wasChallengeDraw()) {
                player.sendMessage(Color.MESSAGE + "The challenge was a draw.");
            }
            else {
                player.sendMessage(Color.MESSAGE + "You lost the challenge.");
            }
            final PlayerInventory inventory = player.getInventory();
            final UDSInventory inventoryContents = UDSPlugin.getInventories().get(player.getName());
            inventory.setContents(inventoryContents.getInventory());
            inventory.setArmorContents(inventoryContents.getArmor());
        }
        if(player.hasPermission("udsplugin.back.ondeath")) {
            player.sendMessage(UDSMessage.MSG_USE_BACK);
        }
    }
}

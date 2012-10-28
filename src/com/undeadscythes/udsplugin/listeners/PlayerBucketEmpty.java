package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class PlayerBucketEmpty implements Listener {
    @EventHandler
    public final void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Location location1 = event.getBlockClicked().getLocation();
        Location location2 = event.getBlockClicked().getRelative(event.getBlockFace()).getLocation();
        if(!RegionUtils.canBuildHere(location1, player)) {
            event.setCancelled(true);
            player.sendMessage(UDSMessage.NO_ACCESS);
        } else if(!RegionUtils.canBuildHere(location2, player)) {
                event.setCancelled(true);
                player.sendMessage(UDSMessage.NO_ACCESS);
        }
    }
}

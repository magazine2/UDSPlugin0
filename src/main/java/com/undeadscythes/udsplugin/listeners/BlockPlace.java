package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
    @EventHandler
    public final void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if(PlayerUtils.getUDS(player).isInPrison()) {
            event.setCancelled(true);
            player.sendMessage(UDSMessage.IN_PRISON);
            return;
        }
        final Location location = event.getBlock().getLocation();
        if(!RegionUtils.canBuildHere(location, player)) {
            event.setCancelled(true);
            player.sendMessage(UDSMessage.CANT_BUILD);
        } else {
            final Block block = event.getBlockAgainst();
            if(block.getType() == Material.SNOW && event.getBlockPlaced().getType() == Material.SNOW) {
                if(event.getBlockReplacedState().getData().getData() == (byte)6) {
                    block.setType(Material.SNOW_BLOCK);
                } else {
                    event.getBlockPlaced().setData((byte)((int)(event.getBlockReplacedState().getData().getData()) + 1));
                }
            }
        }
    }
}

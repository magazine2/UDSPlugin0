package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

public class BlockIgnite implements Listener {
    @EventHandler
    public final void onBlockIgnite(final BlockIgniteEvent event) {
        Location location = event.getBlock().getLocation();
        UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(location);
        for(Map.Entry<String, Region> i : testRegions.entrySet()) {
            if(event.getPlayer() != null && i.getValue().getOwner().equalsIgnoreCase(event.getPlayer().getName())) {
                return;
            }
            if(i.getValue().getFlag("fire")) {
                return;
            }
        }
        if(event.getPlayer() != null) {
            return;
        }
        event.setCancelled(true);
    }
}

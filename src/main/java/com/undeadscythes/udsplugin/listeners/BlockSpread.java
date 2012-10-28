package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpread implements Listener {
    @EventHandler
    public final void onBlockSpread(final BlockSpreadEvent event) {
        final Location location = event.getBlock().getLocation();
        final UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(location);
        for(Map.Entry<String, Region> i : testRegions.entrySet()) {
            if(event.getSource().getType() == Material.ICE) {
                if(!i.getValue().getFlag("snow")) {
                    event.setCancelled(true);
                    return;
                }
            } else if(event.getSource().getType() == Material.FIRE) {
                if(!i.getValue().getFlag("fire")) {
                    event.setCancelled(true);
                    return;
                }
            } else if((event.getSource().getType() == Material.RED_MUSHROOM ||
                       event.getSource().getType() == Material.BROWN_MUSHROOM) &&
                       !i.getValue().getFlag("mushroom")) {
                event.setCancelled(true);
                return;
            }
        }
    }
}

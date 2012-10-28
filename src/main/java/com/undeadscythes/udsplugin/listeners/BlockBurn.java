package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

public class BlockBurn implements Listener {
    @EventHandler
    public final void onBlockBurn(final BlockBurnEvent event) {
        Location location = event.getBlock().getLocation();
        UDSHashMap<Region> testRegions1 = RegionUtils.findRegionsHere(location);
        for(Map.Entry<String, Region> i : testRegions1.entrySet()) {
            if(i.getValue().getFlag("fire")) {
                return;
            }
        }
        event.setCancelled(true);
    }
}

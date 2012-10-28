package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromTo implements Listener {
    @EventHandler
    public final void onBlockFromTo(final BlockFromToEvent event) {
        Location from = event.getBlock().getLocation();
        Location to = event.getToBlock().getLocation();
        UDSHashMap<Region> testRegions1 = RegionUtils.findRegionsHere(to);
        if(!testRegions1.isEmpty()) {
            for(Map.Entry<String, Region> i : testRegions1.entrySet()) {
                UDSHashMap<Region> testRegions2 = RegionUtils.findRegionsHere(from);
                if(!testRegions2.isEmpty()) {
                    for(Map.Entry<String, Region> j : testRegions2.entrySet()) {
                        if(i.getKey().equals(j.getKey())) {
                            return;
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}

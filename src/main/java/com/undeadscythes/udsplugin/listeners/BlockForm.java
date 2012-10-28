package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class BlockForm implements Listener {
    @EventHandler
    public final void onBlockForm(final BlockFormEvent event) {
        if(event.getNewState().getType() == Material.SNOW || event.getNewState().getType() == Material.ICE) {
            Location location = event.getBlock().getLocation();
            UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(location);
            for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                if(!i.getValue().getFlag("snow")) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}

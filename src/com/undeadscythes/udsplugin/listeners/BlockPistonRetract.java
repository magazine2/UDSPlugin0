package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class BlockPistonRetract implements Listener {
    @EventHandler
    public final void onBlockPistonRetract(final BlockPistonRetractEvent event) {
        if(event.isSticky() && event.getRetractLocation().getBlock().getTypeId() != 0) {
            Location piston = event.getBlock().getLocation();
            Location block = event.getRetractLocation();
            UDSHashMap<Region> testRegions1 = RegionUtils.findRegionsHere(block);
            if(!testRegions1.isEmpty()) {
                boolean mixedRegions = false;
                boolean totallyEnclosed = false;
                for(Map.Entry<String, Region> i : testRegions1.entrySet()) {
                    UDSHashMap<Region> testRegions2 = RegionUtils.findRegionsHere(piston);
                    if(!testRegions2.isEmpty()) {
                        for(Map.Entry<String, Region> j : testRegions2.entrySet()) {
                            if(!i.getKey().equals(j.getKey())) {
                                mixedRegions = true;
                            }
                            if(i.getKey().equals(j.getKey())) {
                                totallyEnclosed = true;
                            }
                        }
                    }
                    else {
                        mixedRegions = true;
                    }
                }
                if(mixedRegions && !totallyEnclosed) {
                    if(event.isSticky()) {
                        if(event.getDirection() == BlockFace.DOWN) {
                            event.getBlock().setTypeIdAndData(Material.PISTON_STICKY_BASE.getId(), (byte) 0, true);
                        }
                        if(event.getDirection() == BlockFace.UP) {
                            event.getBlock().setTypeIdAndData(Material.PISTON_STICKY_BASE.getId(), (byte) 1, true);
                        }
                        if(event.getDirection() == BlockFace.NORTH) {
                            event.getBlock().setTypeIdAndData(Material.PISTON_STICKY_BASE.getId(), (byte) 4, true);
                        }
                        if(event.getDirection() == BlockFace.SOUTH) {
                            event.getBlock().setTypeIdAndData(Material.PISTON_STICKY_BASE.getId(), (byte) 5, true);
                        }
                        if(event.getDirection() == BlockFace.WEST) {
                            event.getBlock().setTypeIdAndData(Material.PISTON_STICKY_BASE.getId(), (byte) 3, true);
                        }
                        if(event.getDirection() == BlockFace.EAST) {
                            event.getBlock().setTypeIdAndData(Material.PISTON_STICKY_BASE.getId(), (byte) 2, true);
                        }
                        event.getBlock().getRelative(event.getDirection()).setTypeId(0);
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}


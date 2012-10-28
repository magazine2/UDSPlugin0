package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class BlockPistonExtend implements Listener {
    @EventHandler
    public final void onBlockPistonExtend(final BlockPistonExtendEvent event) {
        List<Block> blocks = event.getBlocks();
        if(blocks.isEmpty()) {
            return;
        }
        Location piston = event.getBlock().getLocation();
        for(Iterator<Block> i = blocks.iterator(); i.hasNext();) {
            UDSHashMap<Region> testRegions1 = RegionUtils.findRegionsHere(i.next().getLocation());
            if(!testRegions1.isEmpty()) {
                boolean mixedRegions = false;
                boolean totallyEnclosed = false;
                for(Map.Entry<String, Region> j : testRegions1.entrySet()) {
                    UDSHashMap<Region> testRegions2 = RegionUtils.findRegionsHere(piston);
                    if(!testRegions2.isEmpty()) {
                        for(Map.Entry<String, Region> k : testRegions2.entrySet()) {
                            if(!j.getKey().equals(k.getKey())) {
                                mixedRegions = true;
                            }
                            if(j.getKey().equals(k.getKey())) {
                                totallyEnclosed = true;
                            }
                        }
                    }
                    else {
                        mixedRegions = true;
                    }
                }
                if(mixedRegions && !totallyEnclosed) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}


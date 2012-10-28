package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysics implements Listener {
    private final transient UDSPlugin plugin;

    public BlockPhysics(final UDSPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    @EventHandler
    public final void onBlockPhysics(final BlockPhysicsEvent event) {
        final Location location = event.getBlock().getLocation();
        final UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(location);
        for(Map.Entry<String, Region> i : testRegions.entrySet()) {
            final boolean a = event.getBlock().getType() == Material.VINE;
            final boolean b = event.getBlock().getRelative(BlockFace.DOWN).getType() == Material.VINE;
            final boolean c = i.getValue().getFlag("vine");
            if (a && b && !c) {
                event.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
            }
        }
        if(RegionUtils.isInQuarry(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}

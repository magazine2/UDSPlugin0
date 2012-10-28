package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Golem;
import org.bukkit.entity.NPC;
import org.bukkit.entity.WaterMob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawn implements Listener {
    @EventHandler
    public final void onCreatureSpawn(final CreatureSpawnEvent event) {
        boolean allowSpawn;
        final Entity entity = event.getEntity();
        if(entity instanceof Animals) {
            allowSpawn = true;
        } else if(entity instanceof WaterMob) {
            allowSpawn = true;
        } else if(entity instanceof NPC) {
            allowSpawn = true;
        } else if(entity instanceof Golem) {
            allowSpawn = true;
        } else {
            allowSpawn = false;
        }
        if(!allowSpawn) {
            final Location location = event.getLocation();
            final UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(location);
            if(testRegions.isEmpty()) {
                allowSpawn = true;
            } else {
                for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                    if(i.getValue().getFlag("mobs")) {
                        allowSpawn = true;
                        break;
                    }
                }
            }
        }
        event.setCancelled(!allowSpawn);
    }
}

package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.utilities.RegionUtils;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlock implements Listener {
    @EventHandler
    public final void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if((event.getEntity() instanceof Enderman || event.getEntity() instanceof Silverfish) && !RegionUtils.isInOpenArea(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}


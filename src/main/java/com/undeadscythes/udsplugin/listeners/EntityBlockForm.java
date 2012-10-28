package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.utilities.RegionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

public class EntityBlockForm implements Listener {
    @EventHandler
    public final void onEntityBlockForm(final EntityBlockFormEvent event) {
        if(event.getEntity() instanceof Snowman) {
            Location location = event.getBlock().getLocation();
            if(!RegionUtils.isInOpenArea(location)) {
                event.setCancelled(true);
            }
        }
    }
}


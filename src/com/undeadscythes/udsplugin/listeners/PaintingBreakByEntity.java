package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.utilities.EntityUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;

public class PaintingBreakByEntity implements Listener {
    @EventHandler
    public final void onPaintingBreakByEntity(final PaintingBreakByEntityEvent event) {
        Location location = event.getPainting().getLocation();
        Entity remover = EntityUtils.getTopLevel(event.getRemover());
        if(remover instanceof Player) {
            event.setCancelled(RegionUtils.canBuildHere(location, (Player)remover));
        } else {
            event.setCancelled(!RegionUtils.isInOpenArea(location));
        }
    }
}

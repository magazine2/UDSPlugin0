package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.utilities.RegionUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

public class EntityInteract implements Listener {
    @EventHandler
    public final void onEntityInteract(final EntityInteractEvent event) {
        final Block block = event.getBlock();
        if(!RegionUtils.isInOpenArea(block.getLocation()) && block.getType() == Material.SOIL) {
            event.setCancelled(true);
        }
    }
}

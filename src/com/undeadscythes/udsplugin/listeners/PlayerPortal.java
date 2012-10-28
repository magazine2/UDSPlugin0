package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PlayerPortal implements Listener {
    @EventHandler
    public final void onPlayerPortal(final PlayerPortalEvent event) {
        Region region = RegionUtils.findRegionHere("portal:", event.getPlayer().getLocation());
        if(region != null) {
            String[] portals = region.getName().replace("portal:", "").split("-");
            String target = "portal:" + portals[1] + "-" + portals[0];
            Location location = RegionUtils.findRegionCenter(RegionUtils.findRegion(target));
            if(location != null) {
                event.setTo(location);
            }
            else {
                event.setTo(event.getFrom());
            }
        }
    }
}

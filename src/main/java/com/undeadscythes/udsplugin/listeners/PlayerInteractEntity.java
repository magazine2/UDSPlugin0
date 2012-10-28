package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntity implements Listener {
    @EventHandler
    public final void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
        if(event.getRightClicked() instanceof Tameable) {
            final Tameable pet = (Tameable) event.getRightClicked();
            if(pet.isTamed()) {
                String ownerName = "";
                if(pet instanceof Wolf) {
                    ownerName = ((Wolf)pet).getOwner().getName();
                }
                if(pet instanceof Ocelot) {
                    ownerName = ((Ocelot)pet).getOwner().getName();
                }
                if(!ownerName.equals(event.getPlayer().getName())) {
                    event.getPlayer().sendMessage(Color.MESSAGE + "This animal belongs to " + PlayerUtils.matchUDS(ownerName).getNick());
                } else if(event.getPlayer().isSneaking()){
                    PlayerUtils.matchUDS(event.getPlayer().getName()).setPetId(event.getRightClicked().getUniqueId());
                    event.getPlayer().sendMessage(Color.MESSAGE + "Pet selected.");
                    event.setCancelled(true);
                }
            }
        }
    }
}

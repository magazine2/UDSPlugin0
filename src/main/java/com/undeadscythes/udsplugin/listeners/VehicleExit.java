package com.undeadscythes.udsplugin.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

public class VehicleExit implements Listener {
    @EventHandler
    public final void onVehicleExit(final VehicleExitEvent event) {
        if(event.getVehicle() instanceof Minecart) {
            if(event.getExited() instanceof Player) {
                Player player = (Player) event.getExited();
                player.getInventory().addItem(new ItemStack(Material.MINECART.getId()));
            }
            event.setCancelled(true);
            event.getVehicle().remove();
        }
    }
}

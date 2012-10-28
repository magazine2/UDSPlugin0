package com.undeadscythes.udsplugin.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

public class VehicleDestroy implements Listener {
    @EventHandler
    public final void onVehicleDestroy(final VehicleDestroyEvent event) {
        final Vehicle vehicle = event.getVehicle();
        if(vehicle instanceof Boat) {
            if(event.getAttacker() != null) {
                vehicle.getWorld().dropItemNaturally(vehicle.getLocation(), new ItemStack(Material.BOAT));
                vehicle.remove();
            }
            event.setCancelled(true);
        }
    }
}

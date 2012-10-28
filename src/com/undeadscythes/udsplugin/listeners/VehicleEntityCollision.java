package com.undeadscythes.udsplugin.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

public class VehicleEntityCollision implements Listener {
    @EventHandler
    public final void onVehicleEntityCollision(final VehicleEntityCollisionEvent event) {
        final Vehicle vehicle = event.getVehicle();
        if(vehicle instanceof Minecart) {
            final Minecart cart = (Minecart)vehicle;
            if(cart.getPassenger() != null) {
                final Entity collider = event.getEntity();
                if(collider instanceof LivingEntity) {
                    final LivingEntity victim = (LivingEntity)collider;
                    if(!(victim instanceof Player)) {
                        victim.remove();
                        event.setCancelled(true);
                        event.setCollisionCancelled(true);
                        event.setPickupCancelled(true);
                    }
                }
            }
        }
    }
}

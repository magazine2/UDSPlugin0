package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamage implements Listener {
    @EventHandler
    public final void onEntityDamage(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if(entity instanceof Player) {
            final Player player = (Player) entity;
            final String playerName = player.getName();
            if(UDSPlugin.getPlayers().get(playerName).hasGodMode()) {
                event.setCancelled(true);
            }
            if(player.getInventory().getHelmet() != null &&
            player.getInventory().getHelmet().getType() == Material.GLASS &&
            event.getCause() == DamageCause.DROWNING) {
                event.setCancelled(true);
            }
        }
    }
}

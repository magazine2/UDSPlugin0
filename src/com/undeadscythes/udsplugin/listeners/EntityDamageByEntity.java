package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.EntityUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {
    private final transient UDSPlugin plugin;

    public EntityDamageByEntity(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public final void onEntityDamage(final EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity();
        Entity damager = EntityUtils.getTopLevel(event.getDamager());
        if(damager instanceof Player) {
            if(entity instanceof Player) {
                event.setCancelled(pvp((Player)damager, (Player)entity));
            } else {
                event.setCancelled(pve((Player)damager, entity));
            }
        }
    }

    public boolean pvp(Player attacker, Player defender) {
        final UDSHashMap<UDSPlayer> udsPlayers = UDSPlugin.getPlayers();
        final UDSPlayer udsDefender = udsPlayers.get(defender.getName());
        final UDSPlayer udsAttacker = udsPlayers.get(attacker.getName());
        if(udsDefender.hasChallenge() && udsAttacker.hasChallenge()) {
            return false;
        } else if(!RegionUtils.canPvp(defender.getLocation()) || !RegionUtils.canPvp(attacker.getLocation())) {
            attacker.sendMessage(UDSMessage.NO_PVP);
            return true;
        } else if(!udsDefender.isInClan()) {
            attacker.sendMessage(UDSMessage.PVP_PLAYER_NO_CLAN);
            return true;
        } else if(!udsAttacker.isInClan()) {
            attacker.sendMessage(UDSMessage.PVP_NO_CLAN);
        } else if(udsDefender.getClan().equals(udsAttacker.getClan())) {
            return true;
        }
        udsAttacker.newAttack(System.currentTimeMillis());
        return false;
    }

    public boolean pve(Player attacker, Entity defender) {
        if(defender instanceof Ocelot) {
            final Ocelot ocelot = (Ocelot)defender;
            if(ocelot.isTamed() && ocelot.getTarget().getUniqueId() != attacker.getUniqueId()) {
                return true;
            }
        } else if(defender instanceof Wolf) {
            final Wolf wolf = (Wolf)defender;
            if(wolf.isTamed() && wolf.getTarget().getUniqueId() != attacker.getUniqueId()) {
                return true;
            }
        } else if((defender instanceof Animals || defender instanceof Villager || defender instanceof Golem) && RegionUtils.isSafe(defender.getLocation())) {
            return true;
        }
        return false;
    }
}

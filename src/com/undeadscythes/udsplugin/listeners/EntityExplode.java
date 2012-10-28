package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {
    private final transient UDSPlugin plugin;

    public EntityExplode(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public final void onEntityExplode(final EntityExplodeEvent event) {
        final Entity entity = event.getEntity();
        if(entity instanceof Creeper) {
            if(plugin.getConfig().getBoolean("block.creeper")) {
                event.blockList().clear();
            }
            final Creeper creeper = (Creeper)entity;
            if(creeper.getHealth() < creeper.getMaxHealth() / 2) {
                final EntityDamageEvent damageEvent = creeper.getLastDamageCause();
                if(damageEvent instanceof EntityDamageByEntityEvent) {
                    Entity killer = ((EntityDamageByEntityEvent) damageEvent).getDamager();
                    if(killer instanceof Player || killer instanceof Arrow) {
                        Player player;
                        if(killer instanceof Arrow) {
                            killer = ((Arrow) killer).getShooter();
                        }
                        if(killer instanceof Player) {
                            player = (Player) killer;
                            final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(player.getName());
                            if(player.getGameMode() == GameMode.SURVIVAL && !serverPlayer.hasGodMode()) {
                                final Random generator = new Random();
                                final UDSConfig udsConfig = UDSPlugin.getUDSConfig();
                                serverPlayer.newMobKill(System.currentTimeMillis());
                                final int reward = (int) (plugin.getConfig().getInt("reward.creeper") * serverPlayer.getRewardRatio() * generator.nextDouble());
                                serverPlayer.addMoney(reward);
                                if(reward == 1) {
                                    player.sendMessage(Color.MESSAGE + "You picked up " + reward + " " + udsConfig.getCurrency() + ".");
                                } else if(reward > 0) {
                                    player.sendMessage(Color.MESSAGE + "You picked up " + reward + " " + udsConfig.getCurrencies() + ".");
                                }
                            }
                        }
                    }
                }
            }
        } else if(entity instanceof Fireball) {
            if(plugin.getConfig().getBoolean("block.ghast")) {
                event.blockList().clear();
            }
        } else if(entity instanceof TNTPrimed && plugin.getConfig().getBoolean("block.tnt")) {
            final List<Block> blocks = event.blockList();
            for(final Iterator<Block> i = blocks.iterator(); i.hasNext();) {
                if(i.next().getType() != Material.TNT) {
                    i.remove();
                }
            }
        }
    }
}

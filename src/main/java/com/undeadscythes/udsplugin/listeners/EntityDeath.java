package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.utilities.EntityUtils;
import java.util.Locale;
import java.util.Random;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {
    private final transient UDSPlugin plugin;
    private static final transient int CHARS_TO_TRIM = 35;

    public EntityDeath(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public final void onEntityDeath(final EntityDeathEvent event) {
        final Entity victim = event.getEntity();
        final EntityDamageEvent damageEvent = victim.getLastDamageCause();
        if(damageEvent instanceof EntityDamageByEntityEvent) {
            final Entity killer = EntityUtils.getTopLevel(((EntityDamageByEntityEvent) damageEvent).getDamager());
            if(killer instanceof Player) {
                if(killer instanceof Player) {
                    payReward((Player)killer, victim);
                }
            } else if(killer instanceof Tameable) {
                final Tameable pet = (Tameable) killer;
                if(pet.isTamed()) {
                    payReward((Player)pet.getOwner(), victim);
                }
            }
        }
    }

    private void payReward(final Player player, final Entity victim) {
        final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(player.getName());
        if(player.getGameMode() == GameMode.SURVIVAL && !serverPlayer.hasGodMode()) {
            final Random generator = new Random();
            final FileConfiguration config = plugin.getConfig();
            serverPlayer.newMobKill(System.currentTimeMillis());
            final int reward = (int) (config.getDouble("reward." + victim.getClass().getName().substring(CHARS_TO_TRIM).toLowerCase(Locale.ENGLISH)) * serverPlayer.getRewardRatio() * generator.nextDouble());
            serverPlayer.addMoney(reward);
            if(reward == 1) {
                player.sendMessage(Color.MESSAGE + "You picked up " + reward + " " + config.getString("currency.singular") + ".");
            } else if(reward > 0) {
                player.sendMessage(Color.MESSAGE + "You picked up " + reward + " " + config.getString("currency.plural") + ".");
            }
        }
    }
}

package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Clan;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSInventory;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    private final transient UDSPlugin plugin;

    public PlayerDeath(final UDSPlugin pluginInstance) {
        this.plugin = pluginInstance;
    }

    @EventHandler
    public final void onPlayerDeath(final PlayerDeathEvent event) {
        Player victim = event.getEntity();
        String victimName = victim.getName();
        UDSPlayer udsVictim = UDSPlugin.getPlayers().get(victimName);
        if(udsVictim.hasNick()) {
            event.setDeathMessage(event.getDeathMessage().replace(victimName, udsVictim.getNick()));
        }
        if(victim.hasPermission("udsplugin.back.ondeath")) {
            udsVictim.setBackPoint(victim.getLocation());
        }
        Player killer = victim.getKiller();
        if(killer != null) {
            String killerName = killer.getName();
            if(udsVictim.hasChallenge()) {
                if(udsVictim.getChallenger().equals(killerName)) {
                    event.getDrops().clear();
                    event.setDroppedExp(0);
                    event.setKeepLevel(true);
                    challengeWin(killer, victim);
                } else {
                    challengeDraw(victim);
                }
            } else {
                pvp(killer, victim);
            }
        }
    }

    public void challengeWin(Player killer, Player victim) {
        UDSPlayer udsKiller = UDSPlugin.getPlayers().get(killer.getName());
        UDSPlayer udsVictim = UDSPlugin.getPlayers().get(victim.getName());
        UDSPlugin.getInventories().put(victim.getName(), new UDSInventory(victim.getInventory().getContents(), victim.getInventory().getArmorContents()));
        udsKiller.addMoney(2 * udsVictim.getWager());
        udsKiller.endChallenge();
        killer.sendMessage(Color.MESSAGE + "You won the challenge.");
        udsVictim.setChallengeWasDraw(false);
    }

    public void challengeDraw(Player victim) {
        UDSPlayer udsVictim = UDSPlugin.getPlayers().get(victim.getName());
        UDSPlayer challenger = UDSPlugin.getPlayers().get(udsVictim.getChallenger());
        challenger.addMoney(udsVictim.getWager());
        udsVictim.addMoney(udsVictim.getWager());
        challenger.endChallenge();
        Bukkit.getPlayerExact(challenger.getName()).sendMessage(Color.MESSAGE + "The challenge was a draw.");
        udsVictim.setChallengeWasDraw(true);
    }

    public void clanKill(Player killer, Player victim) {
        UDSHashMap<Clan> clans = UDSPlugin.getClans();
        Clan victimClan = clans.get(UDSPlugin.getPlayers().get(victim.getName()).getClan());
        Clan killerClan = clans.get(UDSPlugin.getPlayers().get(killer.getName()).getClan());
        if(!killerClan.getName().equals(victimClan.getName())) {
            killerClan.addKill();
        }
        victimClan.addDeath();
    }

    public void pvp(Player killer, Player victim) {
        UDSPlayer udsKiller = UDSPlugin.getPlayers().get(killer.getName());
        UDSPlayer udsVictim = UDSPlugin.getPlayers().get(victim.getName());
        if(udsVictim.getBounty() > 0) {
                    bountyKill(killer, victim);
        }
        if(udsKiller.isInClan() && udsVictim.isInClan()) {
            clanKill(killer, victim);
        }
    }

    public void bountyKill(Player killer, Player victim) {
        UDSPlayer udsKiller = UDSPlugin.getPlayers().get(killer.getName());
        UDSPlayer udsVictim = UDSPlugin.getPlayers().get(victim.getName());
        long total = udsVictim.getBounty();
        Bukkit.getServer().broadcastMessage(Color.BROADCAST + udsKiller.getNick() + " collected the " + total + " " + plugin.getConfig().getString("currency.singular") + " bounty on " + udsVictim.getNick() + ".");
        udsKiller.addMoney(total);
        udsVictim.claimReward();
    }
}

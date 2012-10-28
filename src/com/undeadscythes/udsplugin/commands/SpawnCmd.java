package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public SpawnCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);
        if(sender.hasPermission("udsplugin.spawn")) {
            final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
            final UDSPlayer senderPlayer = serverPlayers.get(senderName);
            if(senderPlayer.isInPrison()) {
                sender.sendMessage(UDSMessage.IN_PRISON);
                return true;
            }
            if(senderPlayer.getLastAttack() + plugin.getConfig().getLong("range.pvptp") < System.currentTimeMillis()) {
                teleportToSpawn(sender);
            } else {
                sender.sendMessage(UDSMessage.TP_WAIT);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }

    public void teleportToSpawn(Player player) {
        final UDSConfig config = UDSPlugin.getUDSConfig();
        final World world = Bukkit.getWorld(config.getWorldName());
        final Location location = world.getSpawnLocation();
        if(location != null) {
            location.setPitch((float) config.getSpawnPitch());
            location.setYaw((float) config.getSpawnYaw());
            player.teleport(LocationUtils.findSafePlace(location));
        } else {
            player.sendMessage(UDSMessage.NO_SPAWN);
        }
    }
}

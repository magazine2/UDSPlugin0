package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public BackCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        if(sender.hasPermission("udsplugin.back")) {
            final UDSPlayer senderPlayer = UDSPlugin.getPlayers().get(senderName);
            if(senderPlayer.getBackPoint() != null) {
                final Location location = senderPlayer.getBackPoint();
                location.getWorld().getChunkAt(location).load();
                sender.teleport(LocationUtils.findSafePlace(location));
            } else {
                sender.sendMessage(UDSMessage.NO_TP);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

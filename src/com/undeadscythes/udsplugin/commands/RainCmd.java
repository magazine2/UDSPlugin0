package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RainCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public RainCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.rain")) {
            sender.getWorld().setStorm(true);
            server.broadcastMessage(Color.BROADCAST + senderName + " summoned forth rain.");
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayCmd implements CommandExecutor {
    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);
        if(sender.hasPermission("udsplugin.day")) {
            sender.getWorld().setTime(0);
            server.broadcastMessage(Color.BROADCAST + UDSPlugin.getPlayers().get(senderName).getNick() + " summoned the sun.");
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

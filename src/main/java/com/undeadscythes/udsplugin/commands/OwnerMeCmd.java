package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class OwnerMeCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public OwnerMeCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ConsoleCommandSender console = server.getConsoleSender();
        ChatColor colorMessage = Color.MESSAGE;

        if(commandName.equalsIgnoreCase("ownerme")) {
            if(sender.hasPermission("udsplugin.ownerme") || senderName.equalsIgnoreCase(plugin.getConfig().getString("server-owner"))) {
                server.dispatchCommand(console, "permissions player setgroup " + senderName + " owner");
                sender.sendMessage(colorMessage + "Welcome back to owner " + senderName + ".");
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

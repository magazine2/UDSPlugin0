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

public class AdminMeCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public AdminMeCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        ChatColor colorMessage = Color.MESSAGE;

        if(commandName.equalsIgnoreCase("adminme")) {
            if(sender.hasPermission("udsplugin.adminme")) {
                server.dispatchCommand(console, "permissions player setgroup " + senderName + " admin");
                sender.sendMessage(colorMessage + "Welcome back to admin " + senderName + ".");
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

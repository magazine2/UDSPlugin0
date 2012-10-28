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

public class SetPermCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public SetPermCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        ChatColor messageColor = Color.MESSAGE;

        if(commandName.equalsIgnoreCase("setperm")) {
            if(sender.hasPermission("udsplugin.setperm")) {
                if(args.length == 3) {
                    server.dispatchCommand(console, "permissions group setperm " + args[0] + " " + args[1] + " " + args[2]);
                    sender.sendMessage(messageColor + "Group " + args[0] + " has permission " + args[1] + " set to " + args[2] + ".");
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

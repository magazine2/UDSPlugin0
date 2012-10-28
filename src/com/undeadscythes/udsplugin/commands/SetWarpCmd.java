package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Warp;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public SetWarpCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ChatColor colorError = Color.ERROR;
        ChatColor colorMessage = Color.MESSAGE;

        if(commandName.equalsIgnoreCase("setwarp")) {
            if(sender.hasPermission("udsplugin.setwarp")) {
                if(args.length == 1 || args.length == 2) {
                    if(UDSPlugin.getWarps().get(args[0]) == null) {
                        String rank = "default";
                        if(args.length == 2) {
                            rank = args[1];
                        }
                        if("jailin".equals(args[0]) || "jailout".equals(args[0])) {
                            rank = "jail";
                        }
                        if("chal1".equals(args[0])) {
                            rank = "chal";
                        }
                        if("chal2".equals(args[0])) {
                            rank = "chal";
                        }
                        if("spawn".equals(args[0])) {
                            rank = "spawn";
                        }
                        UDSPlugin.getWarps().put(args[0], new Warp(rank, sender.getLocation(), args[0]));
                        if(rank.equals("jail")) {
                            sender.sendMessage(colorMessage + "Jail point set.");
                        }
                        else if(rank.equals("chal")) {
                            sender.sendMessage(colorMessage + "Duel point set.");
                        }
                        else {
                            sender.sendMessage(colorMessage + "Warp added.");
                        }
                    } else {
                        sender.sendMessage(colorError + "Warp already exists.");
                    }
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

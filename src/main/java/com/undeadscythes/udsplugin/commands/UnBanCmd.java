package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnBanCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public UnBanCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ChatColor broadcastColor = Color.BROADCAST;

        if(commandName.equalsIgnoreCase("unban")) {
            if(sender.hasPermission("udsplugin.unban")) {
                if(args.length == 1) {
                    OfflinePlayer target = PlayerUtils.matchBannedPlayer(args[0]);
                    if(target != null) {
                        target.setBanned(false);
                        server.broadcastMessage(broadcastColor + target.getName() + " has served their time in banishment.");
                    } else {
                        sender.sendMessage(UDSMessage.NO_PLAYER);
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

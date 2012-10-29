package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WardenCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public WardenCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String logMessage = commandSender.getName() + " used the command /" + command.getName() + " " + StringUtils.join(args, " ");

        if(commandSender instanceof Player && ((Player)commandSender).hasPermission("udsplugin.warden")) {
            final UDSPlayer target = PlayerUtils.matchUDS(args[0]);
            if(target != null) {
                final Server server = Bukkit.getServer();
                final String targetName = target.getName();
                final ChatColor messageColor = Color.MESSAGE;
                final String targetNick = PlayerUtils.matchUDS(targetName).getNick();
                server.dispatchCommand(Bukkit.getConsoleSender(), "permissions player setgroup " + targetName + " warden");
                ((Player)commandSender).sendMessage(messageColor + "Player " + targetNick + " has been promoted.");
                final Player player = Bukkit.getPlayerExact(targetName);
                if(player != null) {
                    player.sendMessage(messageColor + "You are now a trusted warden, keep the peace.");
                }
                server.broadcastMessage(Color.BROADCAST + targetNick + " is now a trusted warden, justice will prevail.");
            } else {
                commandSender.sendMessage(UDSMessage.NO_PLAYER);
            }
        } else {
            commandSender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

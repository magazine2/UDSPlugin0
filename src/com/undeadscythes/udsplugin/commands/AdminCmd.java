package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public AdminCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);
        if(sender.hasPermission("udsplugin.admin")) {
            final UDSPlayer target = PlayerUtils.matchUDS(args[0]);
            if(target != null) {
                final ChatColor colorMessage = Color.MESSAGE;
                final String targetName = target.getNick();
                server.dispatchCommand(Bukkit.getConsoleSender(), "permissions player setgroup " + target.getName() + " admin");
                sender.sendMessage(colorMessage + "Player " + targetName + " has been promoted.");
                final Player player = Bukkit.getPlayerExact(targetName);
                if(player != null) {
                    player.sendMessage(colorMessage + "You are now an administrator, welcome to the elite.");
                }
                server.broadcastMessage(Color.BROADCAST + targetName + " is now an administrator, build them a shrine.");
            } else {
                sender.sendMessage(UDSMessage.NO_PLAYER);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

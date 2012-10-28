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

public class MemberCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public MemberCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.member")) {
            final UDSPlayer target = PlayerUtils.matchUDS(args[0]);
            if(target != null) {
                final ChatColor colorMessage = Color.MESSAGE;
                final String tName = target.getName();
                server.dispatchCommand(Bukkit.getConsoleSender(), "permissions player setgroup " + tName + " member");
                sender.sendMessage(colorMessage + "Player " + tName + " has been promoted.");
                final Player player = PlayerUtils.matchOnlinePlayer(args[0]);
                if(player != null) {
                    player.sendMessage(colorMessage + "You have been promoted, play well.");
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PLAYER);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

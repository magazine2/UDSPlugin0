package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GodCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public GodCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.god")) {
            if(args.length == 0) {
                final UDSPlayer senderPlayer = UDSPlugin.getPlayers().get(senderName);
                if(senderPlayer.hasGodMode()) {
                    senderPlayer.setGodMode(false);
                    sender.sendMessage(Color.MESSAGE + "You no longer have god mode.");
                } else {
                    senderPlayer.setGodMode(true);
                    sender.sendMessage(Color.MESSAGE + "You now have god mode.");
                }
            } else if(args.length == 1) {
                final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                if(target != null) {
                    final String tName = target.getName();
                    final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(tName);
                    if(serverPlayer.hasGodMode()) {
                        final ChatColor messageColor = Color.MESSAGE;
                        serverPlayer.setGodMode(false);
                        target.sendMessage(messageColor + "You no longer have god mode.");
                        sender.sendMessage(messageColor + "God mode removed from " + tName);
                    } else {
                        final ChatColor messageColor = Color.MESSAGE;
                        serverPlayer.setGodMode(true);
                        target.sendMessage(messageColor + "You now have god mode.");
                        sender.sendMessage(messageColor + "God mode given to " + tName);
                    }
                } else {
                    sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                }
            } else {
                sender.sendMessage(UDSMessage.BAD_ARGS);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

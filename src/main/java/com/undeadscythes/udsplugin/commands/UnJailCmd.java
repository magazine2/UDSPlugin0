package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Warp;
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

public class UnJailCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public UnJailCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.unjail")) {
            final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
            if(target != null) {
                final String targetName = target.getName();
                final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(targetName);
                if(serverPlayer.getPrisonTime() != 0) {
                    serverPlayer.release();
                    final Warp warp = UDSPlugin.getWarps().get("jailout");
                    if(warp != null) {
                        final ChatColor colorMessage = Color.MESSAGE;
                        target.teleport(warp.getLocation());
                        target.sendMessage(colorMessage + "You have been released. Tread carefully.");
                        sender.sendMessage(colorMessage + targetName + " has been released.");
                        serverPlayer.setGodMode(false);
                    } else {
                        sender.sendMessage(Color.ERROR + "Jail warp not found, use /setwarp jailout to set one.");
                    }
                } else {
                    sender.sendMessage(Color.ERROR + "Player is not in jail.");
                }
            } else {
                sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

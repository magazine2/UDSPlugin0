package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public StatsCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.stats")) {
            if(args.length == 1) {
                final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(args[0]);
                if(serverPlayer != null) {
                    sender.sendMessage(Color.MESSAGE + serverPlayer.getName() + "'s stats:");
                    sender.sendMessage(Color.COMMAND + "Nickname - " + Color.TEXT + serverPlayer.getNick());
                    if(Bukkit.getPlayerExact(serverPlayer.getName()) != null) {
                        sender.sendMessage(Color.COMMAND + "Last seen - " + Color.TEXT + "Online now.");
                    } else {
                        sender.sendMessage(Color.COMMAND + "Last seen - " + Color.TEXT + TimeUtils.findTime(System.currentTimeMillis() - serverPlayer.getLastLogOff()) + " ago.");
                    }
                    sender.sendMessage(Color.COMMAND + "Time online - " + Color.TEXT + TimeUtils.findTime(serverPlayer.getTimeLogged()));
                } else {
                    sender.sendMessage(UDSMessage.NO_PLAYER);
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

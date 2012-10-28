package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Request;
import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChallengeCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public ChallengeCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        if(sender.hasPermission("udsplugin.challenge")) {
            if(args.length == 2) {
                final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                if(target != null) {
                    final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
                    final String targetName = target.getName();
                    final UDSPlayer serverPlayer = serverPlayers.get(targetName);
                    if(!serverPlayer.hasChallenge()) {
                        if(!UDSPlugin.getRequests().containsKey(targetName)) {
                            final UDSConfig config = UDSPlugin.getUDSConfig();
                            final int wager = Integer.parseInt(args[1]);
                            target.sendMessage(Color.MESSAGE + serverPlayers.get(senderName).getNick() + " has challenged you to a duel for " + wager + " " + config.getCurrencies() + ".");
                            target.sendMessage(UDSMessage.MSG_REQUEST);
                            UDSPlugin.getRequests().put(targetName, new Request(targetName, senderName, Request.Type.CHALLENGE, args[1], config.getRequestTimeout()));
                            sender.sendMessage(Color.MESSAGE + "Challenge was sent.");
                        } else {
                            sender.sendMessage(UDSMessage.HAS_REQUEST);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.HAS_CHALLENGE);
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

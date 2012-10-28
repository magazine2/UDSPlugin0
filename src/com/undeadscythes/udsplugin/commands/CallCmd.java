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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CallCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public CallCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        if(sender.hasPermission("udsplugin.call")) {
            final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
            final UDSPlayer senderPlayer = serverPlayers.get(senderName);
            final UDSConfig config = UDSPlugin.getUDSConfig();
            if(senderPlayer.getLastAttack() + config.getPVPTimer() < System.currentTimeMillis()) {
                if(args.length == 1) {
                    final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                    if(target != null) {
                        final String targetName = target.getName();
                        final UDSPlayer targetPlayer = serverPlayers.get(targetName);
                        if(!UDSPlugin.getRequests().containsKey(targetName)) {
                            if(!targetPlayer.isInPrison()) {
                                if(!senderPlayer.isInPrison()) {
                                    final ChatColor colorMessage = Color.MESSAGE;
                                    UDSPlugin.getRequests().put(targetName, new Request(targetName, senderName, Request.Type.TP, "", config.getRequestTimeout()));
                                    target.sendMessage(colorMessage + senderName + " wants to teleport to you.");
                                    target.sendMessage(UDSMessage.MSG_REQUEST);
                                    sender.sendMessage(colorMessage + "Your request has been sent to " + targetPlayer.getNick());
                                } else {
                                    sender.sendMessage(UDSMessage.ERR_PRISONER);
                                }
                            } else {
                                sender.sendMessage(UDSMessage.IN_PRISON);
                            }
                        } else {
                            sender.sendMessage(Color.ERROR + targetPlayer.getNick() + " already has a pending request.");
                        }
                    } else {
                        sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                    }
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                sender.sendMessage(UDSMessage.TP_WAIT);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

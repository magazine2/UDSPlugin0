package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public RCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.r")) {
            final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
            final UDSPlayer serverPlayer = serverPlayers.get(senderName);
            final Player target = Bukkit.getPlayerExact(serverPlayer.getLastChat());
            if(target != null) {
                final String targetName = target.getName();
                final Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
                final String message = Color.WHISPER + serverPlayer.getNick() + " > " + PlayerUtils.matchUDS(target.getName()).getNick() + ": " + StringUtils.join(args, " ");
                target.sendMessage(message);
                sender.sendMessage(message);
                serverPlayer.setLastChat(targetName);
                serverPlayers.get(targetName).setLastChat(senderName);
                for (int i = 0; i < onlinePlayers.length; i++) {
                    if(onlinePlayers[i].hasPermission("udsplugin.spy") && target != onlinePlayers[i] && sender != onlinePlayers[i]) {
                        onlinePlayers[i].sendMessage(message);
                    }
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PLAYER);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_ARGS);
        }
        return true;
    }
}

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public CCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        if(sender.hasPermission("udsplugin.c")) {
            final UDSPlayer senderPlayer = UDSPlugin.getPlayers().get(senderName);
            if(senderPlayer.isInClan()) {
                final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(senderName);
                final String channel = serverPlayer.getChatChannel();
                if(!"clan".equals(channel)) {
                    serverPlayer.setChatChannel("clan");
                    sender.sendMessage(UDSMessage.CHAT_CLAN);
                } else {
                    UDSPlugin.getPlayers().get(senderName).setChatChannel("norm");
                    sender.sendMessage(UDSMessage.CHAT_NORM);
                }
            } else {
                sender.sendMessage(UDSMessage.NOT_IN_CLAN);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ACmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public ACmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        if(sender.hasPermission("udsplugin.a")) {
            final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(senderName);
            final String channel = serverPlayer.getChatChannel();
            if(!"admn".equals(channel)) {
                serverPlayer.setChatChannel("admn");
                sender.sendMessage(UDSMessage.CHAT_ADMN);
            } else {
                UDSPlugin.getPlayers().get(senderName).setChatChannel("norm");
                sender.sendMessage(UDSMessage.CHAT_NORM);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

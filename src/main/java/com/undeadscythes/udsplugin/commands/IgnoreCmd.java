package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public IgnoreCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.ignore")) {
            final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
            if(target != null) {
                final String targetName = target.getName();
                final String targetNick = UDSPlugin.getPlayers().get(targetName).getNick();
                final UDSPlayer player = UDSPlugin.getPlayers().get(senderName);
                if(player.getIgnores().contains(targetName)) {
                    player.unIgnore(targetName);
                    sender.sendMessage(Color.MESSAGE + "You are no longer ignoring " + targetNick + ".");
                } else {
                    if(!target.hasPermission("udsplugin.ignore.exempt")) {
                        player.ignore(targetName);
                        sender.sendMessage(Color.MESSAGE + "You are now ignoring " + targetNick + ".");
                    } else {
                        sender.sendMessage(Color.ERROR + "You cannot ignore this player.");
                    }
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

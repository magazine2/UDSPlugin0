package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class XPCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public XPCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        if(sender.hasPermission("udsplugin.xp")) {
            if(args.length == 2) {
                final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                if(target != null) {
                    if(args[1].matches("[0-9][0-9]*")) {
                        target.setLevel(target.getLevel() + Integer.parseInt(args[1]));
                    } else {
                        sender.sendMessage(UDSMessage.NO_NUMBER);
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

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public KickCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.kick")) {
            if(args.length > 0) {
                final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                if(target != null) {
                    if(args.length == 1) {
                        target.kickPlayer("You have been kicked for breaking the rules.");
                        plugin.getServer().broadcastMessage(Color.BROADCAST + target.getName() + " has been kicked for breaking the rules.");
                    } else {
                        String[] kickMessage = args;
                        kickMessage[0] = "";
                        target.kickPlayer("You have been kicked for " + StringUtils.join(kickMessage, " "));
                        plugin.getServer().broadcastMessage(Color.BROADCAST + target.getName() + " has been kicked for " + StringUtils.join(kickMessage, " "));
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

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public BanCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);
        if(sender.hasPermission("udsplugin.ban")) {
            if(args.length == 1) {
                final UDSPlayer target = PlayerUtils.matchUDS(args[0]);
                if(target != null) {
                    final Player player = Bukkit.getPlayerExact(target.getName());
                    if(player != null) {
                        final Location location = player.getLocation();
                        location.getWorld().strikeLightningEffect(location);
                        player.kickPlayer("You have been banned for breaking the rules.");
                        player.setBanned(true);
                    } else {
                        PlayerUtils.matchOfflinePlayer(args[0]).setBanned(true);
                    }
                    server.broadcastMessage(Color.BROADCAST + target.getNick() + " has been banned for breaking the rules.");
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

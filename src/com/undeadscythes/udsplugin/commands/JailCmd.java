package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Warp;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JailCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public JailCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.jail")) {
            if(args.length == 2 || args.length == 3) {
                    final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                    if(target != null) {
                        final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(target.getName());
                        if(!serverPlayer.isInPrison()) {
                            final Warp warp = UDSPlugin.getWarps().get("jailin");
                            if(warp != null) {
                                if(args[1].matches("[0-9][0-9]*")) {
                                    if(args.length == 3) {
                                        if(args[2].matches("[0-9][0-9]*")) {
                                            jailPlayer(target, Long.parseLong(args[1]), Long.parseLong(args[2]), warp, serverPlayer, server);
                                        } else {
                                        sender.sendMessage(UDSMessage.NO_NUMBER);
                                    }
                                    } else {
                                        jailPlayer(target, Long.parseLong(args[1]), 0, warp, serverPlayer, server);
                                    }
                                } else {
                                sender.sendMessage(UDSMessage.NO_NUMBER);
                            }
                            } else {
                            sender.sendMessage(UDSMessage.NO_JAIL);
                        }
                        } else {
                        sender.sendMessage("Player is already jailed.");
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

    private void jailPlayer(final Player target, final long sentence, final long fine, final Warp warp, final UDSPlayer serverPlayer, final Server server) {
        final ChatColor messageColor = Color.MESSAGE;
        target.getWorld().strikeLightningEffect(target.getLocation());
        target.teleport(LocationUtils.findSafePlace(warp.getLocation()));
        target.sendMessage(messageColor + "You have been jailed for " + sentence + " minutes for breaking the rules.");
        if(fine != 0) {
            target.sendMessage(messageColor + "You can get out early by paying a " + fine + " " + plugin.getConfig().getString("currency.singular") + " fine with /paybail.");
            serverPlayer.imprison(System.currentTimeMillis(), sentence, fine);
        } else {
            serverPlayer.imprison(System.currentTimeMillis(), sentence, 0);
        }
        target.setGameMode(GameMode.SURVIVAL);
        serverPlayer.setGodMode(true);
        server.broadcastMessage(Color.BROADCAST + serverPlayer.getNick() + " has been jailed.");
    }
}


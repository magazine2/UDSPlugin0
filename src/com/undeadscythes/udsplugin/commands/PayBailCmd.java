package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayBailCmd implements CommandExecutor {
    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);
        if(sender.hasPermission("udsplugin.paybail")) {
            final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(senderName);
            if(serverPlayer.getPrisonTime() != 0) {
                if(serverPlayer.getFine() != 0) {
                    if(serverPlayer.hasEnough(serverPlayer.getFine())) {
                        serverPlayer.takeMoney(serverPlayer.getFine());
                        sender.sendMessage(Color.MESSAGE + "You have paid your bail, tread carefully.");
                        serverPlayer.setGodMode(false);
                        serverPlayer.release();
                        final Warp warp = UDSPlugin.getWarps().get("jailout");
                        if(warp != null) {
                            sender.teleport(warp.getLocation());
                        } else {
                            sender.teleport(server.getWorld(UDSPlugin.getUDSConfig().getWorldName()).getSpawnLocation());
                        }
                    } else {
                        sender.sendMessage(Color.ERROR + "You do not have enough money to pay your bail.");
                    }
                } else {
                    sender.sendMessage(Color.ERROR + "You must serve your full sentence.");
                }
            } else {
                sender.sendMessage(Color.ERROR + "You are not in jail.");
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

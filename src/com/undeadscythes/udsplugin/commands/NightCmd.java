package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NightCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public NightCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.night")) {
            sender.getWorld().setTime(14000);
            server.broadcastMessage(Color.BROADCAST + UDSPlugin.getPlayers().get(senderName).getNick() + " summoned the moon.");
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

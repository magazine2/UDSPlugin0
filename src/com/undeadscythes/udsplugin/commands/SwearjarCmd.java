package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SwearjarCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public SwearjarCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.swearjar")) {
        sender.sendMessage(Color.MESSAGE + "Swearjar contents: " + Color.TEXT + plugin.getConfig().getInt("swearjar") + " " + plugin.getConfig().getString("currency.plural"));
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UDSReloadCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public UDSReloadCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();

        if(commandName.equalsIgnoreCase("udsreload")) {
            if(sender.hasPermission("udsplugin.udsreload")) {
                plugin.reloadConfig();
                UDSPlugin.getUDSConfig().load(plugin.getConfig());
                sender.sendMessage(UDSMessage.MSG_RELOAD);
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

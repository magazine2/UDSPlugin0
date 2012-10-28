package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Warp;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarpCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public DelWarpCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ChatColor colorError = Color.ERROR;

        if(commandName.equalsIgnoreCase("delwarp")) {
            if(sender.hasPermission("udsplugin.delwarp")) {
                if(args.length != 1) {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                    return true;
                }
                Warp warp = UDSPlugin.getWarps().get(args[0]);
                if(warp != null) {
                    UDSPlugin.getWarps().remove(args[0]);
                    sender.sendMessage(colorError + "Warp removed.");
                    return true;
                }
                sender.sendMessage(colorError + "Warp does not exist.");
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

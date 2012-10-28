package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public ServerCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);
        if(args.length == 1) {
            final String subCommand = args[0];
            if(subCommand.equalsIgnoreCase("stop")) {
                if(sender.hasPermission("udsplugin.server.stop")) {
                    Player[] players = server.getOnlinePlayers();
                    for(int i = 0; i < players.length; i++) {
                        players[i].kickPlayer("Server shutting down.");
                    }
                    server.shutdown();
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("info")) {
                if(sender.hasPermission("udsplugin.server.info")) {
                    final ChatColor messageColor = Color.MESSAGE;
                    final ChatColor textColor = Color.TEXT;
                    sender.sendMessage(messageColor + "--- Server Info ---");
                    final List<String> info = plugin.getConfig().getStringList("info");
                    for(String i : info) {
                        sender.sendMessage(textColor + i);
                    }
                    sender.sendMessage(textColor + "This server is using UDSPlugin version " + plugin.getDescription().getVersion() + ".");
                    sender.sendMessage(messageColor + "--- Chat Colors ---");
                    sender.sendMessage(Color.GROUP_DEFAULT + "Default, "
                                     + Color.GROUP_MEMBER + "Member, "
                                     + Color.GROUP_VIP + "VIP, "
                                     + Color.GROUP_WARDEN + "Warden, "
                                     + Color.GROUP_MOD + "Mod, "
                                     + Color.GROUP_ADMIN + "Admin, "
                                     + Color.GROUP_OWNER + "Owner.");
                    sender.sendMessage(textColor + "This server has had " + (Bukkit.getOnlinePlayers().length + Bukkit.getOfflinePlayers().length) + " unique visitors.");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("stats")) {
                if(sender.hasPermission("udsplugin.server.stats")) {
                    sender.sendMessage(Color.MESSAGE + "--- Server Stats ---");
                    Runtime runtime = Runtime.getRuntime();
                    sender.sendMessage(Color.COMMAND + "Memory Usage: " + Color.TEXT + runtime.totalMemory() / (runtime.totalMemory() - runtime.freeMemory()) + "%");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else {
                sender.sendMessage(UDSMessage.BAD_COMMAND);
            }
        } else {
            sender.performCommand("help server");
        }
        return true;
    }
}

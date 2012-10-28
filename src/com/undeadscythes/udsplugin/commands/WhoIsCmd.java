package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhoIsCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public WhoIsCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ChatColor messageColor = Color.MESSAGE;
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();

        if(commandName.equalsIgnoreCase("whois")) {
            if(sender.hasPermission("udsplugin.whois")) {
                if(args.length == 1) {
                    boolean nameFound = false;
                    for(Map.Entry<String, UDSPlayer> i : serverPlayers.entrySet()) {
                        UDSPlayer serverPlayer = i.getValue();
                        if(serverPlayer.getNick().toLowerCase().contains(args[0].toLowerCase())) {
                            sender.sendMessage(messageColor + serverPlayer.getNick() + " is " + serverPlayer.getName() + ".");
                            nameFound = true;
                            break;
                        }
                    }
                    if(!nameFound) {
                        sender.sendMessage(UDSMessage.NO_PLAYER);
                    }
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

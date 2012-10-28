package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RulesCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public RulesCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        UDSConfig config = UDSPlugin.getUDSConfig();
        ChatColor messageColor = Color.MESSAGE;
        ChatColor textColor = Color.TEXT;
        if(commandName.equalsIgnoreCase("rules")) {
            if(sender.hasPermission("udsplugin.rules")) {
                sender.sendMessage(messageColor + "--- Server Rules ---");
                List<String> rules = config.getRules();
                for(String i : rules) {
                    sender.sendMessage(textColor + "- " + i);
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

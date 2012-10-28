package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public TicketCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ChatColor errorColor = Color.ERROR;
        ChatColor messageColor = Color.MESSAGE;

        if(commandName.equalsIgnoreCase("ticket")) {
            if(sender.hasPermission("udsplugin.ticket")) {
                BufferedWriter writer;
                try {
                    writer = new BufferedWriter(new FileWriter("tickets.txt", true));
                } catch (IOException ex) {
                    Logger.getLogger("Minecraft").warning("IO exception");
                    sender.sendMessage(errorColor + "Ticket submission failed.");
                    return true;
                }
                try {
                    writer.write(senderName + " " + StringUtils.join(args, " ") + "\n");
                } catch (IOException ex) {
                    Logger.getLogger("Minecraft").warning("IO exception.");
                    sender.sendMessage(errorColor + "Ticket submission failed.");
                    return true;
                } finally {
                    try {
                        if (writer != null) {
                            writer.flush();
                            writer.close();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger("Minecraft").warning("IO exception.");
                    }
                }
                sender.sendMessage(messageColor + "Ticket sent.");
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

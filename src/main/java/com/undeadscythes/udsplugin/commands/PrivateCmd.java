package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.ChatRoom;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public PrivateCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ChatColor messageColor = Color.MESSAGE;
        ChatColor privateColor = Color.PRIVATE;

        if(commandName.equalsIgnoreCase("private")) {
            if(sender.hasPermission("udsplugin.private")) {
                if(args.length != 1) {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                    return true;
                }
                ChatRoom chatRoom = UDSPlugin.getChatRooms().get(args[0]);
                if(chatRoom != null) {
                    boolean joined = true;
                    for(Iterator<String> j = chatRoom.getMembers().iterator(); j.hasNext();) {
                        String mName = j.next();
                        if(mName.equals(senderName)) {
                            joined = false;
                            j.remove();
                        }
                    }
                    for(Iterator<String> j = chatRoom.getMembers().iterator(); j.hasNext();) {
                        String mName = j.next();
                        Player member = Bukkit.getPlayerExact(mName);
                        if(joined) {
                            member.sendMessage(privateColor + senderName + " joined the chat " + args[0] + ".");
                        }
                        if(!joined) {
                            member.sendMessage(privateColor + senderName + " left the chat " + args[0] + ".");
                        }
                    }
                    if(joined) {
                        chatRoom.getMembers().add(senderName);
                    }
                    if(chatRoom.getMembers().isEmpty()) {
                        UDSPlugin.getChatRooms().remove(args[0]);
                    }
                    if(joined) {
                        sender.sendMessage(messageColor + "You have joined private chat " + args[0] + ".");
                    }
                    if(!joined) {
                        sender.sendMessage(messageColor + "You have left private chat " + args[0] + ".");
                    }
                    return true;
                }
            UDSPlugin.getChatRooms().put(args[0], new ChatRoom(senderName, args[0]));
            sender.sendMessage(messageColor + "Created private chat " + args[0] + ".");
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

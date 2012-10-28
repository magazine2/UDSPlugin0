package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Request;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public NCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ChatColor colorError = Color.ERROR;
        ChatColor colorMessage = Color.MESSAGE;
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();

        if(commandName.equalsIgnoreCase("n") && sender instanceof Player) {
            if(sender.hasPermission("udsplugin.n")) {
                UDSPlayer serverPlayer = serverPlayers.get(senderName);
                Request request = UDSPlugin.getRequests().get(serverPlayer.getName());
                if(request != null) {
                    if(request.getType() == Request.Type.TP) {
                        Player target = Bukkit.getPlayerExact(request.getSender());
                        if(target == null) {
                            UDSPlugin.getRequests().remove(serverPlayer.getName());
                            return true;
                        }
                        target.sendMessage(colorMessage + "Your request to teleport was refused.");
                        UDSPlugin.getRequests().remove(serverPlayer.getName());
                        return true;
                    } else if(request.getType() == Request.Type.SHOP) {
                        Player target = Bukkit.getPlayerExact(request.getSender());
                        if(target == null) {
                            UDSPlugin.getRequests().remove(serverPlayer.getName());
                            return true;
                        }
                        target.sendMessage(colorMessage + "Your offer has been refused.");
                        UDSPlugin.getRequests().remove(serverPlayer.getName());
                        return true;
                    } else if(request.getType() == Request.Type.HOME) {
                        Player target = Bukkit.getPlayerExact(request.getSender());
                        if(target == null) {
                            UDSPlugin.getRequests().remove(serverPlayer.getName());
                            return true;
                        }
                        target.sendMessage(colorMessage + "Your offer has been refused.");
                        UDSPlugin.getRequests().remove(serverPlayer.getName());
                        return true;
                    } else if(request.getType() == Request.Type.CHALLENGE) {
                        Player target = Bukkit.getPlayerExact(request.getSender());
                        if(target == null) {
                            UDSPlugin.getRequests().remove(serverPlayer.getName());
                            return true;
                        }
                        target.sendMessage(colorMessage + "Your challenge has been refused.");
                        UDSPlugin.getRequests().remove(serverPlayer.getName());
                        return true;
                    } else if(request.getType() == Request.Type.CLAN) {
                        Player target = Bukkit.getPlayerExact(request.getSender());
                        if(target == null) {
                            UDSPlugin.getRequests().remove(serverPlayer.getName());
                            return true;
                        }
                        target.sendMessage(colorMessage + "Your invitation has been refused.");
                        UDSPlugin.getRequests().remove(serverPlayer.getName());
                        return true;
                    } else if(request.getType() == Request.Type.PET) {
                        Player target = Bukkit.getPlayerExact(request.getSender());
                        if(target == null) {
                            UDSPlugin.getRequests().remove(serverPlayer.getName());
                            return true;
                        }
                        target.sendMessage(colorMessage + "Your request sell pet was refused.");
                        UDSPlugin.getRequests().remove(serverPlayer.getName());
                        return true;
                    }
                }
                sender.sendMessage(colorError + "You have no requests to refuse.");
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

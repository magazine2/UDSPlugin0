package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSString;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public NickCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.nick")) {
            if(args.length == 1) {
                final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
                for(Map.Entry<String, UDSPlayer> i : serverPlayers.entrySet()) {
                    if(i.getValue().getNick().equalsIgnoreCase(args[0])) {
                        sender.sendMessage(Color.ERROR + "Nickname already taken.");
                        return true;
                    }
                }
                if(serverPlayers.get(args[0]) == null) {
                    if(!(new UDSString(args[0]).censor())) {
                        serverPlayers.get(senderName).setNick(args[0]);
                        sender.sendMessage(Color.MESSAGE + "Nickname changed to " + args[0] + ".");
                    } else {
                        sender.sendMessage(UDSMessage.BAD_STRING);
                    }
                } else {
                    sender.sendMessage(Color.ERROR + "You cannot have that nickname.");
                }
            } else if(args.length == 2) {
                final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
                for(Map.Entry<String, UDSPlayer> i : serverPlayers.entrySet()) {
                    if(i.getValue().getNick().equalsIgnoreCase(args[1])) {
                        sender.sendMessage(Color.ERROR + "Nickname already taken.");
                        return true;
                    }
                }
                final UDSPlayer target = PlayerUtils.matchUDS(args[0]);
                if(target != null) {
                    if(!(new UDSString(args[0]).censor())) {
                        final String targetName = target.getName();
                        final ChatColor messageColor = Color.MESSAGE;
                        serverPlayers.get(targetName).setNick(args[1]);
                        sender.sendMessage(messageColor + targetName + "'s nickname changed to " + args[1] + ".");
                        final Player player = Bukkit.getPlayerExact(target.getName());
                        if(player != null) {
                            player.sendMessage(messageColor + "Your nickname has been changed to " + args[1] + ".");
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_STRING);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PLAYER);
                }
            } else {
                sender.sendMessage(UDSMessage.BAD_ARGS);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

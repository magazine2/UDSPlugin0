package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TellCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public TellCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        UDSPlayer senderPlayer = serverPlayers.get(senderName);

        if(commandName.equalsIgnoreCase("tell") || commandName.equalsIgnoreCase("msg") || commandName.equalsIgnoreCase("whisper")) {
            if(sender.hasPermission("udsplugin.tell")) {
                Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                args[0] = "";
                if(target != null) {
                    String senderNameToPrint = senderName;
                    String targetName = PlayerUtils.matchUDS(target.getName()).getNick();
                    if(senderPlayer.hasNick()) {
                        senderNameToPrint = senderPlayer.getNick();
                    }
                    target.sendMessage(Color.WHISPER + senderNameToPrint + " > " + targetName + ":" + StringUtils.join(args, " "));
                    sender.sendMessage(Color.WHISPER + senderNameToPrint + " > " + targetName + ":" + StringUtils.join(args, " "));
                    serverPlayers.get(senderName).setLastChat(target.getName());
                    serverPlayers.get(target.getName()).setLastChat(senderName);
                    Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
                    for (int i = 0; i < onlinePlayers.length; i++) {
                        if(onlinePlayers[i].hasPermission("udsplugin.spy") && target != onlinePlayers[i] && sender != onlinePlayers[i]) {
                            onlinePlayers[i].sendMessage(Color.WHISPER + senderNameToPrint + " > " + target.getName() + ": " + StringUtils.join(args, " "));
                        }
                    }
                } else {
                    sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                }
            } else {
                sender.sendMessage(UDSMessage.BAD_ARGS);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

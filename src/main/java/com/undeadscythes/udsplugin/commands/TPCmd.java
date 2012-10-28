package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public TPCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();

        if(commandName.equalsIgnoreCase("tp")) {
            if(sender.hasPermission("udsplugin.tp")) {
                if(args.length == 2) {
                    Player player1 = PlayerUtils.matchOnlinePlayer(args[0]);
                    if(player1 == null) {
                        sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                        return true;
                    }
                    Player player2 = PlayerUtils.matchOnlinePlayer(args[1]);
                    if(player2 == null) {
                        sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                        return true;
                    }
                    serverPlayers.get(player1.getName()).setBackPoint(sender.getLocation());
                    player1.teleport(player2);
                }
                if(args.length == 1) {
                    Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                    if(target == null) {
                        sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                        return true;
                    }
                    serverPlayers.get(sender.getName()).setBackPoint(sender.getLocation());
                    sender.teleport(target);
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

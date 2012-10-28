package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TGMCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public TGMCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ChatColor messageColor = Color.MESSAGE;

        if(commandName.equalsIgnoreCase("tgm")) {
            if(sender.hasPermission("udsplugin.tgm")) {
                if(args.length == 0) {
                    if(sender.getGameMode() == GameMode.CREATIVE) {
                        sender.setGameMode(GameMode.SURVIVAL);
                        sender.sendMessage(messageColor + "You are now in survival mode.");
                    } else {
                        sender.setGameMode(GameMode.CREATIVE);
                        sender.sendMessage(messageColor + "You are now in creative mode.");
                    }
                } else if(args.length == 1) {
                    if(sender.hasPermission("udsplugin.tgm.o")) {
                        Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                        if(target != null) {
                            String targetName = target.getName();
                            if(target.getGameMode() == GameMode.CREATIVE) {
                                target.setGameMode(GameMode.SURVIVAL);
                                sender.sendMessage(messageColor + targetName + " is now in survival mode.");
                                target.sendMessage(messageColor + "You are now in survival mode.");
                            } else {
                                target.setGameMode(GameMode.CREATIVE);
                                sender.sendMessage(messageColor + targetName + " is now in creative mode.");
                                target.sendMessage(messageColor + "You are now in creative mode.");
                            }
                        } else {
                            sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.NO_PERM);
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

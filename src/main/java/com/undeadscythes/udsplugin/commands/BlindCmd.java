package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

public class BlindCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public BlindCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        World world = server.getWorld(UDSPlugin.getUDSConfig().getWorldName());
        if(commandName.equalsIgnoreCase("blind")) {
            if(sender.hasPermission("udsplugin.fun.blind")) {
                if(args.length == 1) {
                    Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                    if(target != null) {
                        for(int i = 0; i < 100; i++) {
                            world.spawn(target.getLocation(), ExperienceOrb.class);
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
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

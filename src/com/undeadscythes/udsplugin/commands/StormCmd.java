package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StormCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public StormCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.storm")) {
            if(args.length < 2) {
                final World world = sender.getWorld();
                world.setStorm(true);
                world.setThundering(true);
                if(args.length == 0) {
                    sender.getWorld().setThunderDuration(500);
                } else {
                    sender.getWorld().setThunderDuration(Integer.parseInt(args[0]));
                }
                server.broadcastMessage(Color.BROADCAST + senderName + " angered Thor and now there is a storm approaching.");
            } else {
                sender.sendMessage(UDSMessage.BAD_ARGS);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

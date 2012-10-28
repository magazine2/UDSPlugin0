package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public SetSpawnCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        UDSConfig config = UDSPlugin.getUDSConfig();
        World world = server.getWorld(config.getWorldName());
        ChatColor messageColor = Color.MESSAGE;

        if(commandName.equalsIgnoreCase("setspawn")) {
            if(sender.hasPermission("udsplugin.setspawn")) {
                Location location = sender.getLocation();
                world.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                config.setSpawnPitch(location.getPitch());
                config.setSpawnYaw(location.getYaw());
                config.save();
                sender.sendMessage(messageColor + "Spawn set to your current location.");
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

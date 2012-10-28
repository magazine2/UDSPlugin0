package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapDupeCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public MapDupeCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        UDSConfig config = UDSPlugin.getUDSConfig();
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        UDSPlayer senderPlayer = serverPlayers.get(senderName);

        if(commandName.equalsIgnoreCase("mapdupe")) {
            if(sender.hasPermission("udsplugin.mapdupe")) {
                if(sender.getItemInHand().getType() == Material.MAP) {
                    if(senderPlayer.hasEnough(config.getMapCost())) {
                        sender.getItemInHand().setAmount(sender.getItemInHand().getAmount() + 1);
                        senderPlayer.takeMoney(config.getMapCost());
                    }
                    else {
                        sender.sendMessage(UDSMessage.NO_MONEY);
                    }
                } else {
                    sender.sendMessage(UDSMessage.CANT_DUPE);
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

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MapCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public MapCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        final UDSConfig config = UDSPlugin.getUDSConfig();
        final long mapcost = config.getMapCost();
        final UDSPlayer senderPlayer = UDSPlugin.getPlayers().get(senderName);


        if(senderPlayer.hasEnough(mapcost)) {
            if(sender.hasPermission("udsplugin.map")) {
                sender.getInventory().addItem(new ItemStack(Material.MAP, 1, config.getMap()));
                senderPlayer.takeMoney(mapcost);
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_MONEY);
        }
        return true;
    }
}

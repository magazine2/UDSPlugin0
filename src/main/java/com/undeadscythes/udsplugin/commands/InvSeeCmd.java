package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSInventory;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvSeeCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public InvSeeCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.invsee")) {
            if(args.length == 1) {
                final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
                if(target == null) {
                    sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                } else {
                    UDSPlugin.getInventories().put(senderName, new UDSInventory(sender.getInventory().getContents(), sender.getInventory().getArmorContents()));
                    sender.getInventory().setContents(target.getInventory().getContents());
                    sender.getInventory().setArmorContents(target.getInventory().getArmorContents());
                    sender.sendMessage(Color.MESSAGE + "Copied " + target.getName() + "'s inventory.");
                }
            } else if(args.length == 0) {
                final UDSInventory inventory = UDSPlugin.getInventories().get(senderName);
                sender.getInventory().setContents(inventory.getInventory());
                sender.getInventory().setArmorContents(inventory.getArmor());
            } else {
                sender.sendMessage(UDSMessage.BAD_ARGS);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

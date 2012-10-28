package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ScubaCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public ScubaCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.scuba")) {
            if(sender.getInventory().getHelmet() == null) {
                if(sender.getItemInHand().getType() == Material.GLASS) {
                    sender.getInventory().setHelmet(new ItemStack(Material.GLASS));
                    final ItemStack glass = sender.getItemInHand().clone();
                    glass.setAmount(glass.getAmount() - 1);
                    sender.setItemInHand(glass);
                    sender.sendMessage(Color.MESSAGE + "You put on your scuba gear.");
                } else {
                    sender.sendMessage(UDSMessage.NO_GLASS);
                }
            } else if(sender.getInventory().getHelmet().getType() != Material.GLASS) {
                sender.sendMessage(UDSMessage.ERR_HELMET);
            } else if(sender.getInventory().getHelmet().getType() == Material.GLASS) {
                sender.getInventory().setHelmet(new ItemStack(Material.AIR));
                if(!sender.getInventory().addItem(new ItemStack(Material.GLASS)).isEmpty()) {
                    sender.getWorld().dropItemNaturally(sender.getLocation(), new ItemStack(Material.GLASS));
                    sender.sendMessage(UDSMessage.MSG_DROP_SCUBA);
                } else {
                    sender.sendMessage(Color.MESSAGE + "You take off your scuba gear.");
                }
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

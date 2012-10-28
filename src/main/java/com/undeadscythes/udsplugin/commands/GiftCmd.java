package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiftCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public GiftCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.gift")) {
            if(args.length == 0) {
               sender.sendMessage(UDSMessage.BAD_ARGS);
               return true;
            }
            final Player target = PlayerUtils.matchOnlinePlayer(args[0]);
            if (target != null) {
                String message = "";
                if(args.length != 1) {
                    for (int i = 1; i < args.length; i++){
                        message = message + args[i] + " ";
                    }
                } else {
                    message = "You have recieved a gift.";
                }
                ItemStack stack = sender.getItemInHand().clone();
                sender.getInventory().remove(stack);
                target.getInventory().addItem(stack);
                sender.sendMessage(Color.MESSAGE + "Successfully sent a gift to " + target.getName());
                target.sendMessage(Color.MESSAGE + "Gifting Service: " + message);
                return true;
            }
            else
            {
                sender.sendMessage(Color.ERROR + "Can't find player.");
                return true;
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}
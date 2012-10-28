package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public EnchantCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.enchant")) {
            if(args.length == 1 || args.length == 2) {
                final Enchantment enchantment = Enchantment.getByName(args[0].toUpperCase());
                if(enchantment != null) {
                    int level = enchantment.getMaxLevel();
                    if(args.length == 2) {
                        level = Integer.parseInt(args[1]);
                    }
                    final ItemStack stack = sender.getItemInHand();
                    stack.addEnchantment(enchantment, level);
                    sender.getInventory().setItemInHand(stack);
                } else {
                    sender.sendMessage(UDSMessage.NO_ENCH);
                }
            } else {
                sender.sendMessage(UDSMessage.BAD_ARGS);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

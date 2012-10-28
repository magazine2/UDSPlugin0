package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StackCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public StackCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);



        if(sender.hasPermission("udsplugin.stack")) {
            ItemStack[] items = sender.getInventory().getContents();
            final int len = items.length;
            boolean affected = false;
            for (int i = 0; i < len; i++) {
                final ItemStack item = items[i];
                if (item != null && item.getAmount() > 0 && item.getMaxStackSize() > 1) {
                    final int max = item.getMaxStackSize();
                    if (item.getAmount() < max) {
                        int needed = max - item.getAmount();
                        for (int j = i + 1; j < len; j++) {
                            final ItemStack item2 = items[j];
                            if (item2 != null && item2.getAmount() > 0 && item.getMaxStackSize() > 1) {
                                final boolean a = item2.getTypeId() == item.getTypeId();
                                final boolean b = item.getDurability() == item2.getDurability();
                                final boolean c = item.getEnchantments().equals(item2.getEnchantments());
                                if (a && b && c) {
                                    if (item2.getAmount() > needed) {
                                        item.setAmount(64);
                                        item2.setAmount(item2.getAmount() - needed);
                                        break;
                                    }
                                    items[j] = null;
                                    item.setAmount(item.getAmount() + item2.getAmount());
                                    needed = 64 - item.getAmount();
                                    affected = true;
                                }
                            }
                        }
                    }
                }
            }
            if (affected) {
                sender.getInventory().setContents(items);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}
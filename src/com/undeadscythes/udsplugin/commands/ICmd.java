package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.exceptions.NaNException;
import com.undeadscythes.udsplugin.exceptions.WhitelistException;
import com.undeadscythes.udsplugin.exceptions.ZeroSpawnsException;
import com.undeadscythes.udsplugin.utilities.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ICmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public ICmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        ChatColor errorColor = Color.ERROR;
        ChatColor messageColor = Color.MESSAGE;
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        UDSPlayer senderPlayer = serverPlayers.get(senderName);

        if(args.length == 0 && sender.hasPermission("udsplugin.i.vip")) {
            sender.sendMessage(messageColor + "You have " + senderPlayer.getVipSpawns() + " spawns remaining today.");
        } else if(args.length == 1 || args.length == 2) {
            ItemStack item = null;
            if(args.length == 1) {
                item = ItemUtils.findItem(args[0]);
                item.setAmount(64);
            } else if(args.length == 2) {
                try {
                    item = ItemUtils.findItem(args[0], args[1]);
                } catch (NaNException ex) {
                    sender.sendMessage(UDSMessage.NO_NUMBER);
                }
            }
            if(item != null) {
                if(sender.hasPermission("udsplugin.i.vip")) {
                    try {
                        dailyItemSpawn(sender, item);
                    } catch (ZeroSpawnsException ex) {
                        sender.sendMessage(errorColor + "You have run out of spawns for today.");
                    } catch (WhitelistException ex) {
                        sender.sendMessage(UDSMessage.CANT_SPAWN);
                    }
                } else if(sender.hasPermission("udsplugin.i.warden")) {
                    try {
                        whitelistItemSpawn(sender, item);
                    } catch (WhitelistException ex) {
                        sender.sendMessage(UDSMessage.CANT_SPAWN);
                    }
                } else if(sender.hasPermission("udsplugin.i")) {
                    normalItemSpawn(sender, item);
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_ITEM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_ARGS);
        }
        return true;
    }

    public void dailyItemSpawn(Player player, ItemStack item) throws ZeroSpawnsException, WhitelistException {
        UDSPlayer udsPlayer = UDSPlugin.getPlayers().get(player.getName());
        int spawnsRemaining = udsPlayer.getVipSpawns();
        if(spawnsRemaining < item.getAmount()) {
            item.setAmount(spawnsRemaining);
        }
        if(item.getAmount() == 0) {
            throw new ZeroSpawnsException();
        }
        udsPlayer.reduceVipSpawns(item.getAmount());
        whitelistItemSpawn(player, item);
    }

    public void whitelistItemSpawn(Player player, ItemStack item) throws WhitelistException {
        if(UDSPlugin.getUDSConfig().getItemWhiteList().contains(item.getType())) {
            normalItemSpawn(player, item);
        }
        else {
            throw new WhitelistException();
        }
    }

    public void normalItemSpawn(Player player, ItemStack item) {
        player.getInventory().addItem(item);
    }
}

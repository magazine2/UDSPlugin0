package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSPlayer;
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

public class KitCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public KitCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.Kit")) {
          if (args.length == 0) {
              UDSConfig config = UDSPlugin.getUDSConfig();
              sender.sendMessage(Color.MESSAGE + "Available tool kits:");
              sender.sendMessage(Color.COMMAND + "Stone - " + Color.TEXT + config.getStoneKitCost() + " " + config.getCurrencies());
              sender.sendMessage(Color.COMMAND + "Iron - " + Color.TEXT +  config.getIronKitCost() + " " + config.getCurrencies());
              sender.sendMessage(Color.COMMAND + "Diamond - " + Color.TEXT +  config.getDiamondKitCost() + " " + config.getCurrencies());
    } else if (args.length ==1) {if(args[0].equalsIgnoreCase("stone")) {
         final UDSPlayer senderPlayer = UDSPlugin.getPlayers().get(senderName);
        if(senderPlayer.hasEnough(plugin.getConfig().getLong("cost.kit.stone"))) {
      sender.getInventory().addItem (new ItemStack (Material.STONE_AXE));
      sender.getInventory().addItem (new ItemStack (Material.STONE_HOE ));
      sender.getInventory().addItem (new ItemStack (Material.STONE_PICKAXE));
      sender.getInventory().addItem (new ItemStack (Material.STONE_SPADE ));
      senderPlayer.takeMoney(plugin.getConfig().getLong("cost.kit.stone"));
        } else {
                        sender.sendMessage(UDSMessage.NO_MONEY);
                    }
        } else if(args[0].equalsIgnoreCase("iron")) {
         final UDSPlayer senderPlayer = UDSPlugin.getPlayers().get(senderName);
        if(senderPlayer.hasEnough(plugin.getConfig().getLong("cost.kit.iron"))) {
      sender.getInventory().addItem (new ItemStack (Material.IRON_AXE));
      sender.getInventory().addItem (new ItemStack (Material.IRON_HOE ));
      sender.getInventory().addItem (new ItemStack (Material.IRON_PICKAXE));
      sender.getInventory().addItem (new ItemStack (Material.IRON_SPADE ));
      senderPlayer.takeMoney(plugin.getConfig().getLong("cost.kit.iron"));
        } else {
                        sender.sendMessage(UDSMessage.NO_MONEY);
                    }
      } else if(args[0].equalsIgnoreCase("diamond")) {
      final UDSPlayer senderPlayer = UDSPlugin.getPlayers().get(senderName);
       if(senderPlayer.hasEnough(plugin.getConfig().getLong("cost.kit.diamond"))) {
      sender.getInventory().addItem (new ItemStack (Material.DIAMOND_AXE));
      sender.getInventory().addItem (new ItemStack (Material.DIAMOND_HOE ));
      sender.getInventory().addItem (new ItemStack (Material.DIAMOND_PICKAXE));
      sender.getInventory().addItem (new ItemStack (Material.DIAMOND_SPADE ));
      senderPlayer.takeMoney(plugin.getConfig().getLong("cost.kit.diamond"));
        } else {
                        sender.sendMessage(UDSMessage.NO_MONEY);
                    }
        } else {
                    sender.sendMessage(UDSMessage.NO_KIT);
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
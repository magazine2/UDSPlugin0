package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.ItemUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import com.undeadscythes.udsplugin.utilities.ShopUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.EnderChest;

public class InventoryOpen implements Listener {
    @EventHandler
    public final void onInventoryOpen(final InventoryOpenEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if(inventoryHolder instanceof Player) {
            return;
        }
        Location location = null;
        if(inventoryHolder instanceof EnderChest) {
            event.setCancelled(true);
            return;
        }
        if(inventoryHolder instanceof BrewingStand) {
            location = ((BrewingStand) inventoryHolder).getLocation();
        }
        if(inventoryHolder instanceof Dispenser) {
            location = ((Dispenser) inventoryHolder).getLocation();
        }
        if(inventoryHolder instanceof Furnace) {
            location = ((Furnace) inventoryHolder).getLocation();
        }
        if(inventoryHolder instanceof DoubleChest) {
            location = ((DoubleChest) inventoryHolder).getLocation();
        }
        if(location != null &&
           !RegionUtils.canBuildHere(location, (Player) event.getPlayer())) {
            if(((Player) event.getPlayer()).hasPermission("udsplugin.bypass")) {
                ((Player) event.getPlayer()).sendMessage(UDSMessage.MSG_BYPASS);
                return;
            }
            event.setCancelled(true);
            ((Player) event.getPlayer()).sendMessage(UDSMessage.NO_ACCESS);
            return;
        }
        if(inventoryHolder instanceof Chest) {
            location = ((Chest) inventoryHolder).getLocation();
            Chest chest = (Chest) event.getInventory().getHolder();
            if(chest.getBlock().getRelative(BlockFace.UP).getType() == Material.WALL_SIGN) {
                InventoryView inventoryView = event.getView();
                Inventory shop = inventoryView.getTopInventory();
                Sign sign = (Sign) chest.getBlock().getRelative(BlockFace.UP).getState();
                if(ShopUtils.isShopSign(sign.getLines())) {
                    String playerName = sign.getLine(0).replace(Color.SIGN.toString(), "");
                    ItemStack item = ItemUtils.findItem(sign.getLine(2));
                    item.setAmount(64);
                    if("Server".equals(playerName)) {
                        UDSPlugin.getPlayers().put("server", new UDSPlayer("server"));
                        for(int i = 0; i < 27; i++) {
                            shop.setItem(i, item);
                        }
                    }
                } else if(!RegionUtils.canBuildHere(location, (Player) event.getPlayer())) {
                    if(((Player) event.getPlayer()).hasPermission("udsplugin.bypass")) {
                        ((Player) event.getPlayer()).sendMessage(UDSMessage.MSG_BYPASS);
                        return;
                    }
                    event.setCancelled(true);
                    ((Player) event.getPlayer()).sendMessage(UDSMessage.NO_ACCESS);
                }
            } else if(!RegionUtils.canBuildHere(location, (Player) event.getPlayer())) {
                if(((Player) event.getPlayer()).hasPermission("udsplugin.bypass")) {
                    ((Player) event.getPlayer()).sendMessage(UDSMessage.MSG_BYPASS);
                    return;
                }
                event.setCancelled(true);
                ((Player) event.getPlayer()).sendMessage(UDSMessage.NO_ACCESS);
            }
        }
    }
}

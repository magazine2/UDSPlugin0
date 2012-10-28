package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.ItemUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import com.undeadscythes.udsplugin.utilities.ShopUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class SignChange implements Listener {
    private final static int PRICE_LINE = 3;

    @EventHandler
    public final void onSignChange(final SignChangeEvent event) {
        final ChatColor errorColor = Color.ERROR;
        final ChatColor shopColor = Color.SIGN;
        final Block block = event.getBlock();
        final String line0 = event.getLine(0);
        final String line1 = event.getLine(1);
        final String line2 = event.getLine(2);
        if(event.getLine(1).equalsIgnoreCase("shop")) {
            if(event.getPlayer().hasPermission("udsplugin.shop.server") && line0.equalsIgnoreCase("server")) {
                if(ShopUtils.isShopSign(event.getLines())) {
                    event.setLine(0, shopColor + "Server");
                    event.setLine(1, shopColor + "Shop");
                    event.setLine(PRICE_LINE, "B " + event.getLine(PRICE_LINE) + " S");
                } else {
                    event.getPlayer().sendMessage(errorColor + "This sign shop is not formatted correctly. Check with /shop sign.");
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
                }
            } else if(event.getPlayer().hasPermission("udsplugin.shop.sign")) {
                if(ShopUtils.isShopSign(event.getLines())) {
                    Location pLoc = event.getBlock().getLocation();
                    if(RegionUtils.isInRegion(pLoc, event.getPlayer().getName() + "shop") || RegionUtils.isWorkingIn(event.getPlayer().getName(), pLoc)) {
                        event.setLine(0, shopColor + RegionUtils.findShopOwner(pLoc));
                        event.setLine(1, shopColor + "Shop");
                        event.setLine(PRICE_LINE, "B " + event.getLine(PRICE_LINE) + " S");
                    } else {
                        event.getPlayer().sendMessage(errorColor + "You can only place shop signs in a shop you own or work in.");
                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
                    }
                } else {
                    event.getPlayer().sendMessage(errorColor + "This sign shop is not formatted correctly. Check with /shop sign.");
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
                }
            } else {
                event.getPlayer().sendMessage(UDSMessage.NO_ACCESS);
                event.setCancelled(true);
                block.setType(Material.AIR);
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
            }
        } else if(line0.equalsIgnoreCase("[checkpoint]")) {
            if(event.getPlayer().hasPermission("udsplugin.sign.checkpoint")) {
                event.setLine(0, shopColor + "[CHECKPOINT]");
            } else {
                    event.getPlayer().sendMessage(errorColor + "You cannot place checkpoint signs.");
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
            }
        } else if(line0.equalsIgnoreCase("[minecart]")) {
            if(event.getPlayer().hasPermission("udsplugin.sign.minecart")) {
                event.setLine(0, shopColor + "[MINECART]");
            } else {
                    event.getPlayer().sendMessage(errorColor + "You cannot place minecart signs.");
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
            }
        } else if(line0.equalsIgnoreCase("[prize]")) {
            if(event.getPlayer().hasPermission("udsplugin.sign.prize") && ItemUtils.findItem(line1) != null && line2.matches("[0-9][0-9]*")) {
                event.setLine(0, shopColor + "[PRIZE]");
            } else {
                    event.getPlayer().sendMessage(errorColor + "You cannot place prize signs.");
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
            }
        } else if(line0.equalsIgnoreCase("[item]")) {
            if(event.getPlayer().hasPermission("udsplugin.sign.item") && ItemUtils.findItem(line1) != null && line2.matches("[0-9][0-9]*")) {
                event.setLine(0, shopColor + "[ITEM]");
            } else {
                    event.getPlayer().sendMessage(errorColor + "You cannot place item signs.");
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
            }
        } else if(line0.equalsIgnoreCase("[warp]")) {
            if(event.getPlayer().hasPermission("udsplugin.sign.warp")) {
                event.setLine(0, shopColor + "[WARP]");
            } else {
                    event.getPlayer().sendMessage(errorColor + "You cannot place warp signs.");
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
            }
        } else if(line0.equalsIgnoreCase("[spleef]")) {
            if(event.getPlayer().hasPermission("udsplugin.sign.spleef")) {
                event.setLine(0, shopColor + "[SPLEEF]");
            } else {
                    event.getPlayer().sendMessage(errorColor + "You cannot place spleef signs.");
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SIGN, 1));
            }
        }
    }
}

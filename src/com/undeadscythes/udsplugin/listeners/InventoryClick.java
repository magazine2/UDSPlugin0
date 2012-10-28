package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.ItemUtils;
import com.undeadscythes.udsplugin.utilities.ShopUtils;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {
    @EventHandler
    public final void onInventoryClick(final InventoryClickEvent event) {
        if(event.getInventory().getHolder() instanceof Chest) {
            final Chest chest = (Chest) event.getInventory().getHolder();
            if(chest.getBlock().getRelative(BlockFace.UP).getType() == Material.WALL_SIGN) {
                final Sign sign = (Sign) chest.getBlock().getRelative(BlockFace.UP).getState();
                if(ShopUtils.isShopSign(sign.getLines())) {
                    final String shopOwner = sign.getLine(0).replace(Color.SIGN.toString(), "");
                    boolean serverShop = false;
                    if("Server".equals(shopOwner)) {
                        serverShop = true;
                    }
                    if(event.getWhoClicked().getName().equalsIgnoreCase(shopOwner)) {
                        return;
                    }
                    if(event.getCursor().getTypeId() == 0) {
                        final Player shopperPlayer = (Player) event.getWhoClicked();
                        final ItemStack tradingItem = event.getCurrentItem().clone();
                        final ItemStack shopItem = ItemUtils.findItem(sign.getLine(2));
                        if(tradingItem.getType() == shopItem.getType() && tradingItem.getData().getData() == shopItem.getData().getData()) {
                            tradingItem.setAmount(1);
                            final int rawSlot = event.getRawSlot();
                            int tradeValue;
                            boolean shopIsSelling;
                            final UDSPlayer customer = UDSPlugin.getPlayers().get(shopperPlayer.getName());
                            UDSPlayer owner = null;
                            if(!serverShop) {
                                owner = UDSPlugin.getPlayers().get(shopOwner);
                            }
                            final InventoryView inventoryView = event.getView();
                            final Inventory shop = inventoryView.getTopInventory();
                            final Inventory pack = inventoryView.getBottomInventory();
                            if(event.isShiftClick()) {
                                tradingItem.setAmount(event.getCurrentItem().getAmount());
                            } else if(event.isRightClick()) {
                                tradingItem.setAmount(event.getCurrentItem().getAmount() / 2);
                            }
                            final int buyPrice = Integer.parseInt(sign.getLine(3).split(":")[0].replace("B ", ""));
                            final int sellPrice = Integer.parseInt(sign.getLine(3).split(":")[1].replace(" S", ""));
                            if(rawSlot < 27) {
                                tradeValue = buyPrice * tradingItem.getAmount();
                                shopIsSelling = true;
                            } else {
                                tradeValue = sellPrice * tradingItem.getAmount();
                                shopIsSelling = false;
                            }
                            if(shopIsSelling) {
                                if(tradeValue != 0) {
                                    if(customer.hasEnough(tradeValue)) {
                                        final HashMap<Integer, ItemStack> overflow = pack.addItem(tradingItem.clone());
                                        int overflowAmount = 0;
                                        if(!overflow.isEmpty()) {
                                            overflowAmount = overflow.get(0).getAmount();
                                        }
                                        customer.takeMoney(tradeValue - overflowAmount * buyPrice);
                                        if(!serverShop) {
                                            owner.addMoney(tradeValue - overflowAmount * buyPrice);
                                        }
                                        tradingItem.setAmount(event.getCurrentItem().getAmount() - tradingItem.getAmount() + overflowAmount);
                                        shop.setItem(rawSlot, tradingItem);
                                    } else {
                                        shopperPlayer.sendMessage(UDSMessage.NO_MONEY);
                                    }
                                } else {
                                    shopperPlayer.sendMessage(UDSMessage.DOES_NOT_SELL);
                                }
                            } else {
                                if(tradeValue != 0) {
                                    if(serverShop || owner.hasEnough(tradeValue)) {
                                        final HashMap<Integer, ItemStack> overflow = shop.addItem(tradingItem.clone());
                                        int overflowAmount = 0;
                                        if(!overflow.isEmpty() && !serverShop) {
                                            overflowAmount = overflow.get(0).getAmount();
                                        }
                                        customer.addMoney(tradeValue - overflowAmount * sellPrice);
                                        if(!serverShop) {
                                            owner.takeMoney(tradeValue - overflowAmount * sellPrice);
                                        }
                                        tradingItem.setAmount(event.getCurrentItem().getAmount() - tradingItem.getAmount() + overflowAmount);
                                        pack.setItem(event.getSlot(), tradingItem);
                                    } else {
                                        shopperPlayer.sendMessage(UDSMessage.NO_SHOP_MONEY);
                                    }
                                } else {
                                    shopperPlayer.sendMessage(UDSMessage.DOES_NOT_BUY);
                                }
                            }
                        } else {
                            shopperPlayer.sendMessage(UDSMessage.DOES_NOT_BUY_SELL);
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}

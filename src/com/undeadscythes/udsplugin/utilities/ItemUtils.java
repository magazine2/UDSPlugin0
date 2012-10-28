package com.undeadscythes.udsplugin.utilities;

import com.undeadscythes.udsplugin.UDSItem;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.exceptions.NaNException;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A collection of tools to find various objects within hash maps.
 * @author UndeadScythes
 */
public final class ItemUtils {
    /**
     * Find an item by name, checks custom item name list.
     * @param item Name.
     * @return Item or null.
     */
    public static ItemStack findItem(final String item) {
        ItemStack itemStack;
        if(item.contains(":")) {
            final String itemName = item.split(":")[0];
            Material material;
            if(itemName.matches("[0-9][0-9]*")) {
                material = Material.getMaterial(Integer.parseInt(itemName));
            } else {
                material = Material.matchMaterial(itemName);
            }
            if(material == null) {
                itemStack = null;
            } else {
                itemStack = new ItemStack(material, 1, (short) 0, Byte.parseByte(item.split(":")[1]));
            }
        } else {
            Material material;
            if(item.matches("[0-9][0-9]*")) {
                material = Material.getMaterial(Integer.parseInt(item));
            } else {
                material = Material.matchMaterial(item);
            }
            if(material == null) {
                final UDSItem myItem = UDSPlugin.getItems().get(item);
                if(myItem == null) {
                    itemStack = null;
                } else {
                    material = Material.getMaterial(myItem.getId());
                    itemStack = new ItemStack(material, 1, (short) 0, myItem.getData());
                }
            } else {
                itemStack = new ItemStack(material, 1);
            }
        }
        return itemStack;
    }

    public static ItemStack findItem(final String item, final String amount) throws NaNException {
        int amountI;
        if(amount.matches("[0-9][0-9]*")) {
            amountI = Integer.parseInt(amount);
        } else {
            throw new NaNException();
        }
        ItemStack itemStack;
        if(item.contains(":")) {
            final String itemName = item.split(":")[0];
            Material material;
            if(itemName.matches("[0-9][0-9]*")) {
                material = Material.getMaterial(Integer.parseInt(itemName));
            } else {
                material = Material.matchMaterial(itemName);
            }
            if(material == null) {
                itemStack = null;
            } else {
                itemStack = new ItemStack(material, amountI, (short) 0, Byte.parseByte(item.split(":")[1]));
            }
        } else {
            Material material;
            if(item.matches("[0-9][0-9]*")) {
                material = Material.getMaterial(Integer.parseInt(item));
            } else {
                material = Material.matchMaterial(item);
            }
            if(material == null) {
                final UDSItem myItem = UDSPlugin.getItems().get(item);
                if(myItem == null) {
                    itemStack = null;
                } else {
                    material = Material.getMaterial(myItem.getId());
                    itemStack = new ItemStack(material, amountI, (short) 0, myItem.getData());
                }
            } else {
                itemStack = new ItemStack(material, amountI);
            }
        }
        return itemStack;
    }

    /**
     * Find the name of an item, checks custom item name list.
     * @param item Item.
     * @return
     */
    public static String findBestItemName(final ItemStack item) {
        for(Map.Entry<String, UDSItem> i : UDSPlugin.getItems().entrySet()) {
            UDSItem myItem = i.getValue();
            if(item.getData().getData() == myItem.getData() && item.getTypeId() == myItem.getId()) {
                return myItem.getName();
            }
        }
        return item.getType().toString() + ":" + item.getData().getData();
    }
}

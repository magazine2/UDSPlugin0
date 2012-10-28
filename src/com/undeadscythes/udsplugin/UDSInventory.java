package com.undeadscythes.udsplugin;

import org.bukkit.inventory.ItemStack;

/**
 * Full player inventory contents.
 * @author UndeadScythes
 */
public class UDSInventory {
    private final ItemStack[] inventory;
    private final ItemStack[] armor;

    /**
     *
     * @param inventory Contents of regular player inventory.
     * @param armor Contents of the four armor slots.
     */
    public UDSInventory(final ItemStack[] inventory, final ItemStack[] armor) {
        this.inventory = inventory.clone();
        this.armor = armor.clone();
    }

    /**
     *
     * @return Contents of regular player inventory.
     */
    public final ItemStack[] getInventory() {
        return inventory.clone();
    }

    /**
     *
     * @return Contents of the four armor slots.
     */
    public final ItemStack[] getArmor() {
        return armor.clone();
    }
}

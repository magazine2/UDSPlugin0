package com.undeadscythes.udsplugin;

import org.bukkit.block.Block;

/**
 * Stores basic data about a block.
 * @author UndeadScythes
 */
public class UDSBlock {
    private int id;
    private byte data;

    /**
     *
     * @param block Block.
     */
    public UDSBlock(final Block block) {
        this.id = block.getTypeId();
        this.data = block.getData();
    }

    /**
     *
     * @return ID of the block.
     */
    public final int getId() {
        return this.id;
    }

    /**
     *
     * @return Data value of the block.
     */
    public final byte getData() {
        return this.data;
    }
}

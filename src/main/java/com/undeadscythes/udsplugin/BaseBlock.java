package com.undeadscythes.udsplugin;

import org.bukkit.block.Block;

/**
 *
 * @author Dave
 */
public class BaseBlock {
    private int type;
    private byte data;

    public BaseBlock (Block block) {
        type = block.getTypeId();
        data = block.getData();
    }

    public int getType() {return type;}
    public byte getData() {return data;}
}

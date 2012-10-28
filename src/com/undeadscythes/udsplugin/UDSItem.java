package com.undeadscythes.udsplugin;

public class UDSItem {
    private String itemName;
    private int id;
    private byte data;

    public UDSItem(final String ItemName, final int id, final byte data) {
        this.itemName = ItemName;
        this.id = id;
        this.data = data;
    }

    public final String getName() {
        return itemName;
    }

    public final int getId() {
        return id;
    }

    public final byte getData() {
        return data;
    }
}

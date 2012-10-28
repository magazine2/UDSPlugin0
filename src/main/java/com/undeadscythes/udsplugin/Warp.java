package com.undeadscythes.udsplugin;

import com.undeadscythes.udsplugin.utilities.LocationUtils;
import java.util.Locale;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;

public class Warp {
    private String rank;
    private Location location;
    private String name;

    private static String[] fields = {"rank", "location", "name"};

    public Warp(final String rank, final Location location, final String name) {
        this.rank = rank;
        this.location = location;
        this.name = name;
    }

    public Warp(String[] record) {
        rank = record[ArrayUtils.indexOf(fields, "rank")].toLowerCase(Locale.ENGLISH);
        location = LocationUtils.parseLocation(record[ArrayUtils.indexOf(fields, "location")]);
        name = record[ArrayUtils.indexOf(fields, "name")];
    }

    public String getRecord() {
        return rank + "\t" +
               LocationUtils.getString(location) + "\t" +
               name;
    }

    public static String[] getFields() {return fields;}
    public final String getName() {return name;}
    public final String getRank() {return rank;}
    public final Location getLocation() {return location;}
}
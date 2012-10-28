package com.undeadscythes.udsplugin;

import com.undeadscythes.udsplugin.utilities.LocationUtils;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;

public class Clan {
    private String name;
    private String leaderName;
    private List<String> members;
    private int kills = 0;
    private int deaths = 0;
    private Location baseWarp = null;

    private static String[] fields = {"name", "leader", "kills", "deaths", "base", "members"};

    public Clan(final String leaderName, final String name) {
        this.name = name;
        this.leaderName = leaderName;
        members = new LinkedList<String>();
        members.add(leaderName);
    }

    public Clan(String[] record) {
        name = record[ArrayUtils.indexOf(fields, "name")];
        leaderName = record[ArrayUtils.indexOf(fields, "leader")];
        kills = Integer.parseInt(record[ArrayUtils.indexOf(fields, "kills")]);
        deaths = Integer.parseInt(record[ArrayUtils.indexOf(fields, "deaths")]);
        baseWarp = LocationUtils.parseLocation(record[ArrayUtils.indexOf(fields, "base")]);
        members = new LinkedList<String>(Arrays.asList(record[ArrayUtils.indexOf(fields, "members")].split(",")));
    }

    public String getRecord() {
        return name + "\t" +
               leaderName + "\t" +
               kills + "\t" +
               deaths + "\t" +
               LocationUtils.getString(baseWarp) + "\t" +
               StringUtils.join(members.toArray(), ",");
    }

    public final void addMember(final String member) {
        this.members.add(member);
    }

    public final void removeMember(final String member) {
        members.remove(member);
    }

    public final void addKill() {
        this.kills++;
    }

    public final void addDeath() {
        this.deaths++;
    }

    public final double getKDR() {
        if(this.deaths == 0) {
            return this.kills;
        }
        return ((double) this.kills) / ((double) this.deaths);
    }

    public final Location getBase() {
        return baseWarp == null ? null : baseWarp;
    }

    public final String getLeader() {return leaderName;}
    public final List<String> getMembers() {return members;}
    public final void setName(final String name) {this.name = name;}
    public final String getName() {return name;}
    public final void setLeader(final String leaderName) {this.leaderName = leaderName;}
    public final int getKills() {return this.kills;}
    public final int getDeaths() {return this.deaths;}
    public final void setBase(final Location location) {baseWarp = location;}
}

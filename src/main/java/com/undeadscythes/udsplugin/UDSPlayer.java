package com.undeadscythes.udsplugin;

import com.undeadscythes.udsplugin.utilities.ChatUtils;
import com.undeadscythes.udsplugin.utilities.ClanUtils;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class UDSPlayer {
    private String name;
    private String nick = "";
    private boolean hasGodMode;
    private Location home;
    private boolean hasLockdownPass;
    private long jailTime;
    private long sentence;
    private long bail;
    private long vipTime;
    private int vipSpawns;
    private int bounty;
    private String challenger = "";
    private int wager;
    private boolean challengeWasDraw;
    private String clan = "";
    private Vector editPoint1;
    private Vector editPoint2;
    private String lastChat = "";
    private int money;
    private boolean hiddenRank;
    private LinkedList<String> ignoreList = new LinkedList();
    private long lastLogOn;
    private long lastLogOff;
    private long timeLogged;
    private Location backPoint;
    private String powertool = "";
    private int tool;
    private UUID selectedPet;
    private long lastAttack;
    private long[] mobKills = new long[5];
    private long[] chatTimes = new long[5];
    private String chatChannel = "norm";
    private Location checkPoint;
    private boolean hasClaimedPrize;
    private List<UDSBlock> undoBlocks;
    private Vector undoV1;
    private Vector undoV2;

    private static String[] fields = {"name", "nick", "home", "bounty", "clan", "money", "ptime", "psentence", "fine", "vtime", "vspawns", "lastlog", "timelogged", "hidden"};

    public UDSPlayer(final String name) {
        this.name = name;
    }

    public UDSPlayer(String[] record) {
        name = record[ArrayUtils.indexOf(fields, "name")];
        nick = record[ArrayUtils.indexOf(fields, "nick")];
        if(nick.equals("null")) {
            nick = "";
        }
        home = LocationUtils.parseLocation(record[ArrayUtils.indexOf(fields, "home")]);
        bounty = Integer.parseInt(record[ArrayUtils.indexOf(fields, "bounty")]);
        clan = record[ArrayUtils.indexOf(fields, "clan")];
        if(clan.equals("null")) {
            clan = "";
        }
        money = Integer.parseInt(record[ArrayUtils.indexOf(fields, "money")]);
        jailTime = Long.parseLong(record[ArrayUtils.indexOf(fields, "ptime")]);
        sentence = Long.parseLong(record[ArrayUtils.indexOf(fields, "psentence")]);
        bail = Integer.parseInt(record[ArrayUtils.indexOf(fields, "fine")]);
        vipTime = Long.parseLong(record[ArrayUtils.indexOf(fields, "vtime")]);
        vipSpawns = Integer.parseInt(record[ArrayUtils.indexOf(fields, "vspawns")]);
        lastLogOff = Long.parseLong(record[ArrayUtils.indexOf(fields, "lastlog")]);
        timeLogged = Long.parseLong(record[ArrayUtils.indexOf(fields, "timelogged")]);
        hiddenRank = Boolean.parseBoolean(record[ArrayUtils.indexOf(fields, "hidden")]);
    }

    public String getRecord() {
        return name + "\t" +
                nick + "\t" +
                LocationUtils.getString(home) + "\t" +
                bounty + "\t" +
                clan + "\t" +
                money + "\t" +
                jailTime + "\t" +
                sentence + "\t" +
                bail + "\t" +
                vipTime + "\t" +
                vipSpawns + "\t" +
                lastLogOff + "\t" +
                timeLogged + "\t" +
                hiddenRank;
    }

    public final void saveUndo(final Vector v1, final Vector v2, final LinkedList<UDSBlock> undoBlocks) {
        this.undoV1 = v1;
        this.undoV2 = v2;
        this.undoBlocks = undoBlocks;
    }

    public final void clearUndo() {
        this.undoV1 = null;
        this.undoV2 = null;
        this.undoBlocks.clear();
    }

    public final boolean hasUndo() {
        return (undoBlocks.isEmpty()) ? false : true;
    }

    public final boolean hasNick() {
        if(nick.equals("")) {
            return false;
        }
        return true;
    }

    public final String getNick() {
        if(!nick.equals("")) {
            return nick;
        }
        else {
            return name;
        }
    }

    public final Location getHome() {
        if(home == null) {
            return null;
        }
        return home;
    }

    public final void setHome(final Location location) {
        this.home = location;
    }

    public final void delHome() {
        this.home = null;
    }


    public final void release() {
        this.jailTime = 0;
        this.sentence = 0;
        this.bail = 0;
    }

    public final void imprison(final long time, final long sentence, final long fine) {
        this.jailTime = time;
        this.sentence = sentence;
        this.bail = fine;
    }

    public final boolean isInPrison() {
        if(jailTime == 0) {
            return false;
        }
        return true;
    }

    public final void newVip(final long newVipTime, final int newVipSpawns) {
        this.vipTime = newVipTime;
        this.vipSpawns = newVipSpawns;
    }

    public final void reduceVipTime(final long time) {
        this.vipTime -= time;
    }

    public final void reduceVipSpawns(final int amount) {
        this.vipSpawns -= amount;
    }

    public final void removeVip() {
        this.vipTime = 0;
        this.vipSpawns = 0;
    }

    public final void newBounty(final long newReward) {
        this.bounty += newReward;
    }

    public final void claimReward() {
        bounty = 0;
    }

    public final boolean hasBounty() {
        return (bounty > 0);
    }

    public final boolean hasChallenge() {
        if("".equals(challenger)) {
            return false;
        }
        return true;
    }

    public final void newChallenge(final String newTarget, final int newWager) {
        this.challenger = newTarget;
        this.wager = newWager;
    }

    public final void endChallenge() {
        this.challenger = "";
        this.wager = 0;
    }

    public final void setChallengeWasDraw(final boolean draw) {
        this.challengeWasDraw = draw;
    }

    public final boolean wasChallengeDraw() {
        return challengeWasDraw;
    }

    public final boolean isInClan() {
        if("".equals(clan)) {
            return false;
        }
        return true;
    }

    public final void leaveClan() {
        Clan realClan = ClanUtils.get(clan);
        realClan.removeMember(name);
        Player sender = PlayerUtils.getOnline(name);
        sender.sendMessage(Color.MESSAGE + "You have left clan " + clan + ".");
        if(realClan.getMembers().isEmpty()) {
            ClanUtils.remove(clan);
            RegionUtils.remove(clan + "base");
        } else {
            ChatUtils.sendClanBroadcast(realClan, name + " has left the clan.");
            if(realClan.getLeader().equalsIgnoreCase(name)) {
                realClan.setLeader(realClan.getMembers().get(0));
                ChatUtils.sendClanBroadcast(realClan, realClan.getLeader() + " is now the leader of the clan.");
                if(RegionUtils.get(realClan.getName() + "base") != null) {
                    RegionUtils.get(realClan.getName() + "base").setOwner(realClan.getLeader());
                }
            }
        }
        clan = "";
    }

    public final boolean hasEnough(final long needs) {
        if(money >= needs) {
            return true;
        }
        return false;
    }

    public final void addMoney(final long amount) {
        this.money += amount;
    }

    public final void takeMoney(final long amount) {
        this.money -= amount;
    }

    public final Location getBackPoint() {
        if(backPoint == null) {
            return null;
        }
        return backPoint;
    }

    public final void setBackPoint(final Location location) {
        this.backPoint = location;
    }

    public final Location getCheckPoint() {
        if(checkPoint == null) {
            return null;
        }
        return checkPoint;
    }

    public final void setCheckPoint(final Location location) {
        this.checkPoint = location;
    }

    public final void newChatTime(final long time) {
        if(chatTimes == null) {
            chatTimes = new long[5];
        }
        System.arraycopy(chatTimes, 1, chatTimes, 0, 4);
        chatTimes[4] = time;
    }

    public final long getChatPerSec() {
        long diff = (chatTimes[4] - chatTimes[0]) / 1000;
        return diff / 5;
    }

    public final void newMobKill(final long time) {
        if(mobKills == null) {
            mobKills = new long[5];
        }                    //First run check
        System.arraycopy(mobKills, 1, mobKills, 0, 4);
        mobKills[4] = time;
    }

    public final double getRewardRatio() {
        double ratio = 1;
        final long timeNow = System.currentTimeMillis();
        int ticks = 1;
        for(int i = 4; i >= 0; i--) {
            if(mobKills[i] > timeNow - 30000) {
                ratio *= 1.0 - (((double) (ticks + 1)) / 10);
                ticks++;
            } else {
                break;
            }
        }
        return ratio;
    }

    public final void logOff(final long time) {
        lastLogOff = time;
        timeLogged += lastLogOff - lastLogOn;
    }

    public final void setPowertool(final String command) {
        powertool = command;
    }

    public void ignore(final String player) {
        if(!ignoreList.contains(player)) {
            ignoreList.add(player);
        }
    }

    public void unIgnore(final String player) {
        if(ignoreList.contains(player)) {
            ignoreList.remove(player);
        }
    }

    public void claimPrize() {
        hasClaimedPrize = true;
    }

    public void clearPrize() {
        hasClaimedPrize = false;
    }

    public final boolean toggleRank ()
    {
        hiddenRank ^= true;
        return hiddenRank;
    }

    public int getTool() {return tool;}
    public void setTool(int tool) {this.tool = tool;}
    public static String[] getFields() {return fields;}
    public final boolean hiddenRank() {return hiddenRank;}
    public final Vector getUndoV1() {return undoV1;}
    public final Vector getUndoV2() {return undoV2;}
    public final List<UDSBlock> getUndoBlocks() {return undoBlocks;}
    public final String getName() {return name;}
    public final void setNick(final String newNick) {this.nick = newNick;}
    public final boolean hasGodMode() {return hasGodMode;}
    public final void setGodMode(final boolean godMode) {hasGodMode = godMode;}
    public final boolean hasLockdownPass() {return hasLockdownPass;}
    public final void setLockdownPass(final boolean lockdownPass) {this.hasLockdownPass = lockdownPass;}
    public final long getPrisonTime() {return jailTime;}
    public final long getPrisonSentence() {return sentence;}
    public final long getFine() {return bail;}
    public final long getVipTime() {return vipTime;}
    public final int getVipSpawns() {return vipSpawns;}
    public final void setVipSpawns(final int amount) {this.vipSpawns = amount;}
    public final int getBounty() {return bounty;}
    public final String getChallenger() {return challenger;}
    public final int getWager() {return wager;}
    public final String getClan() {return clan;}
    public final void joinClan(final String newClan) {this.clan = newClan;}
    public final String getLastChat() {return lastChat;}
    public final void setLastChat(final String last) {this.lastChat = last;}
    public final int getMoney() {return money;}
    public final void setMoney(final int amount) {this.money = amount;}
    public final void setEditPoint1(final Vector vector) {this.editPoint1 = vector;}
    public final void setEditPoint2(final Vector vector) {this.editPoint2 = vector;}
    public final Vector getEditPoint1() {return editPoint1;}
    public final Vector getEditPoint2() {return editPoint2;}
    public final void setChatChannel(final String channel) {chatChannel = channel;}
    public final String getChatChannel() {return chatChannel;}
    public final void newAttack(final long time) {lastAttack = time;}
    public final long getLastAttack() {return lastAttack;}
    public final void logOn(final long time) {lastLogOn = time;}
    public final long getLastLogOff() {return lastLogOff;}
    public final long getTimeLogged() {return timeLogged;}
    public final void setPetId(final UUID petId) {this.selectedPet = petId;}
    public final UUID getPetId() {return selectedPet;}
    public final String getPowertool() {return powertool;}
    public LinkedList<String> getIgnores() {return ignoreList;}
    public boolean hasClaimedPrize() {return hasClaimedPrize;}
}

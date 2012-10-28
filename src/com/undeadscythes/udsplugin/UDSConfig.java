package com.undeadscythes.udsplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Storage of plugin config.yml file for easier refactoring of entries.
 * @author UndeadScythe
 */
public class UDSConfig {
    FileConfiguration config;
    private long autosaveTimer;
    private boolean blockCreeper;
    private boolean blockGhast;
    private boolean blockTNT;
    private boolean chatBrackets;
    private int baseCost;
    private int promotionCost;
    private int cityCost;
    private int clanCost;
    private int expansionCost;
    private int homeCost;
    private int diamondKitCost;
    private int ironKitCost;
    private int stoneKitCost;
    private int mapCost;
    private int shopCost;
    private int vipCost;
    private String currencies;
    private String currency;
    private long day;
    private Material giftItem;
    private String info;
    private List<Material> itemWhiteList;
    private short map;
    private int compassRange;
    private int butcherRange;
    private int editRange;
    private int drainRange;
    private int pvpTimer;
    private int radiusBounce;
    private List<String> ranks;
    private long requestTimeout;
    private List<String> rules;
    private String serverOwner;
    private double spawnPitch;
    private double spawnYaw;
    private int vipSpawns;
    private long vipTime;
    private String normalWelcome;
    private String adminWelcome;
    private String worldName;
    private int worldBorder;

    public UDSConfig(FileConfiguration config) {
        load(config);
    }

    public final void load(FileConfiguration config) {
        this.config = config;
        autosaveTimer = config.getLong("autosave-timer");
        blockCreeper = config.getBoolean("block.creeper");
        blockGhast = config.getBoolean("block.ghast");
        blockTNT = config.getBoolean("block.tnt");
        chatBrackets = config.getBoolean("chat-brackets");
        baseCost = config.getInt("cost.base");
        promotionCost = config.getInt("cost.build");
        cityCost = config.getInt("cost.city");
        clanCost = config.getInt("cost.clan");
        expansionCost = config.getInt("cost.expand");
        homeCost = config.getInt("cost.home");
        diamondKitCost = config.getInt("cost.kit.diamond");
        ironKitCost = config.getInt("cost.kit.iron");
        stoneKitCost = config.getInt("cost.kit.stone");
        mapCost = config.getInt("cost.map");
        shopCost = config.getInt("cost.shop");
        vipCost = config.getInt("cost.vip");
        currencies = config.getString("currency.plural");
        currency = config.getString("currency.singular");
        day = config.getLong("day");
        giftItem = Material.getMaterial(config.getString("gift-item"));
        info = config.getString("info");
        itemWhiteList = new ArrayList<Material>();
        for(int i : config.getIntegerList("itemwhitelist")) {
            itemWhiteList.add(Material.getMaterial(i));
        }
        map = (short)config.getInt("map");
        compassRange = config.getInt("range.compass");
        butcherRange = config.getInt("range.butcher");
        editRange = config.getInt("range.edit");
        drainRange = config.getInt("range.drain");
        pvpTimer = config.getInt("range.pvptp");
        radiusBounce = config.getInt("range.move");
        ranks = config.getStringList("ranks");
        requestTimeout = config.getLong("request-timeout");
        rules = config.getStringList("rules");
        serverOwner = config.getString("server-owner");
        spawnPitch = config.getDouble("spawn.pitch");
        spawnYaw = config.getDouble("spawn.yaw");
        vipSpawns = config.getInt("vip.spawns");
        vipTime = config.getLong("vip.time");
        normalWelcome = config.getString("welcome.norm");
        adminWelcome = config.getString("welcome.admin");
        worldName = config.getString("world.name");
        worldBorder = config.getInt("world.border");
    }

    public void save() {
        config.set("day", day);
        config.set("spawn.pitch", spawnPitch);
        config.set("spawn.yaw", spawnYaw);
        try {
            config.save(UDSPlugin.getMainDir() + File.separator + "config.yml");
        } catch (IOException ex) {
            Bukkit.getLogger().info(ex.getMessage());
        }
    }

    public long getAutosaveTimer() {return autosaveTimer;}
    public boolean blockCreeper() {return blockCreeper;}
    public boolean blockGhast() {return blockGhast;}
    public boolean blockTNT() {return blockTNT;}
    public boolean useChatBrackets() {return chatBrackets;}
    public int getBaseCost() {return baseCost;}
    public int getPromotionCost() {return promotionCost;}
    public int getCityCost() {return cityCost;}
    public int getClanCost() {return clanCost;}
    public int getExpansionCost() {return expansionCost;}
    public int getHomeCost() {return homeCost;}
    public int getMapCost() {return mapCost;}
    public int getShopCost() {return shopCost;}
    public int getVIPCost() {return vipCost;}
    public String getCurrencies() {return currencies;}
    public String getCurrency() {return currency;}
    public long getDay() {return day;}
    public void setDay(long day) {this.day = day;}
    public Material getGiftItem() {return giftItem;}
    public String getInfo() {return info;}
    public List<Material> getItemWhiteList() {return itemWhiteList;}
    public int getDiamondKitCost() {return diamondKitCost;}
    public int getIronKitCost() {return ironKitCost;}
    public int getStoneKitCost() {return stoneKitCost;}
    public short getMap() {return map;}
    public int getButcherRange() {return butcherRange;}
    public int getCompassRange() {return compassRange;}
    public int getDrainRange() {return drainRange;}
    public int getEditRange() {return editRange;}
    public int getRadiusBounce() {return radiusBounce;}
    public int getPVPTimer() {return pvpTimer;}
    public List<String> getRanks() {return ranks;}
    public long getRequestTimeout() {return requestTimeout;}
    public List<String> getRules() {return rules;}
    public String getServerOwner() {return serverOwner;}
    public double getSpawnPitch() {return spawnPitch;}
    public void setSpawnPitch(double pitch) {spawnPitch = pitch;}
    public double getSpawnYaw() {return spawnYaw;}
    public void setSpawnYaw(double yaw) {spawnYaw = yaw;}
    public int getVipSpawns() {return vipSpawns;}
    public long getVipTime() {return vipTime;}
    public String getAdminWelcome() {return adminWelcome;}
    public String getNormalWelcome() {return normalWelcome;}
    public int getWorldBorder() {return worldBorder;}
    public String getWorldName() {return worldName;}
}
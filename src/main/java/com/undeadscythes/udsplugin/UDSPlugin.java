package com.undeadscythes.udsplugin;

import com.undeadscythes.udsplugin.utilities.InitUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import com.undeadscythes.udsplugin.utilities.UDSIOUtils;
import java.io.File;
import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class.
 * @author UndeadScythes
 */
public class UDSPlugin extends JavaPlugin {
    private static UDSHashMap<ChatRoom> chatRooms;
    private static UDSHashMap<Clan> clans;
    private static UDSHashMap<UDSInventory> inventories;
    private static UDSHashMap<UDSItem> items;
    private static UDSHashMap<UDSPlayer> players;
    private static UDSHashMap<Region> regions;
    private static UDSHashMap<Request> requests;
    private static UDSHashMap<Warp> warps;
    private static UDSHashMap<WESession> weSessions;
    private static LinkedList<HelpFile> helpFiles;
    private static UDSConfig udsConfig;
    private static Timer udsTimer;
    private static String mainDir = "plugins" + File.separator + "UDSPlugin";
    private static String dataDir = "plugins" + File.separator + "UDSPlugin" + File.separator + "data";
    private static boolean serverInLockdown = false;
    private static long startTime = System.currentTimeMillis();

    /**
     * Used for testing in NetBeans.
     * @param args
     */
    public static void main(final String[] args) {}

    /**
     * Initialize the plugin objects and load data from files.
     */
    @Override
    public final void onEnable() {
        final PluginDescriptionFile plugin = this.getDescription();
        if(new File(mainDir).mkdir()) {
            getLogger().info("Root directory created.");
        }
        if(new File(dataDir).mkdir()) {
            getLogger().info("Data directory created.");
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
        udsConfig = new UDSConfig(getConfig());
        getLogger().info("Config loaded.");
        chatRooms = new UDSHashMap<ChatRoom>();
        clans = new UDSHashMap<Clan>();
        inventories = new UDSHashMap<UDSInventory>();
        items = new UDSHashMap<UDSItem>();
        regions = new UDSHashMap<Region>();
        players = new UDSHashMap<UDSPlayer>();
        warps = new UDSHashMap<Warp>();
        weSessions = new UDSHashMap<WESession>();
        requests = new UDSHashMap<Request>();
        helpFiles = new LinkedList<HelpFile>();
        UDSIOUtils.loadClans(clans, this);
        UDSIOUtils.loadRegions(regions, this);
        RegionUtils.streamlineRegions();
        UDSIOUtils.loadPlayers(players, this);
        UDSIOUtils.loadWarps(warps, this);
        udsTimer = new Timer(this, 100);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, udsTimer, 100, 100);
        getLogger().info("Timer started.");
        InitUtils.initHelpFiles(helpFiles, getConfig());
        InitUtils.initItemNames(items);
        InitUtils.setExecutors(this, getLogger());
        InitUtils.registerEvents(this, getServer().getPluginManager(), getLogger());
        InitUtils.addRecipes(getServer(), getLogger());
        final String logMessage = plugin.getName() + " version " + plugin.getVersion() + " enabled.";
        getLogger().info(logMessage);
    }

    /**
     * Free objects and save data to files.
     */
    @Override
    public final void onDisable() {
        UDSIOUtils.saveClans(clans, this);
        UDSIOUtils.saveRegions(regions, this);
        UDSIOUtils.savePlayers(players, this);
        UDSIOUtils.saveWarps(warps, this);
        chatRooms.clear();
        clans.clear();
        regions.clear();
        players.clear();
        warps.clear();
        final String logMessage = this.getDescription().getName() + " disabled.";
        getLogger().info(logMessage);
    }

    public static UDSHashMap<ChatRoom> getChatRooms() {return chatRooms;}
    public static UDSHashMap<Clan> getClans() {return clans;}
    public static UDSHashMap<UDSInventory> getInventories() {return inventories;}
    public static UDSHashMap<UDSItem> getItems() {return items;}
    public static UDSHashMap<UDSPlayer> getPlayers() {return players;}
    public static UDSHashMap<Region> getRegions() {return regions;}
    public static UDSHashMap<Request> getRequests() {return requests;}
    public static UDSHashMap<Warp> getWarps() {return warps;}
    public static UDSHashMap<WESession> getWESessions() {return weSessions;}
    public static LinkedList<HelpFile> getHelpFiles() {return helpFiles;}
    public static UDSConfig getUDSConfig() {return udsConfig;}
    public static String getMainDir() {return mainDir;}
    public static boolean getServerInLockdown() {return serverInLockdown;}
    public static void setServerInLockdown(final boolean lockdown) {serverInLockdown = lockdown;}
    public long getServerStart() {return startTime;}
}

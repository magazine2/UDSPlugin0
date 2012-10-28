package com.undeadscythes.udsplugin.utilities;

import com.undeadscythes.udsplugin.Clan;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Warp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Collection of functions for loading and saving all data objects.
 * @author UndeadScythes
 */
public final class UDSIOUtils {
    private static final String DATA_DIR = UDSPlugin.getMainDir() + File.separator + "data" + File.separator;

    private UDSIOUtils() {}

    public static void savePlayers(final UDSHashMap<UDSPlayer> players, final UDSPlugin plugin) {
        File path = new File(DATA_DIR + "players.csv");
        try {
            int count = 0;
            FileWriter file = new FileWriter(path.getPath());
            BufferedWriter writer = new BufferedWriter(file);
            for(UDSPlayer player : players.values()) {
                writer.write(player.getRecord());
                writer.newLine();
                count++;
            }
            writer.close();
            String logMessage = count + " players saved.";
            plugin.getLogger().info(logMessage);
        } catch (Exception e) {
            plugin.getLogger().info(e.toString());
        }
    }

    public static void saveRegions(final UDSHashMap<Region> regions, final UDSPlugin plugin) {
        File path = new File(DATA_DIR + "regions.csv");
        try {
            int count = 0;
            FileWriter file = new FileWriter(path.getPath());
            BufferedWriter writer = new BufferedWriter(file);
            for(Region region : regions.values()) {
                writer.write(region.getRecord());
                writer.newLine();
                count++;
            }
            writer.close();
            String logMessage = count + " regions saved.";
            plugin.getLogger().info(logMessage);
        } catch (Exception e) {
            plugin.getLogger().info(e.getLocalizedMessage());
        }
    }

    public static void saveWarps(final UDSHashMap<Warp> warps, final UDSPlugin plugin) {
        File path = new File(DATA_DIR + "warps.csv");
        try {
            int count = 0;
            FileWriter file = new FileWriter(path.getPath());
            BufferedWriter writer = new BufferedWriter(file);
            for(Warp warp : warps.values()) {
                writer.write(warp.getRecord());
                writer.newLine();
                count++;
            }
            writer.close();
            String logMessage = count + " warps saved.";
            plugin.getLogger().info(logMessage);
        } catch (Exception e) {
            plugin.getLogger().info(e.toString());
        }
    }

    public static void saveClans(final UDSHashMap<Clan> clans, final UDSPlugin plugin) {
        File path = new File(DATA_DIR + "clans.csv");
        try {
            int count = 0;
            FileWriter file = new FileWriter(path.getPath());
            BufferedWriter writer = new BufferedWriter(file);
            for(Clan clan : clans.values()) {
                writer.write(clan.getRecord());
                writer.newLine();
                count++;
            }
            writer.close();
            String logMessage = count + " clans saved.";
            plugin.getLogger().info(logMessage);
        } catch (Exception e) {
            plugin.getLogger().info(e.toString());
        }
    }

    /**
     * Custom file loader to load server players from file.
     * @param players
     * @param plugin
     */
    public static void loadPlayers(final UDSHashMap<UDSPlayer> players, final UDSPlugin plugin) {
        final File path = new File(DATA_DIR + "players.csv");
        try {
            int count = 0;
            final FileReader file = new FileReader(path.getPath());
            final BufferedReader reader = new BufferedReader(file);
            String nextLine;
            while((nextLine = reader.readLine()) != null) {
                UDSPlayer player = new UDSPlayer(nextLine.split("\t"));
                players.put(player.getName(), player);
                count++;
            }
            reader.close();
            String logMessage = count + " players loaded.";
            plugin.getLogger().info(logMessage);
        } catch (Exception e) {
            if(e instanceof FileNotFoundException) {
                plugin.getLogger().info("No players to load");
            }
            else {
                plugin.getLogger().info(e.toString());
            }
        }
    }

    /**
     * Custom file loader to load regions from file.
     * @param regions
     * @param plugin
     */
    public static void loadRegions(final UDSHashMap<Region> regions, final UDSPlugin plugin) {
        File path = new File(DATA_DIR + "regions.csv");
        try {
            int count = 0;
            final FileReader file = new FileReader(path.getPath());
            final BufferedReader reader = new BufferedReader(file);
            String nextLine;
            while((nextLine = reader.readLine()) != null) {
                Region region = new Region(nextLine.split("\t"));
                regions.put(region.getName(), region);
                count++;
            }
            reader.close();
            String logMessage = count + " regions loaded.";
            plugin.getLogger().info(logMessage);
        } catch (Exception e) {
            if(e instanceof FileNotFoundException) {
                plugin.getLogger().info("No regions to load");
            } else {
                plugin.getLogger().info(e.toString());
            }
        }
    }

    /**
     * Custom file loader to load warp from file.
     * @param warps
     * @param plugin
     */
    public static void loadWarps(final UDSHashMap<Warp> warps, final UDSPlugin plugin) {
        File path = new File(DATA_DIR + "warps.csv");
        try {
            int count = 0;
            final FileReader file = new FileReader(path.getPath());
            final BufferedReader reader = new BufferedReader(file);
            String nextLine;
            while((nextLine = reader.readLine()) != null) {
                Warp warp = new Warp(nextLine.split("\t"));
                warps.put(warp.getName(), warp);
                count++;
            }
            reader.close();
            String logMessage = count + " warps loaded.";
            plugin.getLogger().info(logMessage);
        } catch (Exception e) {
            if(e instanceof FileNotFoundException) {
                plugin.getLogger().info("No warps to load");
            }
            else {
                plugin.getLogger().info(e.toString());
            }
        }
    }

    /**
     * Custom file loader to load clans from file.
     * @param clans
     * @param plugin
     */
    public static void loadClans(final UDSHashMap<Clan> clans, final UDSPlugin plugin) {
        final File path = new File(DATA_DIR + "clans.csv");
        try {
            int count = 0;
            final FileReader file = new FileReader(path.getPath());
            final BufferedReader reader = new BufferedReader(file);
            String nextLine;
            while((nextLine = reader.readLine()) != null) {
                Clan clan = new Clan(nextLine.split("\t"));
                clans.put(clan.getName(), clan);
                count++;
            }
            reader.close();
            String logMessage = count + " clans loaded.";
            plugin.getLogger().info(logMessage);
        } catch (Exception e) {
            if(e instanceof FileNotFoundException) {
                plugin.getLogger().info("No clans to load");
            }
            else {
                plugin.getLogger().info(e.toString());
            }
        }
    }
}

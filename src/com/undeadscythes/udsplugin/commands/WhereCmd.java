package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WhereCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;
    private final static int SCAN_AREA = 30;

    public WhereCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);
        if(sender.hasPermission("udsplugin.where")) {
            final World world = server.getWorld(UDSPlugin.getUDSConfig().getWorldName());
            final Location playerLocation = sender.getLocation();
            final ChatColor messageColor = Color.MESSAGE;
            final double playerX = playerLocation.getX();
            final double playerY = playerLocation.getY();
            final double playerZ = playerLocation.getZ();
            final double spawnX = world.getSpawnLocation().getX();
            final double spawnZ = world.getSpawnLocation().getZ();
            final int distanceFromSpawn = (int) (Math.sqrt((playerX - spawnX) * (playerX - spawnX) + (playerZ - spawnZ) * (playerZ - spawnZ)));
            sender.sendMessage(messageColor + "Your coordinates are X:" + (int) playerX + " Z:" + (int) playerZ + ".");
            sender.sendMessage(messageColor + "You are " + distanceFromSpawn + " blocks from the spawn point.");
            if(playerY > 64) {
                sender.sendMessage(messageColor + "You are " + (int) (playerY - 64) + " blocks above sea level.");
            } else if(playerY < 64) {
                sender.sendMessage(messageColor + "You are " + (int) (64 - playerY) + " blocks below sea level.");
            } else {
                sender.sendMessage(messageColor + "You are at sea level.");
            }
            sender.sendMessage(messageColor + "You are in a " + playerLocation.getBlock().getBiome().toString().toLowerCase() + " biome.");
            final Region region = new Region("test", new Vector(playerX + SCAN_AREA, playerY + SCAN_AREA, playerZ + SCAN_AREA), new Vector(playerX - SCAN_AREA, playerY - SCAN_AREA, playerZ - SCAN_AREA), sender.getLocation(), "");
            final UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(region);
            if(!testRegions.isEmpty()) {
                String inRegion = "";
                final UDSHashMap<Region> inRegions = RegionUtils.findRegionsHere(playerLocation);
                if(!inRegions.isEmpty()) {
                    for(Map.Entry<String, Region> i : inRegions.entrySet()) {
                        if(i.getKey().endsWith("home")) {
                            sender.sendMessage(messageColor + "You are in " + i.getValue().getName().replaceFirst("home", "") + "'s home.");
                            inRegion = i.getKey();
                        }
                    }
                }
                boolean needToPrint = true;
                for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                    if(i.getKey().endsWith("home") && !i.getKey().equals(inRegion)) {
                        if(needToPrint) {
                            sender.sendMessage(messageColor + "You are near:");
                            needToPrint = false;
                        }
                        sender.sendMessage(Color.TEXT + "- " + i.getValue().getName().replaceFirst("home", "") + "'s home");
                    }
                }
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

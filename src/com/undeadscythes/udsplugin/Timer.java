package com.undeadscythes.udsplugin;

import com.undeadscythes.udsplugin.utilities.ItemUtils;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.UDSIOUtils;
import com.undeadscythes.udsplugin.utilities.VectorUtils;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Timer implements Runnable {
    private final transient UDSPlugin plugin;
    private final transient int interval;
    private final transient UDSConfig config;
    private transient long day;
    private transient long longTime;

    public Timer(final UDSPlugin plugin, final int interval) {
        this.plugin = plugin;
        this.interval = interval;
        this.config = UDSPlugin.getUDSConfig();
        this.day = config.getDay();
        this.longTime = System.currentTimeMillis();
    }

    @Override
    public final void run() {
        this.update(this.interval);
    }

    public final void update(final int lastInterval) {
        final long currentTime = System.currentTimeMillis();
        if(day + 86400000 < currentTime) {
            dailyTask(currentTime);
        }
        fastTask(currentTime, lastInterval);
        if(longTime + config.getAutosaveTimer() < currentTime) {
            autoSave();
        }
    }

    private void fastTask(final long currentTime, final int lastInterval) {
        final Player[] players = plugin.getServer().getOnlinePlayers();
        for (int i = 0; i < players.length; i++) {
            UDSPlayerTask(UDSPlugin.getPlayers().get(players[i].getName()), currentTime, lastInterval);
            playerTask(players[i]);
        }
        timeoutRequests();
    }

    private void autoSave() {
        longTime = System.currentTimeMillis();
        UDSIOUtils.saveClans(UDSPlugin.getClans(), plugin);
        UDSIOUtils.saveRegions(UDSPlugin.getRegions(), plugin);
        UDSIOUtils.savePlayers(UDSPlugin.getPlayers(), plugin);
        UDSIOUtils.saveWarps(UDSPlugin.getWarps(), plugin);
    }

    private void dailyTask(final long currentTime) {
        day = currentTime;
        config.setDay(currentTime);
        config.save();

        ItemStack[] arrows = new ItemStack[9];
        for(int i = 0; i < 9; i++) {
            arrows[i] = new ItemStack(Material.ARROW, 64);
        }
        for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
            if(i.getKey().contains("quarry:")) {
                final ItemStack item = ItemUtils.findItem(i.getKey().replace("quarry:", ""));
                final int matId = item.getTypeId();
                final Byte matData = item.getData().getData();
                for(int ix = (int)i.getValue().getMinVector().getX(); ix <= (int)i.getValue().getMaxVector().getX(); ix++) {
                    for(int iy = (int)i.getValue().getMinVector().getY(); iy <= (int)i.getValue().getMaxVector().getY(); iy++) {
                        for(int iz = (int)i.getValue().getMinVector().getZ(); iz <= (int)i.getValue().getMaxVector().getZ(); iz++) {
                            if(i.getValue().getWorld().getBlockAt(ix, iy, iz).getType() != Material.BEDROCK) {
                                i.getValue().getWorld().getBlockAt(ix, iy, iz).setTypeIdAndData(matId, matData, false);
                            }
                        }
                    }
                }
            }
            if(i.getKey().contains(":dispenser")) {
                final Vector myVector1 = i.getValue().getMinVector();
                final Vector myVector2 = i.getValue().getMaxVector();
                final Vector min = VectorUtils.findMin(myVector1, myVector2);
                final Vector max = VectorUtils.findMax(myVector1, myVector2);
                for(int ix = (int)min.getX(); ix <= (int)max.getX(); ix++) {
                    for(int iy = (int)min.getY(); iy <= (int)max.getY(); iy++) {
                        for(int iz = (int)min.getZ(); iz <= (int)max.getZ(); iz++) {
                            final Block block = i.getValue().getWorld().getBlockAt(ix, iy, iz);
                            if(block.getType() == Material.DISPENSER) {
                                final Dispenser dispenser = (Dispenser)block.getState();
                                dispenser.getInventory().setContents(arrows);
                            }
                        }
                    }
                }
            }
        }
        plugin.getServer().broadcastMessage(Color.BROADCAST + "Quarries have been refilled.");
        for(Map.Entry<String, UDSPlayer> i : UDSPlugin.getPlayers().entrySet()) {
            final UDSPlayer serverPlayer = i.getValue();
            if(serverPlayer.getVipTime() != 0) {
                serverPlayer.setVipSpawns(config.getVipSpawns());
            }
        }
    }

    private void UDSPlayerTask(final UDSPlayer player, final long currentTime, final int lastInterval) {
        final long vipTime = player.getVipTime();
        if(vipTime != 0 && vipTime + config.getVipTime() < currentTime && player != null) {
            final Server server = plugin.getServer();
            server.dispatchCommand(Bukkit.getConsoleSender(), "permissions player setgroup " + player.getName() + " member");
            server.getPlayer(player.getName()).sendMessage(Color.MESSAGE + "Your time as a VIP has come to an end.");
            player.removeVip();
        }
        if(player.getPrisonTime() != 0 && player.getPrisonTime() + (60000 * player.getPrisonSentence()) < currentTime && player != null) {
            final Server server = plugin.getServer();
            final Player realPlayer = server.getPlayer(player.getName());
            player.release();
            realPlayer.sendMessage(Color.MESSAGE + "You have served your time.");
            server.dispatchCommand(Bukkit.getConsoleSender(), "permissions player setgroup " + player.getName() + " member");
            player.setGodMode(false);
            final Location location = UDSPlugin.getWarps().get("jailout").getLocation();
            if(location != null) {
                realPlayer.teleport(location);
            }
            else {
                realPlayer.teleport(server.getWorld(config.getWorldName()).getSpawnLocation());
            }
        }
        if(player.hasGodMode()) {
            final Server server = plugin.getServer();
            final Player realPlayer = server.getPlayer(player.getName());
            realPlayer.setHealth(realPlayer.getMaxHealth());
            realPlayer.setFoodLevel(20);
        }
    }

    private void timeoutRequests() {
        UDSHashMap<Request> requests = UDSPlugin.getRequests();
        for(Map.Entry<String, Request> i : requests.entrySet()) {
            Request request = i.getValue();
            if(request.isTimedOut()) {
                final Player target = plugin.getServer().getPlayer(request.getSender());
                if(target != null) {
                    target.sendMessage(Color.ERROR + "Your request has timed out.");
                }
                requests.remove(i.getKey());
            }
        }
    }

    private void playerTask(final Player player) {
        final Location location = player.getLocation();
        final int border = config.getWorldBorder();
        final int x = location.getBlockX();
        final int z = location.getBlockZ();
        final double radius = Math.abs(Math.sqrt((x * x) + (z * z)));
        final double trespass = (radius - border);
        if(trespass > 10) {
            final double ratio = border / radius;
            final int newX = (int) (x * ratio);
            final int newZ = (int) (z * ratio);
            final Location destination = LocationUtils.findSafePlace(new Location(player.getWorld(), newX, location.getY(), newZ));
            destination.setPitch(location.getPitch());
            destination.setYaw(location.getYaw());
            player.teleport(destination);
            player.sendMessage(Color.ERROR + "You have reached the edge of the currently explorable world.");
        }
    }
}

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSString;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import com.undeadscythes.udsplugin.utilities.WEUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CityCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public CityCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        if(args.length == 0) {
            sender.sendMessage(UDSMessage.BAD_ARGS);
        }
        else if(args[0].equals("new")) {
            if(sender.hasPermission("udsplugin.city.new")) {
                if(args.length == 2) {
                    newCity(sender, args[1]);
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else if(args[0].equals("invite")) {
            if(sender.hasPermission("udsplugin.city.invite")) {
                if(args.length == 3) {
                    addPlayer(sender, args[1], args[2]);
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else if(args[0].equals("banish")) {
            if(sender.hasPermission("udsplugin.city.banish")) {
                if(args.length == 3) {
                    banishPlayer(sender, args[1], args[2]);
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else if(args[0].equals("leave")) {
            if(sender.hasPermission("udsplugin.city.leave")) {
                leaveCity(sender, args[1]);
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else if(args[0].equals("warp")) {
            if(sender.hasPermission("udsplugin.city.warp")) {
                if(args.length == 2) {
                    warpToCity(sender, args[1]);
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else if(args[0].equals("set")) {
            if(sender.hasPermission("udsplugin.city.set")) {
                setCityWarp(sender, args[1]);
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else if(args[0].equals("help")) {
            if(sender.hasPermission("udsplugin.help.city")) {
                sender.performCommand("help city" + StringUtils.join(ArrayUtils.subarray(args, 1, args.length - 1), " "));
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_COMMAND);
        }
        return true;
    }

    public void newCity(Player player, String name) {
        if(!(new UDSString(name).censor())) {
            int price = UDSPlugin.getUDSConfig().getCityCost();
            UDSPlayer udsPlayer = UDSPlugin.getPlayers().get(player.getName());
            if(udsPlayer.hasEnough(price)) {
                Location center = player.getLocation();
                int radius = 100;
                int x = center.getBlockX();
                int z = center.getBlockZ();
                Vector v1 = new Vector(x - radius, 0, z - radius);
                Vector v2 = new Vector(x + radius, 256, z + radius);
                Region existing = UDSPlugin.getRegions().get(name + "city");
                if(existing == null) {
                    World world = player.getWorld();
                    Region city = new Region(name + "city", v1, v2, player.getLocation(), player.getName());
                    if(RegionUtils.isInOpenArea(city)) {
                        UDSPlugin.getRegions().put(name + "city", city);
                        udsPlayer.takeMoney(price);
                        Location next = WEUtils.placeTower(player.getWorld(), Material.FENCE, x - radius, z - radius, 10);
                        world.getBlockAt(next).setType(Material.NETHERRACK);
                        world.getBlockAt(next.add(0, 1, 0)).setType(Material.FIRE);
                        next = WEUtils.placeTower(player.getWorld(), Material.FENCE, x - radius, z + radius, 10);
                        world.getBlockAt(next).setType(Material.NETHERRACK);
                        world.getBlockAt(next.add(0, 1, 0)).setType(Material.FIRE);
                        next = WEUtils.placeTower(player.getWorld(), Material.FENCE, x + radius, z - radius, 10);
                        world.getBlockAt(next).setType(Material.NETHERRACK);
                        world.getBlockAt(next.add(0, 1, 0)).setType(Material.FIRE);
                        next = WEUtils.placeTower(player.getWorld(), Material.FENCE, x + radius, z + radius, 10);
                        world.getBlockAt(next).setType(Material.NETHERRACK);
                        world.getBlockAt(next.add(0, 1, 0)).setType(Material.FIRE);
                        player.sendMessage(Color.MESSAGE + "Your city has been founded, it is 200 by 200 blocks centered where you are now.");
                        Bukkit.broadcastMessage(Color.BROADCAST + udsPlayer.getNick() + " has just founded " + name + "!");
                    } else {
                        player.sendMessage(Color.ERROR + "You are too close to another region. Cities are big, move further from spawn.");
                    }
                } else {
                    player.sendMessage(UDSMessage.BAD_REGION_EXISTS);
                }
            } else {
                player.sendMessage(UDSMessage.NO_MONEY);
            }
        } else {
            player.sendMessage(UDSMessage.BAD_STRING);
        }
    }

    public void addPlayer(Player player, String cityName, String name) {
        Player newPlayer = PlayerUtils.matchOnlinePlayer(name);
        if(newPlayer != null) {
            Region city = UDSPlugin.getRegions().get(cityName + "city");
            if(city != null) {
                if(city.getOwner().equalsIgnoreCase(player.getName())) {
                    if(!city.getMembers().contains(newPlayer.getName())) {
                        city.addMember(newPlayer.getName());
                        player.sendMessage(Color.MESSAGE + UDSPlugin.getPlayers().get(newPlayer.getName()).getNick() + " is now a resident of " + city.getName().subSequence(0, city.getName().length() - 4) + ".");
                        newPlayer.sendMessage(Color.MESSAGE + "Mayor " + UDSPlugin.getPlayers().get(player.getName()).getNick() + " has added you as a resident of " + city.getName().subSequence(0, city.getName().length() - 4) + ".");
                    } else {
                        player.sendMessage(Color.ERROR + "That players is already a resident of your city.");
                    }
                } else {
                    player.sendMessage(UDSMessage.NOT_MAYOR);
                }
            } else {
                player.sendMessage(UDSMessage.NO_CITY);
            }
        } else {
            player.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
        }
    }

    public void banishPlayer(Player mayor, String cityName, String residentNameShort) {
        Player resident = PlayerUtils.matchOnlinePlayer(residentNameShort);
        if(resident != null) {
            Region city = UDSPlugin.getRegions().get(cityName + "city");
            if(city != null) {
                String mayorName = mayor.getName();
                if(city.getOwner().equalsIgnoreCase(mayorName)) {
                    String residentName = resident.getName();
                    if(city.getMembers().contains(residentName)) {
                        city.delMember(residentName);
                        mayor.sendMessage(Color.MESSAGE + residentName + " has been banished from " + city.getName().subSequence(0, city.getName().length() - 4) + ".");
                        resident.sendMessage(Color.MESSAGE + mayorName + " has banished you from " + city.getName().subSequence(0, city.getName().length() - 4) + ".");
                        new SpawnCmd(plugin).teleportToSpawn(resident);
                        Region residentHome = UDSPlugin.getRegions().get(residentName + "home");
                        if(residentHome != null && RegionUtils.hasOverlap(residentHome, city)) {
                            UDSPlugin.getRegions().remove(residentName + "home");
                            UDSPlugin.getPlayers().get(residentName).delHome();
                            resident.sendMessage(Color.MESSAGE + "You no longer own your home in " + city.getName().subSequence(0, city.getName().length() - 4) + ".");
                        }
                    } else {
                        mayor.sendMessage(UDSMessage.PLAYER_NOT_RESIDENT);
                    }
                } else {
                    mayor.sendMessage(UDSMessage.NOT_MAYOR);
                }
            } else {
                mayor.sendMessage(UDSMessage.NO_CITY);
            }
        } else {
            mayor.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
        }
    }

    public void setCityWarp(Player mayor, String cityName) {
        Region city = UDSPlugin.getRegions().get(cityName + "city");
        if(city != null) {
            if(city.getOwner().equalsIgnoreCase(mayor.getName())) {
                Location cityWarp = mayor.getLocation();
                city.setWarp(cityWarp);
                mayor.sendMessage(Color.MESSAGE + "The warp point for " + city.getName().subSequence(0, city.getName().length() - 4) + " has been set.");
            } else {
                mayor.sendMessage(UDSMessage.NOT_MAYOR);
            }
        } else {
            mayor.sendMessage(UDSMessage.NO_CITY);
        }
    }

    public void warpToCity(Player player, String cityName) {
        Region city = UDSPlugin.getRegions().get(cityName + "city");
        if(city != null) {
            Location warp = city.getWarp();
            if(warp != null) {
                player.teleport(LocationUtils.findSafePlace(city.getWarp()));
            } else {
                player.sendMessage(UDSMessage.NO_WARP);
            }
        } else {
            player.sendMessage(UDSMessage.NO_CITY);
        }
    }

    public void leaveCity(Player resident, String cityName) {
        Region city = UDSPlugin.getRegions().get(cityName + "city");
        if(city != null) {
            String residentName = resident.getName();
            if(!city.getOwner().equalsIgnoreCase(resident.getName())) {
                if(city.getMembers().contains(residentName)) {
                    resident.sendMessage(Color.MESSAGE + "You are no longer a resident of " + city.getName().subSequence(0, city.getName().length() - 4) + ".");
                    new SpawnCmd(plugin).teleportToSpawn(resident);
                    Region residentHome = UDSPlugin.getRegions().get(residentName + "home");
                    if(residentHome != null && RegionUtils.hasOverlap(residentHome, city)) {
                        UDSPlugin.getRegions().remove(residentName + "home");
                        UDSPlugin.getPlayers().get(residentName).delHome();
                        resident.sendMessage(Color.MESSAGE + "You no longer own your home in " + city.getName().subSequence(0, city.getName().length() - 4) + ".");
                    }
                } else {
                    resident.sendMessage(UDSMessage.NOT_RESIDENT);
                }
            } else {
                resident.sendMessage(UDSMessage.ERR_MAYOR);
            }
        } else {
            resident.sendMessage(UDSMessage.NO_CITY);
        }
    }
}

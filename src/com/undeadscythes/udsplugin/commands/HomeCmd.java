package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.Request;
import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HomeCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public HomeCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        UDSConfig config = UDSPlugin.getUDSConfig();
        World world = server.getWorld(config.getWorldName());
        ChatColor colorError = Color.ERROR;
        ChatColor colorMessage = Color.MESSAGE;
        ChatColor colorText = Color.TEXT;
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        UDSPlayer senderPlayer = serverPlayers.get(senderName);
        UDSHashMap<Region> regions = UDSPlugin.getRegions();
        if(args.length != 0) {
            String commandName = args[0];
            if(commandName.equalsIgnoreCase("make")) {
                if(sender.hasPermission("udsplugin.home.make")) {
                    if(senderPlayer.hasEnough(config.getHomeCost())) {
                        final Location playerLocation = sender.getLocation();
                        int x = playerLocation.getBlockX();
                        int y = playerLocation.getBlockY();
                        int z = playerLocation.getBlockZ();
                        int homeRadius = 10;
                        int homeUp = 28;
                        int homeDown = 12;
                        Vector bv1 = new Vector(x + homeRadius, y + homeUp, z + homeRadius);
                        Vector bv2 = new Vector(x - homeRadius, y - homeDown, z - homeRadius);
                        Region existing = regions.get(senderName + "home");
                        if(existing == null) {
                            Region region = new Region(senderName + "home", bv1, bv2, sender.getLocation(), senderName);
                            if(RegionUtils.isInOpenArea(region)) {
                                regions.put(senderName + "home", region);
                                int iX = (int) x;
                                int iY = (int) y;
                                int iZ = (int) z;
                                Material fence = Material.FENCE;
                                Material torch = Material.TORCH;
                                world.getBlockAt(iX + homeRadius, iY, iZ + homeRadius).setType(fence);
                                world.getBlockAt(iX + homeRadius, iY, iZ - homeRadius).setType(fence);
                                world.getBlockAt(iX - homeRadius, iY, iZ + homeRadius).setType(fence);
                                world.getBlockAt(iX - homeRadius, iY, iZ - homeRadius).setType(fence);
                                world.getBlockAt(iX + homeRadius, iY + 1, iZ + homeRadius).setType(torch);
                                world.getBlockAt(iX + homeRadius, iY + 1, iZ - homeRadius).setType(torch);
                                world.getBlockAt(iX - homeRadius, iY + 1, iZ + homeRadius).setType(torch);
                                world.getBlockAt(iX - homeRadius, iY + 1, iZ - homeRadius).setType(torch);
                                serverPlayers.get(senderName).setHome(playerLocation);
                                sender.sendMessage(colorMessage + "Area protected 30 blocks up and 10 blocks down.");
                                senderPlayer.takeMoney(config.getHomeCost());
                            } else {
                                sender.sendMessage(colorError + "You cannot protect this area, someone else has a home nearby.");
                            }
                        } else {
                            sender.sendMessage(colorError + "You already have a home.");
                        }
                    } else {
                        sender.sendMessage(UDSMessage.NO_MONEY);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("sell") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.home.sell")) {
                    if(!regions.containsKey(senderName + "home")) {
                        sender.sendMessage(colorError + "You do not have a home to sell.");
                        return true;
                    }
                    if(args.length != 3) {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                        return true;
                    }
                    if(args[2].matches("[0-9][0-9]*")) {
                        int payment = Integer.parseInt(args[2]);
                        Player target = PlayerUtils.matchOnlinePlayer(args[1]);
                        if(target == null) {
                            sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                            return true;
                        }
                        String targetName = target.getName();
                        UDSPlayer serverPlayer = serverPlayers.get(targetName);
                        if(!UDSPlugin.getRequests().containsKey(targetName)) {
                            sender.sendMessage(colorError + targetName + " already has a pending request.");
                            return true;
                        }
                        UDSPlugin.getRequests().put(targetName, new Request(targetName, senderName, Request.Type.HOME, args[2], config.getRequestTimeout()));
                        target.sendMessage(colorMessage + "Do you want to buy " + senderName + "'s home for " + payment + " " + plugin.getConfig().getString("currency.plural") + "?");
                        target.sendMessage(colorMessage + "Use /y or /n in response.");
                        sender.sendMessage(colorMessage + "Your offer has been sent to " + targetName + ".");
                    } else {
                        sender.sendMessage(UDSMessage.NO_NUMBER);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("clear")) {
                if(sender.hasPermission("udsplugin.home.clear")) {
                    Region region = regions.get(senderName + "home");
                    if(region != null) {
                        regions.remove(senderName + "home");
                        serverPlayers.get(senderName).setHome(server.getWorld(plugin.getConfig().getString("world.name")).getSpawnLocation());
                        sender.sendMessage(colorMessage + "Home protection removed.");
                    } else {
                        sender.sendMessage(colorError + "You do not have a home.");
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("set")) {
                if(sender.hasPermission("udsplugin.home.set")) {
                    Location pLoc = sender.getLocation();
                    UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(pLoc);
                    for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                        if(i.getKey().equals(senderName.toLowerCase(Locale.ENGLISH) + "home")) {
                            serverPlayers.get(senderName).setHome(pLoc);
                            sender.sendMessage(colorMessage + "Home point moved.");
                            return true;
                        }
                    }
                    sender.sendMessage(colorError + "You can only place a home point in your home area.");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("add") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.home.add")) {
                    if(args.length == 2) {
                        final UDSPlayer target = PlayerUtils.matchUDS(args[1]);
                        if(target != null) {
                            if(regions.containsKey(senderName + "home")) {
                                final String targetName = target.getName();
                                regions.get(senderName + "home").addMember(targetName);
                                sender.sendMessage(colorMessage + target.getNick() + " has been added as a room mate.");
                                final Player player = Bukkit.getPlayerExact(targetName);
                                if(player != null) {
                                    player.sendMessage(colorMessage + "You have been added as " + PlayerUtils.matchUDS(senderName).getNick() + "'s room mate.");
                                }
                            } else {
                                sender.sendMessage(colorError + "You do not have a home.");
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_PLAYER);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("kick") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.home.kick")) {
                    if(args.length == 2) {
                        final UDSPlayer target = PlayerUtils.matchUDS(args[1]);
                        if(target != null) {
                            if(regions.containsKey(senderName + "home")) {
                                final String targetName = target.getName();
                                if(regions.get(senderName + "home").getMembers().contains(targetName)) {
                                    regions.get(senderName + "home").delMember(targetName);
                                    sender.sendMessage(colorMessage + target.getNick() + " has been removed from room mates.");
                                    final Player player = Bukkit.getPlayerExact(targetName);
                                    if(player != null) {
                                        player.sendMessage(colorMessage + "You are no longer " + PlayerUtils.matchUDS(senderName).getNick() + "'s room mate.");
                                    }
                                } else {
                                    sender.sendMessage(colorError + targetName + " is not your room mate.");
                                }
                            } else {
                                sender.sendMessage(colorError + "You do not have a home.");
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_PLAYER);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("roomies") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.home.roomies")) {
                    if(regions.containsKey(senderName + "home")) {
                        Region region = regions.get(senderName + "home");
                        List<String> members = region.getMembers();
                        if(!members.isEmpty()) {
                            sender.sendMessage(colorMessage + "Your room mates are:");
                            for (Iterator<String> i = members.iterator(); i.hasNext();) {
                                sender.sendMessage(colorText + i.next());
                            }
                        } else {
                            sender.sendMessage(colorMessage + "You have no room mates.");
                        }
                    }
                    boolean print = true;
                    for(Map.Entry<String, Region> entry : regions.entrySet()) {
                        Region region = entry.getValue();
                        if(region.getMembers().contains(senderName) && !entry.getKey().equals(senderName + "home") && entry.getKey().endsWith("home")) {
                            if(print) {
                                sender.sendMessage(colorMessage + "Your are room mates with:");
                                print = false;
                            }
                            sender.sendMessage(colorText + entry.getKey().replaceFirst("home", ""));
                        }
                    }
                }
            } else if(commandName.equalsIgnoreCase("help")) {
                if(sender.hasPermission("udsplugin.help.home")) {
                    sender.performCommand("help home");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("lock")) {
                if(sender.hasPermission("udsplugin.home.lock")) {
                    Region region = regions.get(senderName + "home");
                    if(region != null) {
                        if(!region.getFlag("lock")) {
                            region.toggleFlag("lock");
                        }
                        sender.sendMessage(Color.MESSAGE + "Your home is now locked.");
                    } else {
                        sender.sendMessage(UDSMessage.NO_HOME);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("unlock")) {
                if(sender.hasPermission("udsplugin.home.unlock")) {
                    Region region = regions.get(senderName + "home");
                    if(region != null) {
                        if(region.getFlag("lock")) {
                            region.toggleFlag("lock");
                        }
                        sender.sendMessage(Color.MESSAGE + "Your home is now unlocked.");
                    } else {
                        sender.sendMessage(UDSMessage.NO_HOME);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("expand")) {
                if(args.length == 2) {
                    sender.sendMessage(homeExpand(args[1], sender));
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else if(commandName.equalsIgnoreCase("boot")) {
                if(args.length == 1) {
                    sender.sendMessage(bootAll(sender));
                } else if(args.length == 2) {
                    sender.sendMessage(bootPlayer(sender, args[1]));
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                if(sender.hasPermission("udsplugin.home.tp.other")) {
                    if(PlayerUtils.getUDS(sender).isInPrison()) {
                        sender.sendMessage(UDSMessage.IN_PRISON);
                        return true;
                    }
                    UDSPlayer target = PlayerUtils.matchUDS(args[0]);
                    if(target != null) {
                        if(senderPlayer.getLastAttack() + config.getPVPTimer() < System.currentTimeMillis()) {
                            String targetName = target.getName();
                            Region region = regions.get(targetName + "home");
                            if(region != null) {
                                if(region.getMembers().contains(senderName) || sender.hasPermission("udsplugin.home.tp.any")) {
                                    Location location = LocationUtils.findSafePlace(target.getHome());
                                    location.getWorld().getChunkAt(location).load();
                                    sender.teleport(location);
                                } else {
                                    sender.sendMessage(UDSMessage.BAD_ROOMIE);
                                }
                            } else {
                                sender.sendMessage(UDSMessage.NO_TARGET_HOME);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.TP_WAIT);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.NO_PLAYER);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            }
        } else if(args.length == 0) {
            if(sender.hasPermission("udsplugin.home.tp")) {
                if(PlayerUtils.getUDS(sender).isInPrison()) {
                    sender.sendMessage(UDSMessage.IN_PRISON);
                    return true;
                }
                if(senderPlayer.getLastAttack() + config.getPVPTimer() < System.currentTimeMillis()) {
                    Location location = serverPlayers.get(senderName).getHome();
                    if(location != null) {
                        location.getWorld().getChunkAt(location).load();
                        sender.teleport(LocationUtils.findSafePlace(location));
                    } else {
                        sender.sendMessage(colorError + "You do not have a home point to teleport to.");
                    }
                } else {
                    sender.sendMessage(UDSMessage.TP_WAIT);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.performCommand("help home");
        }
        return true;
    }

    private String homeExpand(final String direction, final Player sender) {
        if(!sender.hasPermission("udsplugin.home.expand")) {
            return UDSMessage.NO_PERM;
        }
        final Region home = UDSPlugin.getRegions().get(sender.getName() + "home");
        if(home == null) {
            return UDSMessage.NO_HOME;
        }
        if(!PlayerUtils.matchUDS(sender.getName()).hasEnough(plugin.getConfig().getLong("cost.expand"))) {
            return UDSMessage.NO_MONEY;
        }
        Region test = new Region("test", home.getMinVector(), home.getMaxVector(), home.getWarp(), "test");
        if(direction.equalsIgnoreCase("north")) {
            test.addMinVector(0, 0, -1);
        } else if(direction.equalsIgnoreCase("south")) {
            test.addMaxVector(0, 0, 1);
        } else if(direction.equalsIgnoreCase("east")) {
            test.addMaxVector(1, 0, 0);
        } else if(direction.equalsIgnoreCase("west")) {
            test.addMinVector(-1, 0, 0);
        } else if(direction.equalsIgnoreCase("up")) {
            test.addMaxVector(0, 1, 0);
        } else if(direction.equalsIgnoreCase("down")) {
            test.addMinVector(0, -1, 0);
        } else {
            return UDSMessage.BAD_DIRECTION;
        }
        if(RegionUtils.findRegionsHere(test).size() != 1) {
            return Color.ERROR + "You cannot expand any further in this direction.";
        }
        home.reset(test.getMinVector(), test.getMaxVector());
        PlayerUtils.matchUDS(sender.getName()).takeMoney(plugin.getConfig().getLong("cost.expand"));
        return Color.MESSAGE + "Home protection expanded.";
    }

    private String bootPlayer(final Player sender, final String targetPartial) {
        if(!sender.hasPermission("udsplugin.home.boot")) {
            return UDSMessage.NO_PERM;
        }
        final Region home = UDSPlugin.getRegions().get(sender.getName() + "home");
        if(home == null) {
            return UDSMessage.NO_HOME;
        }
        final Player target = PlayerUtils.matchOnlinePlayer(targetPartial);
        if(target == null) {
            return UDSMessage.PLAYER_NOT_ONLINE;
        }
        if(!RegionUtils.isInRegion(target.getLocation(), home)) {
            return Color.ERROR + "Player is not in your home.";
        }
        if(home.getOwner().equals(target.getName())) {
            return Color.ERROR + "You can't get booted from your own home.";
        }
        if(home.getMembers().contains(target.getName())) {
            return Color.ERROR + "Use /kick to boot a room mate.";
        }
        target.teleport(target.getWorld().getSpawnLocation());
        return Color.MESSAGE + UDSPlugin.getPlayers().get(target.getName()).getNick() + " was booted from your home.";
    }

    private String bootAll(final Player sender) {
        if(!sender.hasPermission("udsplugin.home.boot")) {
            return UDSMessage.NO_PERM;
        }
        final Region home = UDSPlugin.getRegions().get(sender.getName() + "home");
        if(home == null) {
            return UDSMessage.NO_HOME;
        }
        final Player[] players = Bukkit.getOnlinePlayers();
        for(int i = 0; i < players.length; i++) {
            final boolean a = RegionUtils.isInRegion(players[i].getLocation(), home);
            final boolean b = home.getOwner().equals(players[i].getName());
            final boolean c = home.getMembers().contains(players[i].getName());
            if(a && !b && !c) {
                players[i].teleport(players[i].getWorld().getSpawnLocation());
            }
        }
            return Color.MESSAGE + "Only room mates remain in your home.";
    }
}

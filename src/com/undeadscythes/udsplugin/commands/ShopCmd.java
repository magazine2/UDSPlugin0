package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.Request;
import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.ItemUtils;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ShopCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public ShopCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);
        final UDSConfig config = UDSPlugin.getUDSConfig();
        final String currencies = config.getCurrencies();
        final World world = server.getWorld(config.getWorldName());
        final ChatColor colorError = Color.ERROR;
        final ChatColor colorMessage = Color.MESSAGE;
        final ChatColor colorBroadcast = Color.BROADCAST;
        final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        final UDSPlayer senderPlayer = serverPlayers.get(senderName);
        final UDSHashMap<Region> regions = UDSPlugin.getRegions();


        if(args.length != 0) {
            String commandName = args[0];
            if(commandName.equalsIgnoreCase("buy") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.shop.buy")) {
                    if(!senderPlayer.hasEnough(config.getShopCost())) {
                        sender.sendMessage(colorError + "You do not have enough money to buy a shop.");
                        return true;
                    }
                    Location pLoc = sender.getLocation();
                    double x = pLoc.getX();
                    double y = pLoc.getY();
                    double z = pLoc.getZ();
                    Vector bv = new Vector(x, y, z);
                    Region existing = regions.get(senderName + "shop");
                    if(existing != null) {
                        sender.sendMessage(colorError + "You already own a shop.");
                        return true;
                    }
                    if(RegionUtils.isInOpenArea(bv)) {
                        sender.sendMessage(colorError + "You need to be stood in an empty shop plot.");
                        return true;
                    }
                    UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(pLoc);
                    for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                        Region region = i.getValue();
                        if(i.getKey().startsWith("shop")) {
                            Region shop = new Region(senderName + "shop", region.getMinVector(), region.getMaxVector(), sender.getLocation(), senderName);
                            regions.put(senderName + "shop", shop);
                            regions.remove(i.getKey());
                            senderPlayer.takeMoney(config.getShopCost());
                            sender.sendMessage(colorMessage + "Shop successfully bought.");
                            return true;
                        }
                    }
                    sender.sendMessage("You need to be stood in an empty shop plot.");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("sell") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.shop.sell")) {
                    if(!regions.containsKey(senderName + "shop")) {
                        sender.sendMessage(colorError + "You do not have a shop to sell.");
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
                        if(UDSPlugin.getRequests().containsKey(serverPlayer.getName())) {
                            sender.sendMessage(colorError + targetName + " already has a pending request.");
                            return true;
                        }
                        UDSPlugin.getRequests().put(targetName, new Request(targetName, senderName, Request.Type.SHOP, args[2], config.getRequestTimeout()));
                        target.sendMessage(colorMessage + "Do you want to buy " + senderName + "'s shop for " + payment + " " + currencies + "?");
                        target.sendMessage(colorMessage + "Use /y or /n in response.");
                        sender.sendMessage(colorMessage + "Your offer has been sent to " + targetName + ".");
                    } else {
                        sender.sendMessage(UDSMessage.NO_NUMBER);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("clear") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.shop.clear")) {
                    if(!regions.containsKey(senderName + "shop")) {
                        sender.sendMessage(colorError + "You do not have a shop.");
                        return true;
                    }
                    Region region = regions.get(senderName + "shop");
                    int rNo = 0;
                    while(regions.containsKey("shop" + rNo)) {
                        rNo++;
                    }
                    Region shop = new Region("shop" + rNo, region.getMinVector(), region.getMaxVector(), region.getWarp(), "");
                    regions.remove(senderName + "shop");
                    regions.put("shop" + rNo, shop);
                    sender.sendMessage(colorMessage + "Shop abandoned.");
                    server.broadcastMessage(colorBroadcast + senderName + "'s shop plot is up for sale.");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("make") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.shop.make")) {
                    Location pLoc = sender.getLocation();
                    int x = pLoc.getBlockX();
                    int y = pLoc.getBlockY();
                    int z = pLoc.getBlockZ();
                    int shopSize = 10;
                    Vector bv1 = new Vector(x + shopSize, y + 10, z + shopSize);
                    Vector bv2 = new Vector(x - 1, y - 6, z - 1);
                    int rNo = 0;
                    while(regions.containsKey("shop" + rNo)) {
                        rNo++;
                    }
                    Region region = new Region("shop" + rNo, bv1, bv2, sender.getLocation(), "");
                    regions.put("shop" + rNo, region);
                    int slab = Material.STEP.getId();
                    byte data = 4;
                    world.getBlockAt(x - 1, y, z - 1).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x, y, z - 1).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x - 1, y, z).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x + shopSize, y, z + shopSize).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x + shopSize - 1, y, z + shopSize).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x + shopSize, y, z + shopSize - 1).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x + shopSize, y, z - 1).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x + shopSize, y, z).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x + shopSize - 1, y, z - 1).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x - 1, y, z + shopSize).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x, y, z + shopSize).setTypeIdAndData(slab, data, true);
                    world.getBlockAt(x - 1, y, z + shopSize - 1).setTypeIdAndData(slab, data, true);
                sender.sendMessage(colorMessage + "Shop plot created 10 blocks up and 6 blocks down.");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("set") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.shop.set")) {
                    Vector bv1 = senderPlayer.getEditPoint1();
                    Vector bv2 = senderPlayer.getEditPoint2();
                    int rNo = 0;
                    while(regions.containsKey("shop" + rNo)) {
                        rNo++;
                    }
                    Region region = new Region("shop" + rNo, bv1, bv2, sender.getLocation(), "");
                    regions.put("shop" + rNo, region);
                sender.sendMessage(colorMessage + "Shop plot set.");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("del")) {
                if(sender.hasPermission("udsplugin.shop.del")) {
                    Location pLoc = sender.getLocation();
                    double x = pLoc.getX();
                    double y = pLoc.getY();
                    double z = pLoc.getZ();
                    Vector bv = new Vector(x, y, z);
                    if(RegionUtils.isInOpenArea(bv)) {
                        sender.sendMessage(colorError + "You need to be stood in a shop plot.");
                        return true;
                    }
                    UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(pLoc);
                    for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                        if(i.getKey().startsWith("shop")) {
                            regions.remove(i.getKey());
                            sender.sendMessage(colorMessage + "Shop successfully removed.");
                            return true;
                        }
                    }
                    sender.sendMessage("You need to be stood in a shop plot.");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("sign")) {
                if(sender.hasPermission("udsplugin.shop.sign")) {
                    sender.sendMessage(Color.MESSAGE + "Correct shop sign format:");
                    sender.sendMessage(Color.COMMAND + "Line 1 - " + Color.TEXT + "Leave this line blank.\n");
                    sender.sendMessage(Color.COMMAND + "Line 2 - " + Color.TEXT + "shop\n");
                    sender.sendMessage(Color.COMMAND + "Line 3 - " + Color.TEXT + "<item>\n");
                    sender.sendMessage(Color.COMMAND + "Line 4 - " + Color.TEXT + "<buy price>:<sell price>");
                    sender.sendMessage(Color.TEXT + "The buy price is how much people pay to buy from the shop.");
                    sender.sendMessage(Color.TEXT + "The sell price is how much people pay to sell to the shop.");
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("item")) {
                if(sender.hasPermission("udsplugin.shop.item")) {
                    ItemStack itemStack = sender.getItemInHand();
                    sender.sendMessage(Color.MESSAGE + "Item is " + Color.TEXT + ItemUtils.findBestItemName(itemStack));
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("tp")) {
                if(sender.hasPermission("udsplugin.shop.tp") && args.length == 1) {
                    Region region = regions.get(senderName + "shop");
                    if(region != null) {
                        double x = (region.getMinVector().getX() + region.getMaxVector().getX()) / 2;
                        double y = region.getMinVector().getY();
                        double z = (region.getMinVector().getZ() + region.getMaxVector().getZ()) / 2;
                        Location location = LocationUtils.findSafePlace(new Location(world, x, y, z));
                        location.setPitch(sender.getLocation().getPitch());
                        location.setYaw(sender.getLocation().getYaw());
                        location.getWorld().getChunkAt(location).load();
                        sender.teleport(location);
                    } else {
                        sender.sendMessage(UDSMessage.NO_SHOP_OWNED);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("tp")) {
                if(sender.hasPermission("udsplugin.shop.tp.other") && args.length == 2) {
                    UDSPlayer target = PlayerUtils.matchUDS(args[1]);
                    if(target != null) {
                        if(senderPlayer.getLastAttack() + config.getPVPTimer() < System.currentTimeMillis()) {
                            String targetName = target.getName();
                            Region region = regions.get(targetName + "shop");
                            if(region != null) {
                                if(region.getMembers().contains(senderName) || sender.hasPermission("udsplugin.shop.tp.any")) {
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
            } else if(commandName.equalsIgnoreCase("hire") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.shop.hire")) {
                    if(args.length == 2) {
                        final UDSPlayer target = PlayerUtils.matchUDS(args[1]);
                        if(target != null) {
                            final String targetName = target.getName();
                            if(regions.containsKey(senderName + "shop")) {
                                regions.get(senderName + "shop").addMember(targetName);
                                sender.sendMessage(colorMessage + targetName + " has been added as a worker.");
                                final Player player = Bukkit.getPlayerExact(targetName);
                                if(player != null) {
                                    player.sendMessage(colorMessage + "You have been added as " + senderName + "'s worker.");
                                }
                            } else {
                                sender.sendMessage(colorError + "You do not have a shop.");
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
            } else if(commandName.equalsIgnoreCase("fire") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.shop.fire")) {
                    if(args.length == 2) {
                        final UDSPlayer target = PlayerUtils.matchUDS(args[1]);
                        if(target != null) {
                            if(regions.containsKey(senderName + "shop")) {
                                final String targetName = target.getName();
                                if(regions.get(senderName + "shop").getMembers().contains(targetName)) {
                                    regions.get(senderName + "shop").delMember(targetName);
                                    sender.sendMessage(colorMessage + targetName + " has been fired from your service.");
                                    final Player player = Bukkit.getPlayerExact(targetName);
                                    if(player != null) {
                                        player.sendMessage(colorMessage + "You are no longer " + senderName + "'s worker.");
                                    }
                                } else {
                                    sender.sendMessage(colorError + targetName + " is not your worker.");
                                }
                            } else {
                                sender.sendMessage(colorError + "You do not have a shop.");
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
            } else if(commandName.equalsIgnoreCase("workers") && sender instanceof Player) {
                if(sender.hasPermission("udsplugin.shop.workers")) {
                    if(regions.containsKey(senderName + "shop")) {
                        Region region = regions.get(senderName + "shop");
                        List<String> members = region.getMembers();
                        if(!members.isEmpty()) {
                            sender.sendMessage(colorMessage + "Your workers are:");
                            for (Iterator<String> i = members.iterator(); i.hasNext();) {
                                sender.sendMessage(Color.TEXT + i.next());
                            }
                        } else {
                            sender.sendMessage(colorMessage + "You have no workers.");
                        }
                    }
                    boolean print = true;
                    for(Map.Entry<String, Region> entry : regions.entrySet()) {
                        Region region = entry.getValue();
                        if(region.getMembers().contains(senderName) && !entry.getKey().equals(senderName + "shop") && entry.getKey().endsWith("shop")) {
                            if(print) {
                                sender.sendMessage(colorMessage + "You work for:");
                                print = false;
                            }
                            sender.sendMessage(Color.TEXT + entry.getKey().replaceFirst("shop", ""));
                        }
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(commandName.equalsIgnoreCase("help")) {
                sender.performCommand("help shop");
            } else {
                sender.sendMessage(UDSMessage.BAD_COMMAND);
            }
        } else {
            sender.performCommand("help shop");
        }
        return true;
    }
}

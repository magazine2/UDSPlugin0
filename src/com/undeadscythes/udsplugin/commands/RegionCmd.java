package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSBlock;
import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.ItemUtils;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import com.undeadscythes.udsplugin.utilities.VectorUtils;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class RegionCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public RegionCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        UDSConfig config = UDSPlugin.getUDSConfig();
        World world = server.getWorld(config.getWorldName());
        ChatColor messageColor = Color.MESSAGE;
        ChatColor colorCommand = Color.COMMAND;
        ChatColor colorText = Color.TEXT;
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        UDSPlayer senderPlayer = serverPlayers.get(senderName);
        UDSHashMap<Region> regions = UDSPlugin.getRegions();
        if(args.length != 0) {
            String subCommand = args[0].toLowerCase();
            if(subCommand.equals("set")) {
                if(sender.hasPermission("udsplugin.region.set")) {
                    if(args.length > 1) {
                        Vector point1 = senderPlayer.getEditPoint1();
                        Vector point2 = senderPlayer.getEditPoint2();
                        if(point1 != null && point2 != null) {
                            if(!regions.containsKey(args[1])) {
                                if((!args[1].contains("shop")
                                 && !args[1].contains("home")
                                 && !args[1].contains("portal")
                                 && !args[1].contains("quarry")) || sender.hasPermission("udsplugin.anyregion")) {
                                    String owner = "";
                                    if(args.length == 3) {
                                        owner = args[2];
                                    }
                                    regions.put(args[1], new Region(args[1], point1, point2, sender.getLocation(), owner));
                                    sender.sendMessage(UDSMessage.MSG_REGION_SET);
                                } else {
                                    sender.sendMessage(UDSMessage.BAD_REGION_NAME);
                                }
                            } else {
                                sender.sendMessage(UDSMessage.BAD_REGION_EXISTS);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_POINTS);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("del")) {
                if(sender.hasPermission("udsplugin.region.del")) {
                    if(args.length == 2) {
                        if(regions.containsKey(args[1])) {
                            regions.remove(args[1]);
                            sender.sendMessage(UDSMessage.MSG_REGION_GONE);
                        } else {
                            sender.sendMessage(UDSMessage.NO_REGION);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("list")) {
                if(sender.hasPermission("udsplugin.region.list")) {
                    if(args.length == 1) {
                        String regionList = "";
                        for(Map.Entry<String, Region> i : regions.entrySet()) {
                            if(i.getKey().startsWith("shop")) {
                                regionList = regionList + i.getValue().getName() + ", ";
                            }
                        }
                        if(!regionList.equals("")) {
                            sender.sendMessage(messageColor + "--- Regions (Plots) ---");
                            sender.sendMessage(colorText + regionList.substring(0, regionList.length() - 2));
                        }
                        regionList = "";
                        for(Map.Entry<String, Region> i : regions.entrySet()) {
                            if(i.getKey().endsWith("shop")) {
                                regionList = regionList + i.getValue().getName() + ", ";
                            }
                        }
                        if(!regionList.equals("")) {
                            sender.sendMessage(messageColor + "--- Regions (Shops) ---");
                            sender.sendMessage(colorText + regionList.substring(0, regionList.length() - 2));
                        }
                        regionList = "";
                        for(Map.Entry<String, Region> i : regions.entrySet()) {
                            if(i.getKey().endsWith("home")) {
                                regionList = regionList + i.getValue().getName() + ", ";
                            }
                        }
                        if(!regionList.equals("")) {
                            sender.sendMessage(messageColor + "--- Regions (Homes) ---");
                            sender.sendMessage(colorText + regionList.substring(0, regionList.length() - 2));
                        }
                        regionList = "";
                        for(Map.Entry<String, Region> i : regions.entrySet()) {
                            if(i.getKey().startsWith("quarry")) {
                                regionList = regionList + i.getValue().getName() + ", ";
                            }
                        }
                        if(!regionList.equals("")) {
                            sender.sendMessage(messageColor + "--- Regions (Quarries) ---");
                            sender.sendMessage(colorText + regionList.substring(0, regionList.length() - 2));
                        }
                        regionList = "";
                        for(Map.Entry<String, Region> i : regions.entrySet()) {
                            if(i.getKey().startsWith("portal")) {
                                regionList = regionList + i.getValue().getName() + ", ";
                            }
                        }
                        if(!regionList.equals("")) {
                            sender.sendMessage(messageColor + "--- Regions (Portals) ---");
                            sender.sendMessage(colorText + regionList.substring(0, regionList.length() - 2));
                        }
                        regionList = "";

                        for(Map.Entry<String, Region> i : regions.entrySet()) {
                            if(!i.getKey().contains("shop")
                            && !i.getKey().contains("home")
                            && !i.getKey().contains("quarry")
                            && !i.getKey().contains("portal")) {
                                regionList = regionList + i.getValue().getName() + ", ";
                            }
                        }
                        if(!regionList.equals("")) {
                             sender.sendMessage(messageColor + "--- Regions (Other) ---");
                             sender.sendMessage(colorText + regionList.substring(0, regionList.length() - 2));
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("tp")) {
                if(sender.hasPermission("udsplugin.region.tp")) {
                    if(args.length == 2) {
                        if(regions.containsKey(args[1])) {
                            double x = (regions.get(args[1]).getMinVector().getX() + regions.get(args[1]).getMaxVector().getX()) / 2;
                            double y = regions.get(args[1]).getMinVector().getY();
                            double z = (regions.get(args[1]).getMinVector().getZ() + regions.get(args[1]).getMaxVector().getZ()) / 2;
                            Location location = LocationUtils.findSafePlace(new Location(world, x, y, z));
                            location.setPitch(sender.getLocation().getPitch());
                            location.setYaw(sender.getLocation().getYaw());
                            sender.teleport(location);
                        } else {
                            sender.sendMessage(UDSMessage.NO_REGION);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("addmember")) {
                if(sender.hasPermission("udsplugin.region.addmember")) {
                    if(args.length == 3) {
                        if(regions.containsKey(args[1])) {
                            UDSPlayer target = null;
                            boolean group = false;
                            if(args[2].contains("g:")) {
                                group = true;
                            }
                            if(!group) {
                                target = PlayerUtils.matchUDS(args[2]);
                            }
                            if(target != null || group) {
                                String targetName;
                                if(group) {
                                    targetName = args[2];
                                }
                                else {
                                    targetName = target.getName();
                                }
                                regions.get(args[1]).addMember(targetName);
                                sender.sendMessage(messageColor + targetName + " has been added as a member of " + args[1] + ".");
                                final Player player = Bukkit.getPlayerExact(targetName);
                                if(!group && player != null) {
                                    player.sendMessage(messageColor + "You have been added as a member of " + args[1] + ".");
                                }
                            } else {
                                sender.sendMessage(UDSMessage.NO_PLAYER);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_REGION);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("delmember")) {
                if(sender.hasPermission("udsplugin.region.delmember")) {
                    if(args.length == 3) {
                        if(regions.containsKey(args[1])) {
                            UDSPlayer targetPlayer = null;
                            boolean group = false;
                            if(args[2].contains("g:")) {
                                group = true;
                            }
                            if(!group) {
                                targetPlayer = PlayerUtils.matchUDS(args[2]);
                            }
                            if(targetPlayer != null || group) {
                                String targetName;
                                if(group) {
                                    targetName = args[2];
                                }
                                else {
                                    targetName = targetPlayer.getName();
                                }
                                regions.get(args[1]).delMember(targetName);
                                sender.sendMessage(messageColor + targetName + " has been removed from " + args[1] + "'s members.");
                                Player target = Bukkit.getPlayerExact(targetName);
                                if(!group && target != null) {
                                    target.sendMessage(messageColor + "You have been removed from " + args[1] + "'s members.");
                                }
                            } else {
                                sender.sendMessage(UDSMessage.NO_PLAYER);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_REGION);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("owner")) {
                if(sender.hasPermission("udsplugin.region.owner")) {
                    if(args.length == 2) {
                        Region region = regions.get(args[1]);
                        if(region != null) {
                            region.setOwner("");
                            sender.sendMessage(Color.MESSAGE + "Owner removed from " + args[1] + ".");
                        } else {
                            sender.sendMessage(UDSMessage.NO_REGION);
                        }
                    } else if(args.length == 3) {
                        Region region = regions.get(args[1]);
                        if(region != null) {
                            region.setOwner(args[2]);
                            sender.sendMessage(Color.MESSAGE + args[2] + " set as owner of " + args[1] + ".");
                        } else {
                            sender.sendMessage(UDSMessage.NO_REGION);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if("info".equals(subCommand)) {
                if(sender.hasPermission("udsplugin.region.info")) {
                    if(args.length == 2) {
                        final Region region = regions.get(args[1]);
                        if(region != null) {
                            sender.sendMessage(messageColor + "Region " + region.getName() + " flags:");
                            sender.sendMessage(colorCommand + "Fire - " + colorText + region.getFlag("fire"));
                            sender.sendMessage(colorCommand + "Mobs - " + colorText + region.getFlag("mobs"));
                            sender.sendMessage(colorCommand + "Mushrooms - " + colorText + region.getFlag("mushroom"));
                            sender.sendMessage(colorCommand + "Vines - " + colorText + region.getFlag("vine"));
                            sender.sendMessage(colorCommand + "PVP - " + colorText + region.getFlag("pvp"));
                            sender.sendMessage(colorCommand + "Snow - " + colorText + region.getFlag("snow"));
                            sender.sendMessage(colorCommand + "Locked - " + colorText + region.getFlag("lock"));
                            sender.sendMessage(colorCommand + "Protected - " + colorText + region.getFlag("protect"));
                            sender.sendMessage(colorCommand + "Safe - " + colorText + region.getFlag("safe"));
                            sender.sendMessage(Color.TEXT + "Owner: " + region.getOwner());
                            sender.sendMessage(Color.TEXT + "Members: " + StringUtils.join(region.getMembers().toArray(), " "));
                        } else {
                            sender.sendMessage(UDSMessage.NO_REGION);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("reset")) {
                if(sender.hasPermission("udsplugin.region.reset")) {
                    if(args.length == 2) {
                        Vector point1 = senderPlayer.getEditPoint1();
                        Vector point2 = senderPlayer.getEditPoint2();
                        if(point1 != null && point2 != null) {
                            if(regions.containsKey(args[1])) {
                                if((!args[1].contains("shop") && !args[1].contains("home")) || sender.hasPermission("udsplugin.anyregion")) {
                                    regions.get(args[1]).reset(point1, point2);
                                    sender.sendMessage(UDSMessage.MSG_REGION_RESET);
                                } else {
                                    sender.sendMessage(UDSMessage.BAD_REGION_NAME);
                                }
                            } else {
                                sender.sendMessage(UDSMessage.NO_REGION);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_POINTS);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("select")) {
                if(sender.hasPermission("udsplugin.region.select")) {
                    if(args.length == 2) {
                        Region region = regions.get(args[1]);
                        if(region != null) {
                            senderPlayer.setEditPoint1(region.getMinVector());
                            senderPlayer.setEditPoint2(region.getMaxVector());
                            sender.sendMessage(Color.MESSAGE + "Region selected.");
                        } else {
                            sender.sendMessage(UDSMessage.NO_REGION);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if("flag".equals(subCommand)) {
                if(sender.hasPermission("udsplugin.region.flag")) {
                    if(args.length == 3) {
                        final Region region = regions.get(args[1]);
                        if(region != null) {
                            if(args[2].equalsIgnoreCase("snow")) {
                                region.toggleFlag("snow");
                                sender.sendMessage(messageColor + "Snow can form: " + region.getFlag("snow"));
                            } else if(args[2].equalsIgnoreCase("protect")) {
                                region.toggleFlag("protect");
                                sender.sendMessage(messageColor + "Region is protected: " + region.getFlag("protect"));
                            } else if(args[2].equalsIgnoreCase("locked")) {
                                region.toggleFlag("lock");
                                sender.sendMessage(messageColor + "Region is locked: " + region.getFlag("lock"));
                            } else if(args[2].equalsIgnoreCase("pvp")) {
                                region.toggleFlag("pvp");
                                sender.sendMessage(messageColor + "PVP is allowed: " + region.getFlag("pvp"));
                            } else if(args[2].equalsIgnoreCase("mobs")) {
                                region.toggleFlag("mobs");
                                sender.sendMessage(messageColor + "Mobs can spawn: " + region.getFlag("mobs"));
                            } else if(args[2].equalsIgnoreCase("fire")) {
                                region.toggleFlag("fire");
                                sender.sendMessage(messageColor + "Fire can spread and burn blocks: " + region.getFlag("fire"));
                            } else if(args[2].equalsIgnoreCase("mushroom")) {
                                region.toggleFlag("mushroom");
                                sender.sendMessage(messageColor + "Mushrooms can spread: " + region.getFlag("mushroom"));
                            } else if(args[2].equalsIgnoreCase("vine")) {
                                region.toggleFlag("vine");
                                sender.sendMessage(messageColor + "Vines can spread: " + region.getFlag("vine"));
                            } else if(args[2].equalsIgnoreCase("safe")) {
                                region.toggleFlag("safe");
                                sender.sendMessage(messageColor + "Mobs are safe: " + region.getFlag("safe"));
                            } else {
                                sender.sendMessage(UDSMessage.NO_FLAG);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_REGION);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("portal")) {
                if(sender.hasPermission("udsplugin.region.portal")) {
                    if(args.length == 3) {
                        if(!args[1].contains("-") || !args[2].contains("-")) {
                            regions.put("portal:" + args[1] + "-" + args[2], new Region("portal:" + args[1] + "-" + args[2], senderPlayer.getEditPoint1(), senderPlayer.getEditPoint2(), sender.getLocation(), ""));
                            sender.sendMessage(UDSMessage.MSG_PORTAL_SET);
                        } else {
                            sender.sendMessage(UDSMessage.BAD_REGION_NAME);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("quarry")) {
                if(sender.hasPermission("udsplugin.region.quarry")) {
                    if(args.length == 2) {
                        if(ItemUtils.findItem(args[1]) != null) {
                            ItemStack item = ItemUtils.findItem(args[1]);
                            byte data = item.getData().getData();
                            int id = item.getTypeId();
                            Vector v1 = senderPlayer.getEditPoint1();
                            Vector v2 = senderPlayer.getEditPoint2();
                            Vector min = VectorUtils.findMin(v1, v2);
                            Vector max = VectorUtils.findMax(v1, v2);
                            LinkedList<UDSBlock> blocks = new LinkedList();
                            for(int ix = (int) min.getX(); ix <= (int) max.getX(); ix++) {
                                for(int iy = (int) min.getY(); iy <= (int) max.getY(); iy++) {
                                    for(int iz = (int) min.getZ(); iz <= (int) max.getZ(); iz++) {
                                        blocks.add(new UDSBlock(world.getBlockAt(ix, iy, iz)));
                                        if(world.getBlockAt(ix, iy, iz).getType() != Material.BEDROCK) {
                                            world.getBlockAt(ix, iy, iz).setTypeIdAndData(id, data, true);
                                        }
                                    }
                                }
                            }
                            senderPlayer.saveUndo(min, max, blocks);
                            sender.sendMessage(messageColor + "Set " + RegionUtils.findVolume(v1, v2) + " blocks.");
                            regions.put("quarry:" + args[1], new Region("quarry:" + args[1], senderPlayer.getEditPoint1(), senderPlayer.getEditPoint2(), sender.getLocation(), ""));
                            sender.sendMessage(UDSMessage.MSG_QUARRY_SET);
                        } else {
                            sender.sendMessage(UDSMessage.NO_ITEM);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if("rename".equals(subCommand)) {
                if(sender.hasPermission("udsplugin.region.rename")) {
                    if(args.length == 3) {
                        sender.sendMessage(RegionUtils.regionRename(args[1], args[2]));
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("vert")) {
                if(sender.hasPermission("udsplugin.region.vert")) {
                    Vector point1 = senderPlayer.getEditPoint1();
                    Vector point2 = senderPlayer.getEditPoint2();
                    if(point1 != null && point2 != null) {
                        point1.setY(256);
                        point2.setY(0);
                        sender.sendMessage(UDSMessage.MSG_VERT);
                    } else {
                        sender.sendMessage(UDSMessage.NO_POINTS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equals("help")) {
                sender.performCommand("help region");
            } else {
                sender.sendMessage(UDSMessage.BAD_COMMAND);
            }
        } else {
            sender.performCommand("help region");
        }
        return true;
    }
}

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.comparators.ClanByRatio;
import com.undeadscythes.udsplugin.Clan;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.Request;
import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSString;
import com.undeadscythes.udsplugin.utilities.ChatUtils;
import com.undeadscythes.udsplugin.utilities.ClanUtils;
import com.undeadscythes.udsplugin.utilities.CmdUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import com.undeadscythes.udsplugin.utilities.WEUtils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ClanCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;
    private final int BASE_RADIUS = 25;

    public ClanCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        final UDSConfig config = UDSPlugin.getUDSConfig();
        final ChatColor errorColor = Color.ERROR;
        final ChatColor messageColor = Color.MESSAGE;
        final ChatColor textColor = Color.TEXT;
        final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        final UDSPlayer senderPlayer = serverPlayers.get(senderName);
        final UDSHashMap<Clan> clans = UDSPlugin.getClans();
        if(args.length != 0) {
            final String subCommand = args[0];
            if(subCommand.equalsIgnoreCase("new")) {
                if(sender.hasPermission("udsplugin.clan.new")) {
                    if(args.length == 2) {
                        if(!senderPlayer.isInClan()) {
                            if(!clans.containsKey(args[1])) {
                                if(!(new UDSString(args[1]).censor())) {
                                    int clanCost = config.getClanCost();
                                    if(senderPlayer.hasEnough(clanCost)) {
                                        clans.put(args[1], new Clan(senderName, args[1]));
                                        senderPlayer.joinClan(args[1]);
                                        senderPlayer.takeMoney(clanCost);
                                        sender.sendMessage(messageColor + "Clan created.");
                                        plugin.getServer().broadcastMessage(Color.BROADCAST + senderPlayer.getNick() + " founded clan " + args[1] + ".");
                                    } else {
                                        sender.sendMessage(UDSMessage.NO_MONEY);
                                    }
                                } else {
                                    sender.sendMessage(UDSMessage.BAD_STRING);
                                }
                            } else {
                                sender.sendMessage(UDSMessage.BAD_NAME);
                            }
                        } else {
                            sender.sendMessage(errorColor + "You are already in a clan.");
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("invite")) {
                if(CmdUtils.perm(sender, "clan.invite") && CmdUtils.isLeader(sender) && CmdUtils.hasRequest(sender, args[1])) {
                    if(args.length == 2) {
                        final String clanName = senderPlayer.getClan();
                        Player target = PlayerUtils.matchOnlinePlayer(args[1]);
                        String targetName = target.getName();
                        UDSPlayer targetPlayer = serverPlayers.get(targetName);
                        if(!targetPlayer.isInClan()) {
                            target.sendMessage(messageColor + senderName + " has invited you to join clan " + clanName + ". (Joining a clan enables PVP)");
                            target.sendMessage(UDSMessage.MSG_REQUEST);
                            UDSPlugin.getRequests().put(targetName, new Request(targetName, senderName, Request.Type.CLAN, clanName, 0));
                            sender.sendMessage(messageColor + "Invite sent.");
                        } else {
                            target.sendMessage(messageColor + senderName + " has invited you to leave " + targetPlayer.getClan() + " and join clan " + clanName + ".");
                            target.sendMessage(UDSMessage.MSG_REQUEST);
                            UDSPlugin.getRequests().put(targetName, new Request(targetName, senderName, Request.Type.CLAN, clanName, 0));
                            sender.sendMessage(messageColor + "Invite sent.");
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                }
            } else if(subCommand.equalsIgnoreCase("kick")) {
                if(sender.hasPermission("udsplugin.clan.kick")) {
                    if(senderPlayer.isInClan()) {
                        final String clanName = senderPlayer.getClan();
                        final Clan clan = clans.get(clanName);
                        if(clan.getLeader().equals(senderName)) {
                            if(args.length == 2) {
                                final UDSPlayer player = PlayerUtils.matchUDS(args[1]);
                                if(player != null) {
                                    final String targetName = player.getName();
                                    if(player.getClan().equals(clanName)) {
                                        final Player targetPlayer = Bukkit.getPlayerExact(targetName);
                                        clan.removeMember(targetName);
                                        player.leaveClan();
                                        sender.sendMessage(messageColor + targetName + " has been removed from your clan.");
                                        if(targetPlayer != null) {
                                            targetPlayer.sendMessage(messageColor + "You have been kicked from your clan.");
                                            if(UDSPlugin.getRegions().get(clan.getName() + "base") != null) {
                                                UDSPlugin.getRegions().get(clan.getName() + "base").delMember(targetName);
                                            }
                                        }
                                    } else {
                                        sender.sendMessage(errorColor + targetName + " is not in your clan.");
                                    }
                                } else {
                                    sender.sendMessage(UDSMessage.NO_PLAYER);
                                }
                            } else {
                                sender.sendMessage(UDSMessage.BAD_ARGS);
                            }
                        } else {
                            sender.sendMessage(errorColor + "Only the clan leader can kick members.");
                        }
                    } else {
                        sender.sendMessage(UDSMessage.NOT_IN_CLAN);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("leave") && CmdUtils.perm(sender, "clan.leave") && CmdUtils.inClan(sender)) {
                senderPlayer.leaveClan();
            } else if(subCommand.equalsIgnoreCase("members")) {
                if(sender.hasPermission("udsplugin.clan.members")) {
                    if(args.length == 1) {
                        if(senderPlayer.isInClan()) {
                            Clan clan = clans.get(senderPlayer.getClan());
                            List<String> members = clan.getMembers();
                            sender.sendMessage(messageColor + senderPlayer.getClan() + " members:");
                            for(Iterator<String> i = members.iterator(); i.hasNext();) {
                                String member = i.next();
                                if(member.equals(clan.getLeader())) {
                                    sender.sendMessage(textColor + member + " (Leader)");
                                }
                                else {
                                    sender.sendMessage(textColor + member);
                                }
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NOT_IN_CLAN);
                        }
                    } else if(args.length == 2) {
                        final Clan clan = clans.get(args[1]);
                        if(clan != null) {
                            List<String> members = clan.getMembers();
                            sender.sendMessage(messageColor + clan.getName() + " members:");
                            for(Iterator<String> i = members.iterator(); i.hasNext();) {
                                String member = i.next();
                                String memberNick = serverPlayers.get(member).getNick();
                                if(member.equals(clan.getLeader())) {
                                    sender.sendMessage(textColor + memberNick + " (Leader)");
                                } else {
                                    sender.sendMessage(textColor + memberNick);
                                }
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_CLAN);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("list")) {
                if(sender.hasPermission("udsplugin.clan.list")) {
                    if(args.length == 1 || args.length == 2) {
                        int page;
                        if(args.length == 2) {
                            if(args[1].matches("[0-9][0-9]*")) {
                                page = Integer.parseInt(args[1]);
                            } else {
                                sender.sendMessage(UDSMessage.NO_NUMBER);
                                return true;
                            }
                        } else {
                            page = 1;
                        }
                        sendClanList(sender, page);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("disband")) {
                if(sender.hasPermission("udsplugin.clan.disband")) {
                    if(senderPlayer.isInClan()) {
                        final String clanName = senderPlayer.getClan();
                        Clan clan = clans.get(clanName);
                        if(clan.getLeader().equals(senderName)) {
                            if(args.length == 2) {
                                final List<String> members = ClanUtils.match(args[1], clans).getMembers();
                                for(Iterator<String> i = members.iterator(); i.hasNext();) {
                                    String member = i.next();
                                    UDSPlayer serverPlayer = serverPlayers.get(member);
                                    if(!(serverPlayer == null) &&
                                       serverPlayer.getClan().equalsIgnoreCase(args[1])) {
                                        serverPlayer.leaveClan();
                                    }
                                }
                                clans.remove(args[1]);
                                if(UDSPlugin.getRegions().get(clan.getName() + "base") != null) {
                                    UDSPlugin.getRegions().remove(clan.getName() + "base");
                                }
                                sender.sendMessage(messageColor + "Clan " + args[1] + " disbanded.");
                            } else {
                                sender.sendMessage(UDSMessage.BAD_ARGS);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NOT_LEADER);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.NOT_IN_CLAN);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("owner")) {
                if(args.length == 3) {
                    if(CmdUtils.perm(sender, "clan.owner") && CmdUtils.isPlayer(sender, args[2]) && CmdUtils.isClan(sender, args[2])) {
                        UDSPlayer target = PlayerUtils.matchUDS(args[2]);
                        Clan clan = clans.get(args[1]);
                        boolean anyClan = false;
                        if(sender.hasPermission("udsplugin.clan.owner.any")) {
                            anyClan = true;
                        }
                        if(clan.getLeader().equalsIgnoreCase(senderName) || anyClan) {
                            List<String> clanMembers = clan.getMembers();
                            String targetName = target.getName();
                            if(clanMembers.contains(targetName) || anyClan) {
                                clan.setLeader(targetName);
                                if(UDSPlugin.getRegions().get(clan.getName() + "base") != null) {
                                    UDSPlugin.getRegions().get(clan.getName() + "base").setOwner(targetName);
                                }
                                if(!clanMembers.contains(targetName)) {
                                    PlayerUtils.matchUDS(targetName).joinClan(clan.getName());
                                }
                                ChatUtils.sendClanBroadcast(clan, targetName + " is now the leader of the clan.");
                            } else {
                                sender.sendMessage(UDSMessage.NO_CLAN_MEMBER);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NOT_LEADER);
                        }
                    }
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else if(subCommand.equalsIgnoreCase("help")) {
                sender.performCommand("help clan");
            } else if(subCommand.equalsIgnoreCase("stats")) {
                if(sender.hasPermission("udsplugin.clan.stats")) {
                    if(args.length == 2) {
                        showClanStats(sender, args[1]);
                    } else if(args.length == 1) {
                        showClanStats(sender, senderPlayer.getClan());
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("rename")) {
                if(sender.hasPermission("udsplugin.clan.rename")) {
                    if(args.length == 2) {
                        if(senderPlayer.isInClan()) {
                            Clan clan = clans.get(senderPlayer.getClan());
                            if(clan.getLeader().equalsIgnoreCase(senderName)) {
                                if(!(new UDSString(args[1]).censor())) {
                                    if(!clans.containsKey(args[1])) {
                                        final int clanCost = config.getClanCost();
                                        if(senderPlayer.hasEnough(clanCost)) {
                                            plugin.getServer().broadcastMessage(Color.BROADCAST + senderPlayer.getNick() + " renamed clan " + clan.getName() + " to clan " + args[1] + ".");
                                            RegionUtils.regionRename(clan.getName() + "base", args[1] + "base");
                                            clans.remove(clan.getName());
                                            clan.setName(args[1]);
                                            clans.put(args[1], clan);
                                            senderPlayer.joinClan(args[1]);
                                            List<String> clanMembers = clan.getMembers();
                                            for(Iterator<String> i = clanMembers.iterator(); i.hasNext();) {
                                                String member = i.next();
                                                serverPlayers.get(member).joinClan(args[1]);
                                            }
                                            senderPlayer.takeMoney(clanCost);
                                            sender.sendMessage(messageColor + "Clan renamed.");
                                        } else {
                                            sender.sendMessage(UDSMessage.NO_MONEY);
                                        }
                                    } else {
                                        sender.sendMessage(UDSMessage.BAD_NAME);
                                    }
                                } else {
                                    sender.sendMessage(UDSMessage.BAD_STRING);
                                }
                            } else {
                                sender.sendMessage(UDSMessage.NOT_LEADER);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NOT_IN_CLAN);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("base")) {
                if(args.length == 2) {
                    final Clan clan = UDSPlugin.getClans().get(PlayerUtils.matchUDS(senderName).getClan());
                    if(clan != null) {
                        if(clan.getLeader().equals(senderName)) {
                            if(args[1].equalsIgnoreCase("make")) {
                                if(sender.hasPermission("udsplugin.clan.base.make")) {
                                    if(senderPlayer.hasEnough(config.getBaseCost())) {
                                        final Location playerLocation = sender.getLocation();
                                        final int x = playerLocation.getBlockX();
                                        final int z = playerLocation.getBlockZ();
                                        final Vector bv1 = new Vector(x + BASE_RADIUS, 200, z + BASE_RADIUS);
                                        final Vector bv2 = new Vector(x - BASE_RADIUS, 20, z - BASE_RADIUS);
                                        Region existing = UDSPlugin.getRegions().get(clan.getName() + "base");
                                        if(existing == null) {
                                            Region region = new Region(clan.getName() + "base", bv1, bv2, sender.getLocation(), senderName);
                                            region.setWarp(playerLocation);
                                            if(RegionUtils.isInOpenArea(region)) {
                                                makeBase(sender, region);
                                            } else {
                                                sender.sendMessage(Color.ERROR + "You cannot protect this area, someone has a home nearby.");
                                            }
                                        } else {
                                            sender.sendMessage(Color.ERROR + "You already have a base.");
                                        }
                                    } else {
                                        sender.sendMessage(UDSMessage.NO_MONEY);
                                    }
                                } else {
                                    sender.sendMessage(UDSMessage.NO_PERM);
                                }
                            } else if(args[1].equalsIgnoreCase("clear")) {
                                if(sender.hasPermission("udsplugin.clan.base.clear")) {
                                    Region region = UDSPlugin.getRegions().get(clan.getName() + "base");
                                    if(region != null) {
                                        UDSPlugin.getRegions().remove(clan.getName() + "base");
                                        sender.sendMessage(Color.MESSAGE + "Base protection removed.");
                                    } else {
                                        sender.sendMessage(Color.ERROR + "You do not have a base.");
                                    }
                                } else {
                                    sender.sendMessage(UDSMessage.NO_PERM);
                                }
                            } else if(args[1].equalsIgnoreCase("set")
                                   && CmdUtils.perm(sender, "clan.base.set")
                                   && CmdUtils.inBase(sender, clan.getName() + "base")) {
                                clan.setBase(sender.getLocation());
                                sender.sendMessage(UDSMessage.MSG_BASE_SET);
                            } else {
                                sender.sendMessage(UDSMessage.BAD_COMMAND);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NOT_LEADER);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.NOT_IN_CLAN);
                    }
                } else if(args.length == 1) {
                    if(sender.hasPermission("udsplugin.clan.base.tp")) {
                        if(PlayerUtils.getUDS(sender).isInPrison()) {
                            sender.sendMessage(UDSMessage.IN_PRISON);
                            return true;
                        }
                        Location warp = UDSPlugin.getClans().get(PlayerUtils.matchUDS(senderName).getClan()).getBase();
                        if(warp != null) {
                            sender.teleport(warp);
                        } else {
                            sender.sendMessage(UDSMessage.NO_CLAN_WARP);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.NO_PERM);
                    }
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                sender.sendMessage(UDSMessage.BAD_COMMAND);
            }
        } else {
            sender.performCommand("help clan");
        }
        return true;
    }

    public void sendClanList(Player player, int page) {
        final ArrayList<Clan> clans = new ArrayList<Clan>(UDSPlugin.getClans().values());
        Collections.sort(clans, new ClanByRatio());
        int pages = (clans.size() + 8) / 9;
        if(pages == 0) {
            player.sendMessage(Color.MESSAGE + "There are no clans set up yet.");
        } else if(page > pages) {
            player.sendMessage(UDSMessage.BAD_PAGE);
        } else {
            player.sendMessage(Color.MESSAGE + "--- Clans Page " + page + "/" + pages + " ---");
        }
        int skip = (page - 1) * 9;
        int print = 0;
        for(Clan clan : clans) {
            if(skip == 0 && print < 9) {
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                player.sendMessage(Color.COMMAND + clan.getName() + " - " + Color.TEXT + clan.getLeader() + " " + decimalFormat.format(clan.getKDR()));
                print++;
            } else {
                skip--;
            }
        }
    }

    public void showClanStats(Player player, String clanName) {
        Clan clan = UDSPlugin.getClans().get(clanName);
        if(clan != null) {
            player.sendMessage(Color.MESSAGE + clan.getName() + " stats:");
            player.sendMessage(Color.TEXT + "Leader: " + clan.getLeader());
            player.sendMessage(Color.TEXT + "Members: " + clan.getMembers().size());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            player.sendMessage(Color.TEXT + "KD Ratio: " + decimalFormat.format(clan.getKDR()));
            player.sendMessage(Color.TEXT + "Clan kills: " + clan.getKills());
            player.sendMessage(Color.TEXT + "Clan deaths: " + clan.getDeaths());
        } else {
            player.sendMessage(UDSMessage.NO_CLAN);
        }
    }

    public void makeBase(Player player, Region region) {
        UDSPlayer udsPlayer = PlayerUtils.getUDS(player);
        Clan clan = ClanUtils.get(udsPlayer.getClan());
        clan.setBase(player.getLocation());
        UDSPlugin.getRegions().put(clan.getName() + "base", region);
        for(Iterator<String> i = clan.getMembers().iterator(); i.hasNext();) {
            region.addMember(i.next());
        }
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockX();
        int z = player.getLocation().getBlockZ();
        Material fence = Material.FENCE;
        Material torch = Material.TORCH;
        World world = player.getWorld();
        WEUtils.placeSurfaceLineX(world, fence, x - 1, x + 1, z + BASE_RADIUS);
        WEUtils.placeSurfaceLineX(world, torch, x - 1, x + 1, z + BASE_RADIUS);
        WEUtils.placeSurfaceLineX(world, fence, x - 1, x + 1, z - BASE_RADIUS);
        WEUtils.placeSurfaceLineX(world, torch, x - 1, x + 1, z - BASE_RADIUS);
        WEUtils.placeSurfaceLineZ(world, fence, x + BASE_RADIUS, z - 1, z + 1);
        WEUtils.placeSurfaceLineZ(world, torch, x + BASE_RADIUS, z - 1, z + 1);
        WEUtils.placeSurfaceLineZ(world, fence, x - BASE_RADIUS, z - 1, z + 1);
        WEUtils.placeSurfaceLineZ(world, torch, x - BASE_RADIUS, z - 1, z + 1);
        WEUtils.placeSurfaceCorner(world, fence, x + BASE_RADIUS, -1, z + BASE_RADIUS, -1);
        WEUtils.placeSurfaceCorner(world, torch, x + BASE_RADIUS, -1, z + BASE_RADIUS, -1);
        WEUtils.placeSurfaceCorner(world, fence, x + BASE_RADIUS, -1, z - BASE_RADIUS, 1);
        WEUtils.placeSurfaceCorner(world, torch, x + BASE_RADIUS, -1, z - BASE_RADIUS, 1);
        WEUtils.placeSurfaceCorner(world, fence, x - BASE_RADIUS, 1, z + BASE_RADIUS, -1);
        WEUtils.placeSurfaceCorner(world, torch, x - BASE_RADIUS, 1, z + BASE_RADIUS, -1);
        WEUtils.placeSurfaceCorner(world, fence, x - BASE_RADIUS, 1, z - BASE_RADIUS, 1);
        WEUtils.placeSurfaceCorner(world, torch, x - BASE_RADIUS, 1, z - BASE_RADIUS, 1);
        player.sendMessage(Color.MESSAGE + "Base protected.");
        udsPlayer.takeMoney(UDSPlugin.getUDSConfig().getBaseCost());
    }
}

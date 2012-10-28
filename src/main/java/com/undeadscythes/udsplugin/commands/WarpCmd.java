package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Warp;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCmd implements CommandExecutor {
    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        if(sender.hasPermission("udsplugin.warp")) {
            if(args.length == 0) {
                if(!sender.hasPermission("udsplugin.warp.all")) {
                    String warpList = "";
                    for(Map.Entry<String, Warp> i : UDSPlugin.getWarps().entrySet()) {
                        final Warp warp = i.getValue();
                        if(sender.hasPermission("udsplugin.group." + warp.getRank())) {
                            warpList = warpList.concat(warp.getName().concat(", "));
                        }
                    }
                    if("".equals(warpList)) {
                        sender.sendMessage(Color.MESSAGE + "There are no available warps.");
                    } else {
                        sender.sendMessage(Color.MESSAGE + "--- Available Warps ---");
                        sender.sendMessage(Color.TEXT + warpList.substring(0, warpList.length() - 2));
                    }
                } else {
                    LinkedList<String> ranksDone = new LinkedList<String>();
                    while(true) {
                        String warpList = "";
                        String currentRank = "@#@";
                        for(Map.Entry<String, Warp> i : UDSPlugin.getWarps().entrySet()) {
                            final Warp warp = i.getValue();
                            if("@#@".equals(currentRank) && !ranksDone.contains(warp.getRank().toLowerCase(Locale.ENGLISH))) {
                                currentRank = warp.getRank();
                                ranksDone.add(currentRank.toLowerCase(Locale.ENGLISH));
                            }
                            if(warp.getRank().equalsIgnoreCase(currentRank)) {
                                warpList = warpList.concat(warp.getName().concat(", "));
                            }
                        }
                        if("@#@".equals(currentRank)) {
                            break;
                        }
                        sender.sendMessage(Color.MESSAGE + "--- Rank: " + currentRank + " ---");
                        sender.sendMessage(Color.TEXT + warpList.substring(0, warpList.length() - 2));
                    }
                }
            } else if(args.length == 1) {
                if(PlayerUtils.getUDS(sender).isInPrison()) {
                    sender.sendMessage(UDSMessage.IN_PRISON);
                    return true;
                }
                final Warp warp = UDSPlugin.getWarps().get(args[0]);
                if(warp != null) {
                    if(sender.hasPermission("udsplugin.group." + warp.getRank()) || sender.hasPermission("udsplugin.warp.all")) {
                        final Location location = warp.getLocation();
                        location.getWorld().getChunkAt(location).load();
                        sender.teleport(LocationUtils.findSafePlace(location));
                    } else {
                        sender.sendMessage(UDSMessage.NO_PERM_THIS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_WARP);
                }
            } else {
                sender.sendMessage(UDSMessage.BAD_ARGS);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

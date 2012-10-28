package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.comparators.PlayerByBounty;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCmd implements CommandExecutor {
    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final Server server = Bukkit.getServer();
        final String senderName = commandSender.getName();
        final Player sender = server.getPlayer(senderName);
        if(sender.hasPermission("udsplugin.bounty")) {
            if(args.length == 0 || args.length == 1) {
                int page;
                if(args.length == 1) {
                    if(args[0].matches("[0-9][0-9]*")) {
                        page = Integer.parseInt(args[0]);
                    } else {
                        sender.sendMessage(UDSMessage.NO_NUMBER);
                        return true;
                    }
                } else {
                    page = 1;
                }
                sendBounties(sender, page);
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("remove") && sender.hasPermission("udsplugin.bounty.remove")) {
                    final UDSPlayer targetPlayer = PlayerUtils.matchUDS(args[1]);
                    if(targetPlayer != null) {
                        targetPlayer.claimReward();
                    } else {
                        sender.sendMessage(UDSMessage.NO_PLAYER);
                    }
                } else {
                    final UDSPlayer targetPlayer = PlayerUtils.matchUDS(args[0]);
                    if(targetPlayer != null) {
                        if(args[1].matches("[0-9][0-9]*")) {
                            final int bounty = Integer.parseInt(args[1]);
                            if(bounty > 0) {
                                final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
                                final UDSPlayer senderPlayer = serverPlayers.get(senderName);
                                if(senderPlayer.hasEnough(bounty)) {
                                    senderPlayer.takeMoney(bounty);
                                    targetPlayer.newBounty(bounty);
                                    server.broadcastMessage(Color.BROADCAST + senderPlayer.getNick() + " placed a bounty on " + targetPlayer.getNick() + ".");
                                } else {
                                    sender.sendMessage(UDSMessage.NO_MONEY);
                                }
                            } else {
                                sender.sendMessage(UDSMessage.BAD_NUMBER);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_NUMBER);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.NO_PLAYER);
                    }
                }
            } else {
                sender.sendMessage(UDSMessage.BAD_ARGS);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
    return true;
    }

    public void sendBounties(Player player, int page) {
        final UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        int pages = (PlayerUtils.countBounties(serverPlayers) + 8) / 9;
        if(pages == 0) {
            player.sendMessage(Color.MESSAGE + "There are no bounties to collect.");
        } else if(page > pages) {
            player.sendMessage(UDSMessage.BAD_PAGE);
        } else {
            player.sendMessage(Color.MESSAGE + "--- Current Bounties Page " + page + "/" + pages + " ---");
        }
        ArrayList<UDSPlayer> bounties = new ArrayList<UDSPlayer>();
        for(UDSPlayer i : serverPlayers.values()) {
            if(i.hasBounty()) {
                bounties.add(i);
            }
        }
        Collections.sort(bounties, new PlayerByBounty());
        int skip = (page - 1) * 9;
        int print = 0;
        for(UDSPlayer serverPlayer : bounties) {
            if(skip == 0 && print < 9) {
                player.sendMessage(Color.COMMAND + "- " + serverPlayer.getNick() + "'s reward: " + Color.TEXT + (int)serverPlayer.getBounty() + " " + UDSPlugin.getUDSConfig().getCurrencies());
                print++;
            } else {
                skip--;
            }
        }
    }
}

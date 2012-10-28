package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.comparators.PlayerByMoney;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.CmdUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCmd implements CommandExecutor {
    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        UDSConfig config = UDSPlugin.getUDSConfig();
        String currencies = config.getCurrencies();
        ChatColor colorError = Color.ERROR;
        ChatColor colorMessage = Color.MESSAGE;
        ChatColor colorCommand = Color.COMMAND;
        ChatColor colorText = Color.TEXT;
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();
        UDSPlayer senderPlayer = serverPlayers.get(senderName);
        if(args.length != 0) {
            String subCommand = args[0];
            if(subCommand.equalsIgnoreCase("prices")) {
                if(CmdUtils.perm(sender, "money.prices")) {
                    sender.sendMessage(colorMessage + "--- Server Prices ---");
                    sender.sendMessage(colorCommand + "Build Rights: " + colorText + (int) config.getPromotionCost() + " " + currencies);
                    sender.sendMessage(colorCommand + "Map of Spawn: " + colorText + (int) config.getMapCost() + " " + currencies);
                    sender.sendMessage(colorCommand + "Home Protection: " + colorText + (int) config.getHomeCost() + " " + currencies);
                    sender.sendMessage(colorCommand + "City Shop: " + colorText + (int) config.getShopCost() + " " + currencies);
                    sender.sendMessage(colorCommand + "VIP Rank: " + colorText + (int) config.getVIPCost() + " " + currencies);
                    sender.sendMessage(colorCommand + "Clan: " + colorText + (int) config.getClanCost() + " " + currencies);
                    sender.sendMessage(colorCommand + "Clan Base: " + colorText + (int) config.getBaseCost() + " " + currencies);
                    sender.sendMessage(colorCommand + "City: " + colorText + (int) config.getCityCost() + " " + currencies);
                }
            } else if(subCommand.equalsIgnoreCase("pay")) {
                if(sender.hasPermission("udsplugin.money.pay")) {
                    if(args.length != 3) {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                        return true;
                    }
                    int payment;
                    try {
                        payment = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(colorError + "Not a valid amount.");
                        return true;
                    }
                    if(payment < 0) {
                        sender.sendMessage(colorError + "Not a valid amount.");
                        return true;
                    }
                    UDSPlayer target = PlayerUtils.matchUDS(args[1]);
                    if(target == null) {
                        sender.sendMessage(UDSMessage.NO_PLAYER);
                        return true;
                    }
                    String tagetName = target.getName();
                    if(!senderPlayer.hasEnough(payment)) {
                        sender.sendMessage(colorError + "You do not have that much money to give away.");
                        return true;
                    }
                    senderPlayer.takeMoney(payment);
                    target.addMoney(payment);
                    sender.sendMessage(colorMessage + "Payment sent.");
                    Player player = Bukkit.getPlayerExact(tagetName);
                    if(player != null) {
                        player.sendMessage(colorMessage + senderPlayer.getNick() + " just paid you " + payment + " " + currencies + ".");
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("grant")) {
                if(sender.hasPermission("udsplugin.money.grant")) {
                    if(args.length != 3) {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                        return true;
                    }
                    int payment;
                    try {
                        payment = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(colorError + "Not a valid amount.");
                        return true;
                    }
                    UDSPlayer target = PlayerUtils.matchUDS(args[1]);
                    if(target == null) {
                        sender.sendMessage(UDSMessage.NO_PLAYER);
                        return true;
                    }
                    target.addMoney(payment);
                    sender.sendMessage(colorMessage + target.getNick() + " was given " + payment + " " + currencies + ".");
                    final Player player = Bukkit.getPlayerExact(target.getName());
                    if(player != null) {
                        player.sendMessage(colorMessage + "You have been granted " + payment + " " + currencies + ".");
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("set")) {
                if(sender.hasPermission("udsplugin.money.set")) {
                    if(args.length == 3) {
                        final UDSPlayer targetPlayer = PlayerUtils.matchUDS(args[1]);
                        if(targetPlayer != null) {
                            if(args[2].matches("[0-9][0-9]*")) {
                                targetPlayer.setMoney(Integer.parseInt(args[2]));
                                sender.sendMessage(colorMessage + "You set " + targetPlayer.getNick() + "'s balance to " + Long.parseLong(args[2]) + " " + currencies + ".");
                            } else {
                                sender.sendMessage(UDSMessage.NO_NUMBER);
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
            } else if(subCommand.equalsIgnoreCase("rank")) {
                if(sender.hasPermission("udsplugin.money.rank")) {
                    showRanks(sender);
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if(subCommand.equalsIgnoreCase("help")) {
                sender.performCommand("help money");
            } else if(subCommand.equalsIgnoreCase("hide")) {
                if(args.length == 2) {
                    UDSPlayer player = PlayerUtils.matchUDS(args[1]);
                    if(player != null) {
                        sender.sendMessage(Color.MESSAGE + player.getNick() + "'s rank " + (player.toggleRank() ? "is now" : "is no longer") + " hidden.");
                    } else {
                        sender.sendMessage(UDSMessage.NO_PLAYER);
                    }
                } else {
                    sender.sendMessage(UDSMessage.BAD_ARGS);
                }
            } else {
                UDSPlayer targetPlayer = PlayerUtils.matchUDS(args[0]);
                if(targetPlayer != null) {
                    if(sender.hasPermission("udsplugin.money.other")) {
                        sender.sendMessage(colorMessage + targetPlayer.getName() + " has " + targetPlayer.getMoney() + " " + config.getCurrencies() + ".");
                    } else {
                        sender.sendMessage(UDSMessage.NO_PERM);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PLAYER);
                }
            }
        } else if(args.length == 0) {
            if(sender.hasPermission("udsplugin.money.balance")) {
                    sender.sendMessage(colorMessage + "You have " + senderPlayer.getMoney() + " " + currencies + ".");
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.performCommand("help money");
        }
        return true;
    }

    private void showRanks(Player player) {
        ArrayList<UDSPlayer> ranks = new ArrayList<UDSPlayer>(UDSPlugin.getPlayers().values());
        ArrayList<UDSPlayer> remove = new ArrayList<UDSPlayer>();
        for(UDSPlayer ranker : ranks) {
            if(ranker.hiddenRank()) {
                remove.add(ranker);
            }
        }
        for(UDSPlayer ranker : remove) {
            ranks.remove(ranker);
        }
        Collections.sort(ranks, new PlayerByMoney());
        int rank = ranks.indexOf(PlayerUtils.getUDS(player));
        player.sendMessage(Color.MESSAGE + "--- Top Ranking Players ---");
        int top = 5;
        if(ranks.size() < 5) {
            top = ranks.size();
        }
        for(int i = 1; i <= top; i++) {
            player.sendMessage(Color.TEXT + "" + i + ": " + ranks.get(ranks.size() - i).getNick() + ", " + ranks.get(ranks.size() - i).getMoney());
        }
        if(rank < ranks.size() - 6) {
            player.sendMessage(Color.TEXT + "Your rank: " + rank);
        }
    }
}

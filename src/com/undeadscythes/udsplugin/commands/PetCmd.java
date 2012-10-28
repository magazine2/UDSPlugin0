package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Request;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class PetCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public PetCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();


        if(args.length != 0) {
            final String subCommand = args[0].toLowerCase();
            final Player sender = Bukkit.getServer().getPlayer(senderName);
            final UDSPlayer player = PlayerUtils.matchUDS(senderName);
            if("give".equals(subCommand)) {
                if(sender.hasPermission("udsplugin.pet.give")) {
                    if(args.length == 2) {
                        if(player.getPetId() != null) {
                            for(final Iterator<Entity> i = sender.getWorld().getEntities().iterator(); i.hasNext();) {
                                final Entity entity = i.next();
                                if(entity.getUniqueId() == player.getPetId()) {
                                    final UDSPlayer target = PlayerUtils.matchUDS(args[1]);
                                    if(target != null) {
                                        final Player targetPlayer = Bukkit.getPlayerExact(target.getName());
                                        if(targetPlayer != null) {
                                            final Tameable pet = (Tameable) entity;
                                            pet.setOwner((AnimalTamer)targetPlayer);
                                            targetPlayer.sendMessage(Color.MESSAGE + PlayerUtils.matchUDS(senderName).getNick() + " has just sent you a pet.");
                                            sender.sendMessage(Color.MESSAGE + "You just sent your pet to " + target.getNick() + ".");
                                        } else {
                                            sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                                        }
                                    } else {
                                        sender.sendMessage(UDSMessage.NO_PLAYER);
                                    }
                                    break;
                                }
                            }
                        } else {
                            sender.sendMessage(UDSMessage.NO_PET);
                        }
                    } else {
                        sender.sendMessage(UDSMessage.BAD_ARGS);
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            } else if("sell".equals(subCommand)) {
                if(sender.hasPermission("udsplugin.pet.sell")) {
                    if(player.getPetId() != null) {
                        if(args.length == 3) {
                            if(args[2].matches("[0-9][0-9]*")) {
                                final Player target = PlayerUtils.matchOnlinePlayer(args[1]);
                                if(target != null) {
                                    final String targetName = target.getName();
                                    if(!targetName.equals(senderName)) {
                                        if(!UDSPlugin.getRequests().containsKey(targetName)) {
                                            final ChatColor messageColor = Color.MESSAGE;
                                            UDSPlugin.getRequests().put(targetName, new Request(targetName, senderName, Request.Type.PET, args[2], plugin.getConfig().getLong("request-timeout")));
                                            target.sendMessage(messageColor + "Do you want to buy " + senderName + "'s pet for " + Integer.parseInt(args[2]) + " " + plugin.getConfig().getString("currency.plural") + "?");
                                            target.sendMessage(messageColor + "Use /y or /n in response.");
                                            sender.sendMessage(messageColor + "Your offer has been sent to " + targetName + ".");
                                        } else {
                                            sender.sendMessage(Color.ERROR + targetName + " already has a pending request.");
                                        }
                                    } else {
                                        sender.sendMessage(UDSMessage.ERR_SELF);
                                    }
                                } else {
                                    sender.sendMessage(UDSMessage.PLAYER_NOT_ONLINE);
                                }
                            } else {
                                sender.sendMessage(UDSMessage.NO_NUMBER);
                            }
                        } else {
                            sender.sendMessage(UDSMessage.BAD_ARGS);
                        }
                    } else {
                        sender.sendMessage(Color.ERROR + "You do not have a pet to sell.");
                    }
                } else {
                    sender.sendMessage(UDSMessage.NO_PERM);
                }
            }
        }
        return true;
    }
}

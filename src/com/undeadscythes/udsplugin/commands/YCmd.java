package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Clan;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.Request;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.CmdUtils;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class YCmd implements CommandExecutor {
    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Player player = Bukkit.getServer().getPlayer(senderName);
        if(CmdUtils.perm(player, "y") && CmdUtils.hasRequest(player, senderName)) {
            Request request = UDSPlugin.getRequests().get(senderName);
            if(request.getType() == Request.Type.TP) {
                tpRequest(request.getRecipent(), request.getSender());
            } else if(request.getType() == Request.Type.CHALLENGE) {
                challengeRequest(request.getRecipent(), request.getSender(), request.getData());
            } else if(request.getType() == Request.Type.SHOP) {
                shopRequest(request.getRecipent(), request.getSender(), request.getData());
            } else if(request.getType() == Request.Type.HOME) {
                homeRequest(request.getRecipent(), request.getSender(), request.getData());
            } else if(request.getType() == Request.Type.CLAN) {
                clanRequest(request.getRecipent(), request.getData());
            } else if(request.getType() == Request.Type.PET) {
                petRequest(request.getRecipent(), request.getSender(), request.getData());
            }
        }
        return true;
    }

    private void tpRequest(String targetName, String teleporterName) {
        Player target = Bukkit.getPlayerExact(targetName);
        Player teleporter = Bukkit.getPlayerExact(teleporterName);
        if(target != null) {
            UDSPlugin.getPlayers().get(teleporterName).setBackPoint(teleporter.getLocation());
            teleporter.teleport(target);
        } else {
            teleporter.sendMessage(UDSMessage.UNAVAILABLE_PLAYER);
        }
        UDSPlugin.getRequests().remove(teleporterName);
    }

    private void shopRequest(String buyerName, String sellerName, String data) {
        Player seller = Bukkit.getPlayerExact(sellerName);
        Player buyer = Bukkit.getPlayerExact(buyerName);
        UDSPlayer udsBuyer = UDSPlugin.getPlayers().get(buyerName);
        if(seller != null) {
            int payment = Integer.parseInt(data);
            if(udsBuyer.hasEnough(payment)) {
                UDSHashMap<Region> regions = UDSPlugin.getRegions();
                if(!regions.containsKey(buyerName + "shop")) {
                    Region oldRegion = regions.get(sellerName + "shop");
                    regions.put(buyerName + "shop", new Region(buyerName + "shop", oldRegion.getMinVector(), oldRegion.getMaxVector(), oldRegion.getWarp(), buyerName));
                    regions.remove(sellerName + "shop");
                    udsBuyer.takeMoney(payment);
                    UDSPlugin.getPlayers().get(sellerName).addMoney(payment);
                    buyer.sendMessage(Color.MESSAGE + "You just bought " + sellerName + "'s shop for " + payment + " " + UDSPlugin.getUDSConfig().getCurrencies() + ".");
                    seller.sendMessage(Color.MESSAGE + "Yor shop has been sold to " + buyerName + " for " + payment + " " + UDSPlugin.getUDSConfig().getCurrencies() + ".");
                } else {
                    buyer.sendMessage(Color.ERROR + "You already own a shop.");
                    seller.sendMessage(Color.MESSAGE + "Your offer has been refused.");
                }
            } else {
                buyer.sendMessage(Color.ERROR + "You do not have enough money to buy the shop.");
                seller.sendMessage(Color.MESSAGE + "Your offer has been refused.");
            }
        } else {
            buyer.sendMessage(Color.ERROR + "Player is now unavailable.");
        }
        UDSPlugin.getRequests().remove(buyerName);
    }

    private void petRequest(String buyerName, String sellerName, String data) {
        Player seller = Bukkit.getPlayerExact(sellerName);
        Player buyer = Bukkit.getPlayerExact(buyerName);
        UDSPlayer udsBuyer = UDSPlugin.getPlayers().get(buyerName);
        if(seller != null) {
            int payment = Integer.parseInt(data);
            if(udsBuyer.hasEnough(payment)) {
                final UDSPlayer udsSeller = UDSPlugin.getPlayers().get(sellerName);
                for(final Iterator<Entity> i = seller.getWorld().getEntities().iterator(); i.hasNext();) {
                    final Entity entity = i.next();
                    if(entity.getUniqueId() == udsSeller.getPetId()) {
                        final Tameable pet = (Tameable) entity;
                        pet.setOwner((AnimalTamer)buyer);
                        buyer.sendMessage(Color.MESSAGE + "You bought " + udsSeller.getNick() + "'s pet.");
                        seller.sendMessage(Color.MESSAGE + "You just sold your pet to " + udsBuyer.getNick() + ".");
                        break;
                    }
                }
            } else {
                buyer.sendMessage(Color.ERROR + "You do not have enough money to buy the Pet.");
                seller.sendMessage(Color.MESSAGE + "Your offer has been refused.");
            }
        } else {
            buyer.sendMessage(Color.ERROR + "Player is now unavailable.");
        }
        UDSPlugin.getRequests().remove(udsBuyer.getName());
    }

    private void challengeRequest(String targetName, String challengerName, String data) {
        Player challenger = Bukkit.getPlayerExact(challengerName);
        Player target = Bukkit.getPlayerExact(targetName);
        if(challenger != null) {
            int wager = Integer.parseInt(data);
            UDSPlayer udsTarget = UDSPlugin.getPlayers().get(targetName);
            if(udsTarget.hasEnough(wager)) {
                UDSPlayer udsChallenger = UDSPlugin.getPlayers().get(challengerName);
                if(target.getGameMode() == GameMode.CREATIVE) {
                    target.setGameMode(GameMode.SURVIVAL);
                }
                if(challenger.getGameMode() == GameMode.CREATIVE) {
                    challenger.setGameMode(GameMode.SURVIVAL);
                }
                if(udsTarget.hasGodMode()) {
                    udsTarget.setGodMode(false);
                }
                if(udsChallenger.hasGodMode()) {
                    udsChallenger.setGodMode(false);
                }
                udsTarget.takeMoney(wager);
                udsChallenger.takeMoney(wager);
                udsTarget.newChallenge(challengerName, wager);
                udsChallenger.newChallenge(targetName, wager);
            } else {
                target.sendMessage(Color.ERROR + "You cannot match the wager.");
                challenger.sendMessage(Color.ERROR + udsTarget.getNick() + " cannot match your wager.");
            }
        } else {
            target.sendMessage(UDSMessage.UNAVAILABLE_PLAYER);
        }
        UDSPlugin.getRequests().remove(targetName);
    }

    private void homeRequest(String buyerName, String sellerName, String data) {
        Player seller = Bukkit.getPlayerExact(sellerName);
        Player buyer = Bukkit.getPlayer(buyerName);
        if(seller != null) {
            int payment = Integer.parseInt(data);
            UDSPlayer udsBuyer = UDSPlugin.getPlayers().get(buyerName);
            if(udsBuyer.hasEnough(payment)) {
                UDSHashMap<Region> regions = UDSPlugin.getRegions();
                if(!regions.containsKey(buyerName + "home")) {
                    Region oldRegion = regions.get(sellerName + "home");
                    regions.put(buyerName + "home", new Region(buyerName + "home", oldRegion.getMinVector(), oldRegion.getMaxVector(), oldRegion.getWarp(), buyerName));
                    regions.remove(sellerName + "home");
                    udsBuyer.takeMoney(payment);
                    UDSPlayer targetPlayer = UDSPlugin.getPlayers().get(sellerName);
                    targetPlayer.addMoney(payment);
                    buyer.sendMessage(Color.MESSAGE + "You just bought " + sellerName + "'s home for " + payment + " " + UDSPlugin.getUDSConfig().getCurrencies() + ".");
                    seller.sendMessage(Color.MESSAGE + "Yor home has been sold to " + buyerName + " for " + payment + " " + UDSPlugin.getUDSConfig().getCurrencies() + ".");
                } else {
                    buyer.sendMessage(Color.ERROR + "You already own a home.");
                    seller.sendMessage(Color.MESSAGE + "Your offer has been refused.");
                }
            } else {
                buyer.sendMessage(Color.ERROR + "You do not have enough money to buy the home.");
                seller.sendMessage(Color.MESSAGE + "Your offer has been refused.");
            }

        } else {
            buyer.sendMessage(Color.ERROR + "Player is now unavailable.");
        }
        UDSPlugin.getRequests().remove(buyerName);
    }

    private void clanRequest(String playerName, String clanName) {
        UDSPlayer udsPlayer = UDSPlugin.getPlayers().get(playerName);
        if(udsPlayer.isInClan()) {
            udsPlayer.leaveClan();
        }
        Clan clan = UDSPlugin.getClans().get(clanName);
        clan.addMember(playerName);
        udsPlayer.joinClan(clanName);
        for(Iterator<String> i = clan.getMembers().iterator(); i.hasNext();) {
            final Player clanMember = Bukkit.getPlayerExact(i.next());
            if(clanMember != null) {
                clanMember.sendMessage(Color.CLAN + "[" + clanName + "] " + udsPlayer.getNick() + " has joined the clan.");
            }
        }
        if(UDSPlugin.getRegions().get(clan.getName() + "base") != null) {
            UDSPlugin.getRegions().get(clan.getName() + "base").addMember(playerName);
        }
        UDSPlugin.getRequests().remove(playerName);
    }
}

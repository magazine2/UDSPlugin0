package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhoCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public WhoCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        String commandName = command.getName();
        ChatColor messageColor = Color.MESSAGE;
        UDSHashMap<UDSPlayer> serverPlayers = UDSPlugin.getPlayers();

        if(commandName.equalsIgnoreCase("who")) {
            if(sender.hasPermission("udsplugin.who")) {
                Player[] players = server.getOnlinePlayers();
                sender.sendMessage(messageColor + "--- Online Players (" + players.length + "/" + server.getMaxPlayers() + ") ---");
                String dList = "";
                String mList = "";
                String wList = "";
                String vList = "";
                String bList = "";
                String aList = "";
                String oList = "";
                for(int i = 0; i < players.length; i++) {
                    String tag = "";
                    if(PlayerUtils.matchUDS(players[i].getName()).hasGodMode()) {
                        tag = "[G";
                    }
                    if(players[i].getGameMode() == GameMode.CREATIVE) {
                        if(tag.equals("")) {
                            tag = "[C]";
                        }
                        else {
                            tag += "C]";
                        }
                    } else if(!tag.equals("")) {
                        tag += "]";
                    }
                    String playerName = serverPlayers.get(players[i].getName()).getNick();
                    if(serverPlayers.get(players[i].getName()).hasNick()) {
                        playerName = "~" + playerName;
                    }
                    if(players[i].hasPermission("group.default")) {
                        dList = dList + tag + playerName + " ";
                    }
                    if(players[i].hasPermission("group.member")) {
                        mList = mList + tag + playerName + " ";
                    }
                    if(players[i].hasPermission("group.warden")) {
                        wList = wList + tag + playerName + " ";
                    }
                    if(players[i].hasPermission("group.vip")) {
                        vList = vList + tag + playerName + " ";
                    }
                    if(players[i].hasPermission("group.mod")) {
                        bList = bList + tag + playerName + " ";
                    }
                    if(players[i].hasPermission("group.admin")) {
                        aList = aList + tag + playerName + " ";
                    }
                    if(players[i].hasPermission("group.owner")) {
                        oList = oList + tag + playerName + " ";
                    }
                }
                if(!"".equals(dList)) {
                    sender.sendMessage(Color.GROUP_DEFAULT + dList);
                }
                if(!"".equals(mList)) {
                    sender.sendMessage(Color.GROUP_MEMBER + mList);
                }
                if(!"".equals(wList)) {
                    sender.sendMessage(Color.GROUP_WARDEN + wList);
                }
                if(!"".equals(vList)) {
                    sender.sendMessage(Color.GROUP_VIP + vList);
                }
                if(!"".equals(bList)) {
                    sender.sendMessage(Color.GROUP_MOD + bList);
                }
                if(!"".equals(aList)) {
                    sender.sendMessage(Color.GROUP_ADMIN + aList);
                }
                if(!"".equals(oList)) {
                    sender.sendMessage(Color.GROUP_OWNER + oList);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_PERM);
            }
        } else {
            sender.sendMessage(UDSMessage.BAD_COMMAND);
        }
        return true;
    }
}

package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSString;
import com.undeadscythes.udsplugin.Warp;
import com.undeadscythes.udsplugin.utilities.ChatUtils;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import java.io.File;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {
    private final transient UDSPlugin plugin;

    public AsyncPlayerChat(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public final void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(player.getName());
        serverPlayer.newChatTime(System.currentTimeMillis());
        String playerName = serverPlayer.getNick();
        final Server server = player.getServer();
        String logMessage = player.getName() + ": " + event.getMessage();
        plugin.getLogger().info(logMessage);
        if("priv".equals(serverPlayer.getChatChannel())) {
            ChatUtils.sendPrivMessage(player, event.getMessage());
        } else if("clan".equals(serverPlayer.getChatChannel())) {
            ChatUtils.sendClanMessage(player, event.getMessage());
        } else if("admn".equals(serverPlayer.getChatChannel())) {
            ChatUtils.sendAdminMessage(player, event.getMessage());
        } else if("norm".equals(serverPlayer.getChatChannel())) {
            if(serverPlayer.getChatPerSec() < 1) {
                player.kickPlayer(UDSMessage.KICK_SPAM);
                server.broadcastMessage(Color.BROADCAST + playerName + " has been kicked for spamming chat.");
            } else {
                final UDSString message = new UDSString(event.getMessage());
                if(message.censor() && !serverPlayer.isInPrison()) {
                    if(serverPlayer.hasEnough(1)) {
                        server.broadcastMessage(Color.BROADCAST + playerName + " just put 1 credit in the swear jar.");
                        final FileConfiguration config = plugin.getConfig();
                        config.set("swearjar", config.getInt("swearjar") + 1);
                        try {
                            config.save(UDSPlugin.getMainDir() + File.separator + "config.yml");
                        } catch (IOException exception) {
                            plugin.getLogger().info(exception.toString());
                        }
                        player.sendMessage(Color.ERROR + "Please keep bad language to a minimum.");
                        serverPlayer.takeMoney(1);
                    } else {
                        if(!serverPlayer.isInPrison()) {
                            final Warp warp = UDSPlugin.getWarps().get("jailin");
                            if(warp != null) {
                                final ChatColor messageColor = Color.MESSAGE;
                                player.getWorld().strikeLightningEffect(player.getLocation());
                                player.teleport(LocationUtils.findSafePlace(warp.getLocation()));
                                player.sendMessage(messageColor + "You have been jailed for 1 minute for swearing.");
                                player.sendMessage(messageColor + "You can get out early by paying a 1 credit fine with /paybail.");
                                serverPlayer.imprison(System.currentTimeMillis(), 1, 1);
                                player.setGameMode(GameMode.SURVIVAL);
                                serverPlayer.setGodMode(true);
                                server.broadcastMessage(Color.BROADCAST + serverPlayer.getNick() + " has been jailed.");
                            } else {
                                player.kickPlayer("You have been kicked for swearing.");
                            }
                        }
                    }
                }
                if(UDSPlugin.getUDSConfig().useChatBrackets()) {
                    playerName = "[" + playerName + "]";
                }
                if(serverPlayer.isInPrison()) {
                    player.sendMessage(UDSMessage.NO_CHAT);
                } else if(player.hasPermission("group.default")) {
                    ChatUtils.sendNormMessage(player.getName(), Color.GROUP_DEFAULT + playerName + Color.TEXT + ": " + message.getString());
                } else if(player.hasPermission("group.member")) {
                    ChatUtils.sendNormMessage(player.getName(), Color.GROUP_MEMBER + playerName + Color.TEXT + ": " + message.getString());
                } else if(player.hasPermission("group.vip")) {
                    ChatUtils.sendNormMessage(player.getName(), Color.GROUP_VIP + playerName + Color.TEXT + ": " + message.getString());
                } else if(player.hasPermission("group.warden")) {
                    ChatUtils.sendNormMessage(player.getName(), Color.GROUP_WARDEN + playerName + Color.TEXT + ": " + message.getString());
                } else if(player.hasPermission("group.mod")) {
                    ChatUtils.sendNormMessage(player.getName(), Color.GROUP_MOD + playerName + Color.TEXT + ": " + message.getString());
                } else if(player.hasPermission("group.admin")) {
                    ChatUtils.sendNormMessage(player.getName(), Color.GROUP_ADMIN + playerName + Color.TEXT + ": " + message.getString());
                } else if(player.hasPermission("group.owner")) {
                    ChatUtils.sendNormMessage(player.getName(), Color.GROUP_OWNER + playerName + Color.TEXT + ": " + message.getString());
                }
            }
        }
        event.setCancelled(true);
    }
}

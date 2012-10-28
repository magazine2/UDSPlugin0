package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockdownCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public LockdownCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.lockdown")) {
            if(args.length == 0) {
                if(!UDSPlugin.getServerInLockdown()) {
                    UDSPlugin.setServerInLockdown(true);
                    UDSPlugin.getPlayers().get(senderName).setLockdownPass(true);
                    server.broadcastMessage(Color.BROADCAST + "Server is now in lockdown.");
                } else {
                    UDSPlugin.setServerInLockdown(false);
                    server.broadcastMessage(Color.BROADCAST + "Server is no longer in lockdown.");
                    for(Map.Entry<String, UDSPlayer> i : UDSPlugin.getPlayers().entrySet()) {
                        i.getValue().setLockdownPass(false);
                    }
                }
            } else if(args.length == 1) {
                final UDSPlayer serverPlayer = PlayerUtils.matchUDS(args[0]);
                if(serverPlayer != null) {

                    final String targetName = serverPlayer.getNick();
                    if(serverPlayer.hasLockdownPass()) {
                        serverPlayer.setLockdownPass(false);
                        sender.sendMessage(Color.MESSAGE + targetName + "'s lockdown pass has been removed.");
                    } else {
                        serverPlayer.setLockdownPass(true);
                        sender.sendMessage(Color.MESSAGE + targetName + " has been issued with a lockdown pass.");
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
        return true;
    }
}

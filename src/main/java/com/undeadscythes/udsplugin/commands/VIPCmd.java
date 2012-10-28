package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VIPCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public VIPCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("group.vip")) {
            final int timeLeft = (int) (plugin.getConfig().getLong("vip.time") + UDSPlugin.getPlayers().get(senderName).getVipTime() - System.currentTimeMillis());
            sender.sendMessage(Color.MESSAGE + "You still have " + TimeUtils.findTime(timeLeft) + " of VIP time remaining.");
        } else if(sender.hasPermission("udsplugin.vip")) {
            final UDSConfig config = UDSPlugin.getUDSConfig();
            final UDSPlayer senderPlayer = UDSPlugin.getPlayers().get(senderName);
            if(senderPlayer.hasEnough(config.getVIPCost())) {
                sender.sendMessage(Color.MESSAGE + "Enjoy your new VIP status.");
                senderPlayer.takeMoney(config.getVIPCost());
                server.dispatchCommand(Bukkit.getConsoleSender(), "permissions player setgroup " + senderName + " vip");
                senderPlayer.newVip(System.currentTimeMillis(), config.getVipSpawns());
            } else {
                sender.sendMessage(UDSMessage.NO_MONEY);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

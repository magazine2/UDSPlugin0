package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptRulesCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public AcceptRulesCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);
        if(sender.hasPermission("udsplugin.acceptrules")) {
            final int cost = UDSPlugin.getUDSConfig().getPromotionCost();
            final UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(senderName);
            if(serverPlayer.hasEnough(cost)) {
                serverPlayer.takeMoney(cost);
                server.broadcastMessage(Color.BROADCAST + senderName + " has accepted the rules.");
                sender.sendMessage(Color.MESSAGE + "Thanks for accepting the rules, enjoy your stay in Minecraftopia.");
                server.dispatchCommand(Bukkit.getConsoleSender(), "permissions player setgroup " + senderName + " member");
            } else {
                sender.sendMessage(UDSMessage.PROMOTE_NO_MONEY);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

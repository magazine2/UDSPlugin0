package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SignsCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public SignsCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.signs")) {
            sender.sendMessage(Color.MESSAGE + "Special signs available to you and format (lines 1-4):");
            if(sender.hasPermission("udsplugin.sign.checkpoint")) {
                sender.sendMessage(Color.COMMAND + "Checkpoint - " + Color.TEXT + "1: [checkpoint], 2-4: Anything");
            }
            if(sender.hasPermission("udsplugin.sign.minecart")) {
                sender.sendMessage(Color.COMMAND + "Minecart - " + Color.TEXT + "1: [minecart], 2-4: Anything");
            }
            if(sender.hasPermission("udsplugin.sign.item")) {
                sender.sendMessage(Color.COMMAND + "Item - " + Color.TEXT + "1: [item], 2: <item>, 3: <amount>, 4: Anything");
            }
            if(sender.hasPermission("udsplugin.sign.prize")) {
                sender.sendMessage(Color.COMMAND + "Prize - " + Color.TEXT + "1: [prize], 2: <item>, 3: <amount>, 4: Anything");
            }
            if(sender.hasPermission("udsplugin.sign.warp")) {
                sender.sendMessage(Color.COMMAND + "Warp - " + Color.TEXT + "1: [warp], 2: <warp>, 3-4: Anything");
            }
            if(sender.hasPermission("udsplugin.sign.spleef")) {
                sender.sendMessage(Color.COMMAND + "Spleef - " + Color.TEXT + "1: [spleef], 2: <region>, 3-4: Anything");
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class FireworkCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public FireworkCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.firework")) {
            final TNTPrimed tnt = sender.getWorld().spawn(sender.getLocation().add(0, 1, 0), TNTPrimed.class);
            tnt.setVelocity(sender.getLocation().getDirection().multiply(3));
            tnt.setFuseTicks(15);
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

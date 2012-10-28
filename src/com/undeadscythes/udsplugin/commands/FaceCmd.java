package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Direction;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.CmdUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FaceCmd implements CommandExecutor {
    @Override
    public final boolean onCommand(final CommandSender cmdSender, final Command cmd, final String label, final String[] args) {
        final Player sender = PlayerUtils.getOnline(cmdSender.getName());
        if(CmdUtils.perm(sender, "face")) {
            if(args.length == 1) {
                Location view = sender.getLocation();
                view.setYaw(Direction.getDirection(args[0]).toYaw());
                sender.teleport(view);
            } else if(args.length == 0) {
                sender.sendMessage(Color.MESSAGE + "You are facing " + PlayerUtils.getDirection8(sender).toString() + ".");
            } else {
                sender.sendMessage(UDSMessage.BAD_ARGS);
            }
        }
        return true;
    }
}

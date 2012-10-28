package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.CmdUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PowertoolCmd implements CommandExecutor {
    @Override
    public final boolean onCommand(final CommandSender cmdSender, final Command cmd, final String label, final String[] args) {
        final Player sender = PlayerUtils.getOnline(cmdSender.getName());
        if(CmdUtils.perm(sender, "powertool")) {
            if(args.length == 0) {
                PlayerUtils.matchUDS(sender).setPowertool("");
                sender.sendMessage(UDSMessage.POWERTOOL_OFF);
            } else {
                if(args[0].startsWith("/")) {
                    sender.sendMessage(UDSMessage.POWERTOOL_SLASH);
                } else {
                    PlayerUtils.matchUDS(sender).setPowertool(StringUtils.join(args, " "));
                    PlayerUtils.matchUDS(sender).setTool(sender.getItemInHand().getTypeId());
                    sender.sendMessage(UDSMessage.POWERTOOL_ON);
                }
            }
        }
        return true;
    }
}

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.utilities.ItemUtils;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ResetQuarryCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public ResetQuarryCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.resetquarry")) {
            int count = 0;
            for(Map.Entry<String, Region> i : UDSPlugin.getRegions().entrySet()) {
                if(i.getKey().contains("quarry:")) {
                    count++;
                    final ItemStack item = ItemUtils.findItem(i.getKey().replace("quarry:", ""));
                    final int matId = item.getTypeId();
                    final Byte matData = item.getData().getData();
                    for(int ix = (int) i.getValue().getMinVector().getX(); ix <= (int) i.getValue().getMaxVector().getX(); ix++) {
                        for(int iy = (int) i.getValue().getMinVector().getY(); iy <= (int) i.getValue().getMaxVector().getY(); iy++) {
                            for(int iz = (int) i.getValue().getMinVector().getZ(); iz <= (int) i.getValue().getMaxVector().getZ(); iz++) {
                                if(i.getValue().getWorld().getBlockAt(ix, iy, iz).getType() != Material.BEDROCK) {
                                    i.getValue().getWorld().getBlockAt(ix, iy, iz).setTypeIdAndData(matId, matData, false);
                                }
                            }
                        }
                    }
                }
            }
            sender.sendMessage(Color.MESSAGE + "Reset " + count + " quarries.");
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public DebugCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);
        if(sender.hasPermission("udsplugin.debug")) {
            if("maze".equals(args[0])) {
                int x = sender.getLocation().getBlockX();
                int y = sender.getLocation().getBlockY();
                int z = sender.getLocation().getBlockZ();
                for(int i = -5; i < 5; i++) {
                    for(int j = -5; j < 5; j++) {
                        Block block = sender.getWorld().getBlockAt(x + i, y - 1, z + j);
                        if(block.getTypeId() == 0 || block.getTypeId() == 17) {
                            block.setType(Material.LOG);
                            block = block.getRelative(BlockFace.UP);
                            block.setType(Material.LEAVES);
                            block.setData((byte)1);
                            block = block.getRelative(BlockFace.UP);
                            block.setType(Material.LEAVES);
                            block.setData((byte)1);
                            block = block.getRelative(BlockFace.UP);
                            block.setType(Material.LEAVES);
                            block.setData((byte)1);
                        }
                    }
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

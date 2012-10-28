package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnerCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public SpawnerCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Server server = Bukkit.getServer();
        final Player sender = server.getPlayer(senderName);


        if(sender.hasPermission("udsplugin.spawner")) {
            final Block block = sender.getTargetBlock(null, 100);
            if(block.getType() == Material.MOB_SPAWNER) {
                final EntityType mob = EntityType.fromName(args[0]);
                if(mob != null) {
                    ((CreatureSpawner)block.getState()).setSpawnedType(mob);
                    sender.sendMessage(Color.MESSAGE + "Spawner set to " + mob.toString().toLowerCase().replace("_", " ") + ".");
                } else {
                    sender.sendMessage(UDSMessage.BAD_MOB);
                }
            } else {
                sender.sendMessage(UDSMessage.NO_SPAWNER);
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

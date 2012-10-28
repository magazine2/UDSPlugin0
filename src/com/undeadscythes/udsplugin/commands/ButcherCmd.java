package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSConfig;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlugin;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

public class ButcherCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public ButcherCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        String senderName = commandSender.getName();
        Server server = Bukkit.getServer();
        Player sender = server.getPlayer(senderName);
        UDSConfig config = UDSPlugin.getUDSConfig();
        World world = server.getWorld(config.getWorldName());
        if(sender.hasPermission("udsplugin.butcher")) {
            List<Entity> entities = world.getEntities();
            Location here = sender.getLocation();
            double range = config.getButcherRange();
            int count = 0;
            if(args.length == 0) {
                for(Iterator<Entity> i = entities.iterator(); i.hasNext();) {
                    Entity entity = i.next();
                    if(entity instanceof Monster || entity instanceof Slime || entity instanceof Flying) {
                        Location location = entity.getLocation();
                        if(location.getX() > here.getX() + range) {
                            continue;
                        }
                        if(location.getX() < here.getX() - range) {
                            continue;
                        }
                        if(location.getY() > here.getY() + range) {
                            continue;
                        }
                        if(location.getY() < here.getY() - range) {
                            continue;
                        }
                        if(location.getZ() > here.getZ() + range) {
                            continue;
                        }
                        if(location.getZ() < here.getZ() - range) {
                            continue;
                        }
                        entity.remove();
                        count++;
                    }
                }
            } else if(args[0].equalsIgnoreCase("all")) {
                for(Iterator<Entity> i = entities.iterator(); i.hasNext();) {
                    Entity entity = i.next();
                    if(entity instanceof Creature || entity instanceof Slime || entity instanceof Flying) {
                        Location location = entity.getLocation();
                        if(location.getX() > here.getX() + range) {
                            continue;
                        }
                        if(location.getX() < here.getX() - range) {
                            continue;
                        }
                        if(location.getY() > here.getY() + range) {
                            continue;
                        }
                        if(location.getY() < here.getY() - range) {
                            continue;
                        }
                        if(location.getZ() > here.getZ() + range) {
                            continue;
                        }
                        if(location.getZ() < here.getZ() - range) {
                            continue;
                        }
                        entity.remove();
                        count++;
                    }
                }
            }
            sender.sendMessage(Color.MESSAGE + "Butchered " + count + " mobs.");
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

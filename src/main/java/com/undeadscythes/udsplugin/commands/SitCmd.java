package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.UDSMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Stairs;
import org.bukkit.util.Vector;

public class SitCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public SitCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String senderName = commandSender.getName();
        final Player sender = Bukkit.getServer().getPlayer(senderName);


        if(sender.hasPermission("udsplugin.sit")) {
            if(!sender.isInsideVehicle()) {
                final Block target = sender.getTargetBlock(null, 3);
                final boolean a = target.getTypeId() == 53;
                final boolean b = target.getTypeId() == 67;
                final boolean c = target.getTypeId() == 108;
                final boolean d = target.getTypeId() == 109;
                final boolean e = target.getTypeId() == 114;
                final boolean f = target.getTypeId() == 128;
                if(a || b || c || d || e || f) {
                    //final Entity arrow = sender.getWorld().spawn(target.getLocation().add(0.5, 0.5, 0.5), Arrow.class);
                    //arrow.setPassenger(sender);
                    final Item seat = sender.getWorld().dropItemNaturally(target.getLocation(), new ItemStack(Material.SNOW_BALL));
                    seat.setPickupDelay(2147483647);
                    seat.teleport(target.getLocation().add(0.5, -0.5, 0.5));
                    seat.setVelocity(new Vector(0, 0, 0));
                    final Stairs chair = (Stairs)target.getState().getData();
                    Location view = new Location(sender.getWorld(), sender.getLocation().getX(), sender.getLocation().getY(), sender.getLocation().getZ());
                    if(chair.getDescendingDirection() == BlockFace.NORTH) {
                        view.setYaw(90);
                    } else if(chair.getDescendingDirection() == BlockFace.EAST) {
                        view.setYaw(180);
                    } else if(chair.getDescendingDirection() == BlockFace.SOUTH) {
                        view.setYaw(270);
                    } else {
                        view.setYaw(0);
                    }
                    sender.teleport(view);
                    seat.setPassenger(sender);
                }
            } else {
                final Entity seat = sender.getVehicle();
                seat.eject();
                seat.remove();
                final Stairs chair = (Stairs)sender.getWorld().getBlockAt(sender.getLocation()).getState().getData();
                if(chair.getDescendingDirection() == BlockFace.NORTH) {
                        sender.teleport(sender.getLocation().add(-1, 0, 0));
                    } else if(chair.getDescendingDirection() == BlockFace.EAST) {
                        sender.teleport(sender.getLocation().add(0, 0, -1));
                    } else if(chair.getDescendingDirection() == BlockFace.SOUTH) {
                        sender.teleport(sender.getLocation().add(1, 0, 0));
                    } else {
                        sender.teleport(sender.getLocation().add(0, 0, 1));
                    }
            }
        } else {
            sender.sendMessage(UDSMessage.NO_PERM);
        }
        return true;
    }
}

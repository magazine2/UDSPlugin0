package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreak implements Listener {
    @EventHandler
    public final void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if(PlayerUtils.getUDS(player).isInPrison()) {
            event.setCancelled(true);
            player.sendMessage(UDSMessage.IN_PRISON);
            return;
        }
        if(!RegionUtils.canBuildHere(event.getBlock().getLocation(), player)) {
            event.setCancelled(true);
            player.sendMessage(UDSMessage.CANT_BUILD);
        }
        final Block block = event.getBlock();
        if(block.getType() == Material.WALL_SIGN && !player.isSneaking()) {
            final Sign sign = (Sign)block.getState();
            if(sign.getLine(0).equals(Color.SIGN + "[CHECKPOINT]")
            || sign.getLine(0).equals(Color.SIGN + "[MINECART]")
            || sign.getLine(0).equals(Color.SIGN + "[PRIZE]")
            || sign.getLine(0).equals(Color.SIGN + "[ITEM]")
            || sign.getLine(0).equals(Color.SIGN + "[WARP]")
            || sign.getLine(0).equals(Color.SIGN + "[SPLEEF]")) {
                event.setCancelled(true);
            }
        } else if(block.getType() == Material.MOB_SPAWNER && !event.isCancelled()) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.MOB_SPAWNER));
        } else if(block.getType() == Material.CHEST) {
            Chest chest = (Chest)block;
            if(chest.getInventory().getContents().length != 0) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(UDSMessage.CHEST_NOT_EMPTY);
            }
        }
        if(block.getType() == Material.SNOW_BLOCK) {
            UDSHashMap<Region> regions = RegionUtils.findRegionsHere(block.getLocation());
            for(Map.Entry<String, Region> i : regions.entrySet()) {
                if(i.getValue().getName().toLowerCase().contains("spleef")) {
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                    event.getPlayer().setItemInHand(new ItemStack(event.getPlayer().getItemInHand()));
                    event.getPlayer().updateInventory();
                }
            }
        }
    }
}

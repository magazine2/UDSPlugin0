package com.undeadscythes.udsplugin.listeners;

import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.Region;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSMessage;
import com.undeadscythes.udsplugin.UDSPlayer;
import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Warp;
import com.undeadscythes.udsplugin.utilities.ItemUtils;
import com.undeadscythes.udsplugin.utilities.LocationUtils;
import com.undeadscythes.udsplugin.utilities.PlayerUtils;
import com.undeadscythes.udsplugin.utilities.RegionUtils;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Player-World interaction events.
 * @author UDS
 */
public class PlayerInteract implements Listener {
    private final transient UDSPlugin plugin;

    /**
     * Constructor.
     * @param plugin UDSPlugin.
     */
    public PlayerInteract(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Fired when a player clicks something.
     * @param event Event that occurred.
     */
    @EventHandler
    public final void onPlayerInteract(final PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Material inHand = player.getItemInHand().getType();
        Block block = event.getClickedBlock();
        UDSPlayer udsPlayer = PlayerUtils.matchUDS(player.getName());
        if(action == Action.LEFT_CLICK_AIR) {
            if(inHand == Material.COMPASS && player.hasPermission("udsplugin.compass")) {
                compassTo(player);
                event.setCancelled(true);
            }
        } else if(action == Action.LEFT_CLICK_BLOCK) {
            if(inHand == Material.COMPASS && player.hasPermission("udsplugin.compass")) {
                compassTo(player);
                event.setCancelled(true);
            } else if(inHand == Material.STICK && player.hasPermission("udsplugin.wand")) {
                wand1(player, block);
                event.setCancelled(true);
            } else if(block.getType() == Material.WALL_SIGN) {
                sign(player, (Sign)block.getState());
                event.setCancelled(true);
            } else {
                event.setCancelled(lockCheck(block, player));
            }
        } else if(action == Action.RIGHT_CLICK_AIR) {
            if(inHand == Material.PAPER && player.hasPermission("udsplugin.paper.complex")) {
                paperComplex(player, player.getLocation());
                event.setCancelled(true);
            } else if(inHand == Material.PAPER && player.hasPermission("udsplugin.paper.simple")) {
                paperSimple(player, block.getLocation());
                event.setCancelled(true);
            } else if(inHand == Material.COMPASS && player.hasPermission("udsplugin.compass")) {
                compassThru(player);
                event.setCancelled(true);
            } else if(!"".equals(udsPlayer.getPowertool()) && inHand.getId() == udsPlayer.getTool()) {
                powertool(player);
                event.setCancelled(true);
            }
        } else if(action == Action.RIGHT_CLICK_BLOCK) {
            if(inHand == Material.STICK && player.hasPermission("udsplugin.wand")) {
                wand2(player, block);
                event.setCancelled(true);
            } else if(inHand == Material.PAPER && player.hasPermission("udsplugin.paper.complex")) {
                paperComplex(player, block.getLocation());
                event.setCancelled(true);
            } else if(inHand == Material.PAPER && player.hasPermission("udsplugin.paper.simple")) {
                paperSimple(player, block.getLocation());
                event.setCancelled(true);
            } else if(inHand == Material.COMPASS) {
                compassThru(player);
                event.setCancelled(true);
            } else if(inHand == Material.MONSTER_EGG && block.getType() == Material.MOB_SPAWNER) {
                setMobSpawner(block, player);
                event.setCancelled(true);
            } else if(block.getType() == Material.WALL_SIGN) {
                sign(player, (Sign)block.getState());
                event.setCancelled(true);
            } else if(!"".equals(udsPlayer.getPowertool()) && inHand.getId() == udsPlayer.getTool()) {
                powertool(player);
                event.setCancelled(true);
            } else {
                event.setCancelled(lockCheck(block, player) || bonemealCheck(block, player));
            }
        }
    }

    /**
     * Powertool events.
     * @param player Player using a powertool.
     */
    public void powertool(final Player player) {
        player.performCommand(PlayerUtils.matchUDS(player.getName()).getPowertool());
    }

    /**
     * Check before applying bonemeal effects.
     * @param location Location of block clicked.
     * @param player Player using bonemeal.
     * @return
     */
    public boolean bonemealCheck(final Block block, final Player player) {
        return player.getItemInHand().getType() == Material.INK_SACK &&
               player.getItemInHand().getData().getData() == (byte)15 &&
               !RegionUtils.canBuildHere(block.getLocation(), player);
    }

    /**
     * Check if a region is locked.
     * @param block Block the player is clicking.
     * @param player Player who is interacting.
     * @return
     */
    public boolean lockCheck(final Block block, final Player player) {
        final Material material = block.getType();
        if(material == Material.WOODEN_DOOR
        || material == Material.IRON_DOOR_BLOCK
        || material == Material.STONE_BUTTON
        || material == Material.LEVER
        || material == Material.TRAP_DOOR
        || material == Material.FENCE_GATE) {
            final Location location = block.getLocation();
            if(!RegionUtils.canBuildHere(location, player) && RegionUtils.isLocked(location)) {
                player.sendMessage(UDSMessage.CANT_DO_THAT_HERE);
                return true;
            }
        }
        return false;
    }

    /**
     * Check to change mob spawner type.
     * @param block Block the was clicked.
     * @param player Player who clicked.
     */
    public void setMobSpawner(final Block block, final Player player) {
        byte itemData = player.getItemInHand().getData().getData();
        player.setItemInHand(new ItemStack(Material.MONSTER_EGG, player.getItemInHand().getAmount() - 1, (short)0, itemData));
        ((CreatureSpawner)block.getState()).setSpawnedType(EntityType.fromId(itemData));
        player.sendMessage(Color.MESSAGE + "Spawner set.");
    }

    /**
     * Check if wand was used.
     * @param player Player using wand.
     * @param action Action type of event.
     * @param block Block clicked.
     * @return <code>true</code> if event needs to be cancelled.
     */
    public void wand1(final Player player, final Block block) {
        UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(player.getName());
        serverPlayer.setEditPoint1(new Vector(block.getX(), block.getY(), block.getZ()));
        player.sendMessage(UDSMessage.MSG_POINT_1);
        if(serverPlayer.getEditPoint1() != null && serverPlayer.getEditPoint2() != null) {
            player.sendMessage(Color.MESSAGE.toString() + RegionUtils.findVolume(serverPlayer.getEditPoint1(), serverPlayer.getEditPoint2()) + " blocks selected.");
        }
    }

    public void wand2(final Player player, final Block block) {
        UDSPlayer serverPlayer = UDSPlugin.getPlayers().get(player.getName());
        serverPlayer.setEditPoint2(new Vector(block.getX(), block.getY(), block.getZ()));
        player.sendMessage(UDSMessage.MSG_POINT_2);
        if(serverPlayer.getEditPoint1() != null && serverPlayer.getEditPoint2() != null) {
            player.sendMessage(Color.MESSAGE.toString() + RegionUtils.findVolume(serverPlayer.getEditPoint1(), serverPlayer.getEditPoint2()) + " blocks selected.");
        }
    }

    /**
     * Check if player is using a compass.
     * @param action Action type of event.
     * @param player Player using compass.
     * @param block Block clicked.
     * @param blockFace Face of block clicked.
     */
    public void compassTo(final Player player) {
        Location location = player.getTargetBlock(null, UDSPlugin.getUDSConfig().getCompassRange()).getLocation();
        location.setYaw(player.getLocation().getYaw());
        location.setPitch(player.getLocation().getPitch());
        player.teleport(LocationUtils.findSafePlace(location));
    }

    public void compassThru(final Player player) {
        List<Block> LOS =  player.getLastTwoTargetBlocks(null, 5);
        if(LOS.size() == 1) {
            return;
        }
        Location location = LOS.get(1).getRelative(LOS.get(0).getFace(LOS.get(1))).getLocation();
        location.setYaw(player.getLocation().getYaw());
        location.setPitch(player.getLocation().getPitch());
        player.teleport(LocationUtils.findSafePlace(location));
    }

    /**
     * Check if player is using paper.
     * @param action Action type of event.
     * @param player Player using paper.
     * @param block Block clicked.
     */
    public void paperSimple(final Player player, final Location location) {
        if(!RegionUtils.isInOpenArea(location)) {
            UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(location);
            for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                Region region = i.getValue();
                if(region.getOwner().equalsIgnoreCase(player.getName())) {
                    player.sendMessage(Color.MESSAGE + "You own this block.");
                } else if(region.getMembers().contains(player.getName())) {
                    player.sendMessage(Color.MESSAGE + "Your room mate owns this block.");
                } else {
                    player.sendMessage(Color.MESSAGE + "Somebody else owns this block.");
                }
            }
        } else {
            player.sendMessage(Color.MESSAGE + "No regions here.");
        }
    }

    public void paperComplex(final Player player, final Location location) {
        if(!RegionUtils.isInOpenArea(location)) {
            UDSHashMap<Region> testRegions = RegionUtils.findRegionsHere(location);
            for(Map.Entry<String, Region> i : testRegions.entrySet()) {
                Region region = i.getValue();
                player.sendMessage(Color.MESSAGE + "--- Region " + i.getKey() + " ---");
                player.sendMessage(Color.TEXT + "Owner: " + region.getOwner());
                player.sendMessage(Color.TEXT + "Members: " + StringUtils.join(region.getMembers().toArray(), " "));
            }
        } else {
            player.sendMessage(Color.MESSAGE + "No regions here.");
        }
    }

    /**
     * Check if player clicked a sign.
     * @param player Player using a sign.
     * @param block Block clicked.
     */
    public void sign(final Player player, final Sign sign) {
        if(sign.getLine(0).equals(Color.SIGN + "[CHECKPOINT]")) {
            if(player.hasPermission("udsplugin.checkpoint")) {
                UDSPlayer serverPlayer = PlayerUtils.matchUDS(player.getName());
                serverPlayer.setCheckPoint(player.getLocation());
                player.sendMessage(Color.MESSAGE + "Checkpoint set. Use /check to return here. Good luck.");
            } else {
                player.sendMessage(UDSMessage.CANT_DO_THAT);
            }
        } else if(sign.getLine(0).equals(Color.SIGN + "[MINECART]")) {
            if(player.hasPermission("udsplugin.minecart")) {
                Location location = sign.getBlock().getLocation();
                location.setX(location.getBlockX() + 0.5);
                location.setY(location.getBlockY() - 0.5);
                location.setZ(location.getBlockZ() + 0.5);
                plugin.getServer().getWorld(location.getWorld().getName()).spawn(location, Minecart.class);
            } else {
                player.sendMessage(UDSMessage.CANT_DO_THAT);
            }
        } else if(sign.getLine(0).equals(Color.SIGN + "[PRIZE]")) {
            if(player.hasPermission("udsplugin.prize")) {
                if(UDSPlugin.getPlayers().get(player.getName()).hasClaimedPrize()) {
                    UDSPlugin.getPlayers().get(player.getName()).claimPrize();
                    final ItemStack item = ItemUtils.findItem(sign.getLine(1));
                    item.setAmount(Integer.parseInt(sign.getLine(2)));
                    player.getInventory().addItem(item);
                } else {
                    player.sendMessage(UDSMessage.HAVE_PRIZE);
                }
            } else {
                player.sendMessage(UDSMessage.CANT_DO_THAT);
            }
        } else if(sign.getLine(0).equals(Color.SIGN + "[ITEM]")) {
            if(player.hasPermission("udsplugin.item")) {
                    final ItemStack item = ItemUtils.findItem(sign.getLine(1));

                    int owned = PlayerUtils.countItems(player, item);
                    if(owned < Integer.parseInt(sign.getLine(2))) {
                        item.setAmount(Integer.parseInt(sign.getLine(2)) - owned);
                        player.getInventory().addItem(item);
                    } else {
                        player.sendMessage(UDSMessage.HAVE_ITEM);
                    }
            } else {
                player.sendMessage(UDSMessage.CANT_DO_THAT);
            }
        } else if(sign.getLine(0).equals(Color.SIGN + "[WARP]")) {
            if(player.hasPermission("udsplugin.warp")) {
                    final Warp warp = UDSPlugin.getWarps().get(sign.getLine(1));
                    if(warp != null) {
                        player.teleport(warp.getLocation());
                    } else {
                    player.sendMessage(UDSMessage.NO_WARP);
                }
            } else {
                player.sendMessage(UDSMessage.CANT_DO_THAT);
            }
        } else if(sign.getLine(0).equals(Color.SIGN + "[SPLEEF]")) {
            if(player.hasPermission("udsplugin.spleef")) {
                    final Region region = UDSPlugin.getRegions().get(sign.getLine(1));
                    if(region != null) {
                        final Vector min = region.getMinVector();
                        final Vector max = region.getMaxVector();
                        final World world = region.getWorld();
                        for(int ix = (int) min.getX(); ix <= (int) max.getX(); ix++) {
                            for(int iy = (int) min.getY(); iy <= (int) max.getY(); iy++) {
                                for(int iz = (int) min.getZ(); iz <= (int) max.getZ(); iz++) {
                                    world.getBlockAt(ix, iy, iz).setType(Material.SNOW_BLOCK);
                                }
                            }
                        }
                    } else {
                    player.sendMessage(UDSMessage.NO_REGION);
                }
            } else {
                player.sendMessage(UDSMessage.CANT_DO_THAT);
            }
        }
    }
}

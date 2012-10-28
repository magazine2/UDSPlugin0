package com.undeadscythes.udsplugin.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstone implements Listener{
    @EventHandler
    public final void onBlockRedstone(final BlockRedstoneEvent event) {
        final Block block = event.getBlock();
        if(block instanceof Jukebox && event.getNewCurrent() != 0 && event.getOldCurrent() == 0) {
            final Jukebox jukebox = (Jukebox)block;
            jukebox.setPlaying(jukebox.getPlaying());
        }
    }
}

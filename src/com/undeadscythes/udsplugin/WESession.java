package com.undeadscythes.udsplugin;

import java.util.Stack;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Used to store the actions of a player whilst world editing.
 * @author Dave
 */
public class WESession {
    private String playerName;
    private Stack<Cuboid> edits;
    private Cuboid clipboard;
    private Vector v1;
    private Vector v2;

    public WESession(Player player) {
        playerName = player.getName();
        edits = new Stack<Cuboid>();
    }

    public void save(Cuboid cuboid) {
        edits.push(cuboid);
    }

    public Cuboid load() {
        return edits.pop();
    }

    public boolean hasUndo() {
        return !edits.isEmpty();
    }

    public Cuboid get() {return clipboard;}
    public void copy(Cuboid cuboid) {clipboard = cuboid;}
    public String getName() {return playerName;}
}

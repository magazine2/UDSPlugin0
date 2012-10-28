package com.undeadscythes.udsplugin;

import org.bukkit.util.Vector;

/**
 *
 * @author Dave
 */
public enum Direction {
    NORTH(new Vector(0, 0, -1), 180),
    EAST(new Vector(1, 0, 0), 270),
    SOUTH(new Vector(0, 0, 1), 0),
    WEST(new Vector(-1, 0, 0), 90),
    NORTH_EAST(NORTH.toVector().add(EAST.toVector()), 215),
    SOUTH_EAST(SOUTH.toVector().add(EAST.toVector()), 305),
    SOUTH_WEST(SOUTH.toVector().add(WEST.toVector()), 45),
    NORTH_WEST(NORTH.toVector().add(WEST.toVector()), 135),
    UP(new Vector(0, 1, 0), 0),
    DOWN(new Vector(0, -1, 0), 0),
    NEUTRAL(new Vector(0, 0, 0), 0);

    private Vector vector;
    private float yaw;

    private Direction(Vector vector, float yaw) {
        this.vector = vector;
        this.yaw = yaw;
    }

    public Vector toVector() {
        return vector;
    }

    public float toYaw() {
        return yaw;
    }

    @Override
    public String toString() {
        String s = super.toString();
        return s.substring(0, 1) + s.substring(1).toLowerCase();
    }

    public static Direction getDirection(String name) {
        for(Direction d : values()) {
            if(name.toLowerCase().equals(d.toString().toLowerCase())) return d;
        }
        return NEUTRAL;
    }
}

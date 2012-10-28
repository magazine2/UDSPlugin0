package com.undeadscythes.udsplugin.utilities;

import org.bukkit.util.Vector;


/**
 * A collection of tools to find various objects within hash maps.
 * @author UndeadScythes
 */
public final class VectorUtils {
    public static Vector parseVector(String string) {
        String[] split = string.split(",");
        if(split[0].equals("null")) return null; // Remove this line once all 'edits' are done and location defaults are set properly.
        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);
        return new Vector(x, y, z);
    }

    public static String getString(Vector vector) {
        if(vector == null) return "null"; // Also remove this line for the same reason as above.
        return vector.getX() + "," +
               vector.getY() + "," +
               vector.getZ();
    }

    /**
     * Construct the vector with the minimum components of two vectors.
     * @param v1 Vector.
     * @param v2 Vector.
     * @return
     */
    public static Vector findMin(final Vector v1, final Vector v2) {
        final double x = findMin(v1.getX(), v2.getX());
        final double y = findMin(v1.getY(), v2.getY());
        final double z = findMin(v1.getZ(), v2.getZ());
        return new Vector(x, y, z);
    }

    /**
     * Find the minimum of two doubles.
     * @param a Double.
     * @param b Double.
     * @return
     */
    public static double findMin(final double a, final double b) {
        return (a > b) ? b : a;
    }

    /**
     * Construct the vector with the maximum components of two vectors.
     * @param v1 Vector.
     * @param v2 Vector.
     * @return
     */
    public static Vector findMax(final Vector v1, final Vector v2) {
        double x = findMax(v1.getX(), v2.getX());
        double y = findMax(v1.getY(), v2.getY());
        double z = findMax(v1.getZ(), v2.getZ());
        return new Vector(x, y, z);
    }

    /**
     * Find the maximum of two doubles.
     * @param a Double.
     * @param b Double.
     * @return
     */
    public static double findMax(final double a, final double b) {
        return (a > b) ? a : b;
    }
}

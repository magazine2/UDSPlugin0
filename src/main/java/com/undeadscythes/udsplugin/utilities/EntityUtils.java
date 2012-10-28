package com.undeadscythes.udsplugin.utilities;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;

/**
 * A collection of tools to find entity relationships.
 * @author UndeadScythes
 */
public final class EntityUtils {
    /**
     * Find an entities owner if it has one.
     * @param partialName Partial name.
     * @return Closest matching clan or null.
     */
    public static Entity getTopLevel(Entity entity) {
        if(entity instanceof Arrow) {
            return ((Arrow)entity).getShooter();
        }
        return entity;
    }


}

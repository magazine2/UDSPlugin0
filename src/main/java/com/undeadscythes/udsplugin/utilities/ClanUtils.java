package com.undeadscythes.udsplugin.utilities;

import com.undeadscythes.udsplugin.Clan;
import com.undeadscythes.udsplugin.UDSHashMap;
import com.undeadscythes.udsplugin.UDSPlugin;
import java.util.Locale;
import java.util.Map;

/**
 * A collection of tools to find various objects within hash maps.
 * @author UndeadScythes
 */
public final class ClanUtils {
    public static void remove(String clanName) {
        UDSPlugin.getClans().remove(clanName);
    }
    /**
     * Find a clan by partial name.
     * @param partialName Partial name.
     * @return Closest matching clan or null.
     */
    public static Clan match(final String partialName, final UDSHashMap<Clan> clans) {
        for(Map.Entry<String, Clan> i : clans.entrySet()) {
            if(i.getValue().getName().toLowerCase(Locale.ENGLISH).contains(partialName.toLowerCase(Locale.ENGLISH))) {
                return i.getValue();
            }
            if(i.getKey().toLowerCase(Locale.ENGLISH).contains(partialName.toLowerCase(Locale.ENGLISH))) {
                return i.getValue();
            }
        }
        return null;
    }

    public static Clan get(String clanName) {
        return UDSPlugin.getClans().get(clanName);
    }
}

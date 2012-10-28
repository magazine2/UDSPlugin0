package com.undeadscythes.comparators;

import com.undeadscythes.udsplugin.Clan;
import java.util.Comparator;

/**
 * Compare clans by KDR.
 * @author Scythe
 */
public class ClanByRatio implements Comparator<Clan> {
    @Override
    public int compare(Clan clan1, Clan clan2) {
        return (int)((clan2.getKDR() - clan1.getKDR()) * 1000);
    }
}

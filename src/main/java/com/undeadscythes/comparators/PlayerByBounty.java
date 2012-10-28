package com.undeadscythes.comparators;

import com.undeadscythes.udsplugin.UDSPlayer;
import java.util.Comparator;

/**
 *
 * @author Scythe
 */
public class PlayerByBounty implements Comparator<UDSPlayer> {
    @Override
    public int compare(UDSPlayer player1, UDSPlayer player2) {
        return player2.getBounty() - player1.getBounty();
    }
}

package com.undeadscythes.udsplugin.comparators;

import com.undeadscythes.udsplugin.UDSPlayer;
import java.util.Comparator;

/**
 *
 * @author Dave
 */
public class PlayerByMoney implements Comparator<UDSPlayer> {
    @Override
    public int compare(UDSPlayer player1, UDSPlayer player2) {
        return player1.getMoney() - player2.getMoney();
    }
}

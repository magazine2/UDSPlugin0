package com.undeadscythes.udsplugin.utilities;

import com.undeadscythes.udsplugin.UDSPlugin;
import com.undeadscythes.udsplugin.Color;
import org.bukkit.ChatColor;

/**
 * A collection of useful counting functions.
 * @author UndeadScythes
 */
public final class ShopUtils {
    private static final int OWNER_LINE = 0;
    private static final int SHOP_LINE = 1;
    private static final int PRICE_LINE = 3;

    /**
     * Is this sign a valid shop sign? Valid before and after corrections.
     * @param lines Lines of the sign.
     * @return
     */
    public static boolean isShopSign(final String[] lines) {
        final String shopLine = lines[SHOP_LINE];
        final ChatColor signColor = Color.SIGN;
        final String ownerLine = lines[OWNER_LINE];
        final String priceLine = lines[PRICE_LINE];
        return (shopLine.equalsIgnoreCase(signColor + "shop")
                || shopLine.equalsIgnoreCase("shop"))
            && ((UDSPlugin.getPlayers().get(ownerLine.replace(signColor.toString(), "")) != null
                || "".equals(ownerLine)
                || (signColor + "server").equalsIgnoreCase(ownerLine)))
            && ItemUtils.findItem(lines[2]) != null
            && priceLine.contains(":")
            && priceLine.split(":")[0].replace("B ", "").matches("[0-9][0-9]*")
            && priceLine.split(":")[1].replace(" S", "").matches("[0-9][0-9]*");
    }

}

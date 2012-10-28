package com.undeadscythes.udsplugin;

import org.bukkit.ChatColor;

/**
 * Chat colors used by UDSPlugin.
 * @author UndeadScythes
 */
public final class Color {
    /**
     * Error message.
     */
    public static final ChatColor ERROR = ChatColor.LIGHT_PURPLE;
    /**
     * Feedback message.
     */
    public static final ChatColor MESSAGE = ChatColor.YELLOW;
    /**
     * Directory listing.
     */
    public static final ChatColor COMMAND = ChatColor.BLUE;
    /**
     * Ordinary chat or description.
     */
    public static final ChatColor TEXT = ChatColor.WHITE;
    /**
     * Server broadcast.
     */
    public static final ChatColor BROADCAST = ChatColor.DARK_RED;
    /**
     * Private chat.
     */
    public static final ChatColor PRIVATE = ChatColor.RED;
    /**
     * Clan chat.
     */
    public static final ChatColor CLAN = ChatColor.BLUE;
    /**
     * Whispers.
     */
    public static final ChatColor WHISPER = ChatColor.GRAY;
    /**
     * Default group.
     */
    public static final ChatColor GROUP_DEFAULT = ChatColor.WHITE;
    /**
     * Member group.
     */
    public static final ChatColor GROUP_MEMBER = ChatColor.GREEN;
    /**
     * VIP group.
     */
    public static final ChatColor GROUP_VIP = ChatColor.DARK_PURPLE;
    /**
     * Warden group.
     */
    public static final ChatColor GROUP_WARDEN = ChatColor.AQUA;
    /**
     * Moderator group.
     */
    public static final ChatColor GROUP_MOD = ChatColor.DARK_AQUA;
    /**
     * Administrator group.
     */
    public static final ChatColor GROUP_ADMIN = ChatColor.YELLOW;
    /**
     * Server owner group
     */
    public static final ChatColor GROUP_OWNER = ChatColor.GOLD;
    /**
     * Validated sign.
     */
    public static final ChatColor SIGN = ChatColor.GREEN;

    private Color() {}
}

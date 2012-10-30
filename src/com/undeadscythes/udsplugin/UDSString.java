package com.undeadscythes.udsplugin;

import java.util.*;
import org.bukkit.*;

public class UDSString {
    private transient String string;

    public UDSString(final String string) {
        this.string = string;
    }

    public final boolean censor() {
        boolean censored = false;
        final String[] censors = {"fuk", "shit", "bastard", "fuck", "bitch", "arse", "slag", "tosser", "tits", "wank",
                                  "fudgepacker", "asshole", "tosspot", "penis", "vagina", "bollock", "cunt", "nigger",
                                  "pussy", "twat", "faggot", "cock", "wiener", "dick", "chode", "anal", "ass hole"};
        for(int i = 0; i < censors.length; i++) {
            if(string.toLowerCase(Locale.ENGLISH).contains(censors[i])) {
                string = string.replaceAll("(?i)" + censors[i], ChatColor.MAGIC + censors[i] + ChatColor.WHITE);
                censored = true;
            }
        }
        return censored;
    }

    public final String getString() {
        return string;
    }
}

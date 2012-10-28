package com.undeadscythes.udsplugin.utilities;

/**
 * A collection of tools to find various objects within hash maps.
 * @author UndeadScythes
 */
public final class TimeUtils {
    /**
     * Find the time.
     * @param timePass Time.
     * @return Time formatted: "X days X hours, X minutes [and] X seconds".
     */
    public static String findTime(final long timePass) {
        double time = timePass;
        String formattedTime = "";
        boolean addedText = false;
        if(time > 86400000) {
            formattedTime = formattedTime + (int) Math.floor(time / 86400000) + " days";
            time = time % 86400000 ;
            addedText = true;
        }
        if(time > 3600000) {
            if(addedText) {
                formattedTime = formattedTime + ", ";
            }
            formattedTime = formattedTime + (int) Math.floor(time / 3600000) + " hours";
            time = time % 3600000;
            addedText = true;
        } else {
            addedText = false;
        }
        if(time > 60000) {
            if(addedText) {
                formattedTime = formattedTime + ", ";
            }
            formattedTime = formattedTime + (int) Math.floor(time / 60000) + " minutes";
            time = time % 60000;
            addedText = true;
        } else {
            addedText = false;
        }
        if(time > 1000) {
            if(addedText) {
                formattedTime = formattedTime + " and ";
            }
            formattedTime = formattedTime + (int) Math.floor(time / 1000) + " seconds";
            time = time % 1000;
        } else {
            addedText = false;
        }
        return formattedTime;
    }
}

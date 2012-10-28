package com.undeadscythes.udsplugin;

import java.util.LinkedList;
import java.util.List;

/**
 * Private chat room.
 * @author UndeadScythes
 */
public class ChatRoom {
    private String name;
    private List<String> members;

    /**
     * @param creatorName Name of player opening the chat room.
     * @param roomName Name of the chat room.
     */
    public ChatRoom(final String creatorName, final String roomName) {
        this.name = roomName;
        this.members = new LinkedList<String>();
        this.members.add(creatorName);
    }

    /**
     * @return Name of the chat room.
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return Members of the chat room.
     */
    public final List<String> getMembers() {
        return members;
    }
}

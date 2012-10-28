package com.undeadscythes.udsplugin;

public class Request {
    private String recipent;
    private String sender;
    private Type type;
    private String data;
    private long time;
    private long timeout;
            
    public enum Type {
    TP, SHOP, CHALLENGE, CLAN, HOME, PET;
    }

    public Request(String recipent, String sender, Type type, String data, long timeout) {
        this.recipent = recipent;
        this.sender = sender;
        this.type = type;
        this.data = data;
        this.time = System.currentTimeMillis();
        this.timeout = timeout;
        if(timeout == 0) {
            this.timeout = UDSPlugin.getUDSConfig().getRequestTimeout();
        }
    }

    public String getRecipent() {
        return recipent;
    }

    public String getSender() {
        return sender;
    }

    public Type getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public boolean isTimedOut () {
        return (time + timeout < System.currentTimeMillis());
    }
}

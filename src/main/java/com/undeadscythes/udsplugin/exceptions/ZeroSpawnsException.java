package com.undeadscythes.udsplugin.exceptions;

public class ZeroSpawnsException extends Exception {
    String message;
    
    public ZeroSpawnsException() {
        super();
        message = "";
    }
    
    public ZeroSpawnsException(String message) {
        super();
        this.message = message;
    }
    
    public String getError() {
        return message;
    }
}

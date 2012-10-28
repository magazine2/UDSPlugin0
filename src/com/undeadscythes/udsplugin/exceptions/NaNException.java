package com.undeadscythes.udsplugin.exceptions;

public class NaNException extends Exception {
    String message;
    
    public NaNException() {
        super();
        message = "";
    }
    
    public NaNException(String message) {
        super();
        this.message = message;
    }
    
    public String getError() {
        return message;
    }
}

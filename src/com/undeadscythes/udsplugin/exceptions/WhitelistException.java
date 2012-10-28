package com.undeadscythes.udsplugin.exceptions;

public class WhitelistException extends Exception {
    String message;
    
    public WhitelistException() {
        super();
        message = "";
    }
    
    public WhitelistException(String message) {
        super();
        this.message = message;
    }
    
    public String getError() {
        return message;
    }
}

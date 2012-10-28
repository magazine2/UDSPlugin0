package com.undeadscythes.udsplugin.exceptions;

public class NoFlagException extends Exception {
    String message;
    
    public NoFlagException() {
        super();
        message = "";
    }
    
    public NoFlagException(String message) {
        super();
        this.message = message;
    }
    
    public String getError() {
        return message;
    }
}

package com.aa.toolbox.jar.exceptions;

/**
 *
 * @author pastorduran
 */
public class FileHandlerException extends Exception {
    
    private int error;

    public FileHandlerException(String message) {
        super(message);
    }
    
    public FileHandlerException(String message, int error) {
        super(message);
        this.error = error;
    }

    public FileHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getError() {
        return error;
    }
    
}

package com.interswitch.dps.codemanagement.exceptions;

public class ConflictException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private Object data;

    public ConflictException(String message) {
        super(message);
    }
    
    public ConflictException(String message, Object object) {
        super(message);
        this.data = object;
    }

    public Object getData() {
        return data;
    }
}

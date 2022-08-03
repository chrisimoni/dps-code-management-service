package com.interswitch.dps.codemanagement.exceptions;

public class GracefulFailureException extends RuntimeException {

    private Object data;

    public GracefulFailureException(String message) {
        super(message);
    }

    public GracefulFailureException(String message, Object object) {
        super(message);
        this.data = object;
    }

    public Object getData() {
        return data;
    }
}

package com.interswitch.dps.codemanagement.exceptions;

public class ForbiddenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException(String message) {
        super(message);
    }
}
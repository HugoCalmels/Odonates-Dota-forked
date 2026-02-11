package com.palladium46.odonatesdota.exceptions;

public class BannedUserException extends RuntimeException {

    public BannedUserException() {
        super();
    }

    public BannedUserException(String message) {
        super(message);
    }

    public BannedUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public BannedUserException(Throwable cause) {
        super(cause);
    }
}
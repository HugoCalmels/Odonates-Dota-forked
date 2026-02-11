package com.palladium46.odonatesdota.exceptions;

public class UnAuthorizeException extends RuntimeException {

    public UnAuthorizeException() {
        super();
    }

    public UnAuthorizeException(String message) {
        super(message);
    }

    public UnAuthorizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthorizeException(Throwable cause) {
        super(cause);
    }
}

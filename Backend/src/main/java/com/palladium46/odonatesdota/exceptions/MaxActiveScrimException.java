package com.palladium46.odonatesdota.exceptions;

public class MaxActiveScrimException extends RuntimeException {

    public MaxActiveScrimException() {
        super();
    }

    public MaxActiveScrimException(String message) {
        super(message);
    }

    public MaxActiveScrimException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxActiveScrimException(Throwable cause) {
        super(cause);
    }
}

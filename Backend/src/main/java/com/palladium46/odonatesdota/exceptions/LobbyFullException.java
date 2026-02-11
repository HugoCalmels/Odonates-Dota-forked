package com.palladium46.odonatesdota.exceptions;

public class LobbyFullException extends RuntimeException {

    public LobbyFullException() {
        super();
    }

    public LobbyFullException(String message) {
        super(message);
    }

    public LobbyFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public LobbyFullException(Throwable cause) {
        super(cause);
    }
}

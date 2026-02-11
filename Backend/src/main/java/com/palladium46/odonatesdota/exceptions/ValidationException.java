package com.palladium46.odonatesdota.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {

    private List<String> messages;

    public ValidationException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages(){
        return messages;
    }
}
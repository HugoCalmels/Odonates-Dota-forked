package com.palladium46.odonatesdota.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public class ErrorUserDto {
    private String error;
    private String timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> messages;

    public ErrorUserDto(String error, String timestamp, List<String> messages) {
        this.error = error;
        this.timestamp = timestamp;
        this.messages = messages;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

}

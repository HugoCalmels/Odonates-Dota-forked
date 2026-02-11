package com.palladium46.odonatesdota.websocket;

public class Session {
    String userName;
    String sessionId;
    String roomId;

    public Session(String userName, String sessionId, String roomId) {
        this.userName = userName;
        this.sessionId = sessionId;
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}

package com.palladium46.odonatesdota.lobbysauvage.model;

public enum LobbyStatus {
    WAITING_FOR_PLAYER, READY, CLOSED, LAUNCHED;

    @Override
    public String toString() {
        return name();
    }
}

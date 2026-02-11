package com.palladium46.odonatesdota.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public class LobbySseEmitter extends SseEmitter {
    private static final Long SSE_TIMEOUT = 3600L * 3000L; // 3 hour

    public LobbySseEmitter() {
        super(SSE_TIMEOUT);
        this.userId = null;
        this.lobbyId = null;
    }

    public LobbySseEmitter(Long lobbyId, UUID userId) {
        super(Long.MAX_VALUE);
        this.userId = userId;
        this.lobbyId = lobbyId;
    }

    private final Long lobbyId;
    private final UUID userId;

    public Long getLobbyId() {
        return lobbyId;
    }

    public UUID getUserId() {
        return userId;
    }
}

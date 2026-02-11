package com.palladium46.odonatesdota.websocket.listener;

import com.palladium46.odonatesdota.websocket.Session;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebSocketEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private final SimpMessagingTemplate messagingTemplate;
    private final List<Session> sessions = new ArrayList<>();

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        if (destination != null && destination.startsWith("/topic/rooms/")) {
            String roomId = destination.split("/")[3];
            String username = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : "Anonymous";
            sessions.add(new Session(username, headerAccessor.getSessionId(), roomId));
            messagingTemplate.convertAndSend(destination, username + " has joined the room.");
        }
    }

    @Component
    public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

        private final SimpMessagingTemplate messagingTemplate;

        public WebSocketDisconnectListener(SimpMessagingTemplate messagingTemplate) {
            this.messagingTemplate = messagingTemplate;
        }

        @Override
        public void onApplicationEvent(SessionDisconnectEvent event) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
            String sessionId = headerAccessor.getSessionId();
            String destination = "/topic/rooms/" + getRoomId(sessionId);
            String username = getUsername(sessionId);
            messagingTemplate.convertAndSend(destination, username + " has left the room.");
        }

        private String getRoomId(String sessionId) {
            return getSession(sessionId).getRoomId();
        }

        private String getUsername(String sessionId) {
            return getSession(sessionId).getUserName();
        }

        private Session getSession(String sessionId) {
            return sessions.stream().filter(s -> s.getSessionId().equals(sessionId)).findFirst().orElseThrow();
        }
    }
}

package com.palladium46.odonatesdota.websocket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyChatController {

    @MessageMapping("/room/{roomId}/chat")
    @SendTo("/topic/rooms/{roomId}")
    public String handleChatMessage(@DestinationVariable String roomId, String message) {
        return message;
    }

    @MessageMapping("/room/{roomId}/join")
    @SendTo("/topic/rooms/{roomId}")
    public String handleJoinMessage(@DestinationVariable String roomId, String username) {
        return username + " has joined the room.";
    }
}

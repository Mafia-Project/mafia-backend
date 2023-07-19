package com.poscodx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public GameSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/rooms/{id}/games")
    public void send(@DestinationVariable String id, String message) {
        simpMessagingTemplate.convertAndSend("/sub/rooms/" +id, message);
    }
}

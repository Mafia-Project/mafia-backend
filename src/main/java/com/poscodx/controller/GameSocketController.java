package com.poscodx.controller;

import com.poscodx.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameSocketController {

    private final GameService gameService;

    @MessageMapping("/rooms/{id}/games")
    public void send(@DestinationVariable String id, String message) {
        gameService.testMessage(id, message);
    }
}

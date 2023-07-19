package com.poscodx.controller;

import com.poscodx.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/rooms/{id}")
    public void send(@DestinationVariable String id, String message) {
        chatService.sendMessage(id, message);
        System.out.println("채팅");
        System.out.println(message);
    }
}

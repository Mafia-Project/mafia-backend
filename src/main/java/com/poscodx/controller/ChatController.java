package com.poscodx.controller;

import com.poscodx.dto.ChatRequest;
import com.poscodx.dto.ChatResponse;
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
    public void send(@DestinationVariable String id, ChatRequest request) {
        System.out.println(request.toString());
        chatService.sendMessage(id, request);
    }
}

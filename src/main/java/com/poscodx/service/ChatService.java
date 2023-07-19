package com.poscodx.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessage(String id, String message) {

        String topic = String.format("/sub/chat/rooms/%s", id);

        simpMessagingTemplate.convertAndSend(topic, message);
    }
}

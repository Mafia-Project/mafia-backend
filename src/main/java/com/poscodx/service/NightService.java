package com.poscodx.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NightService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void testMessage(String id, String message){
        String topic = String.format("/sub/rooms/%s", id);
        simpMessagingTemplate.convertAndSend(topic, message);
    }

    private String getTopic(String id){
        return String.format("/sub/rooms/%s", id);
    }
}

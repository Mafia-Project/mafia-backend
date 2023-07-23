package com.poscodx.service;

import com.poscodx.domain.ChatType;
import com.poscodx.domain.JobType;
import com.poscodx.dto.ChatRequest;
import com.poscodx.dto.ChatResponse;
import com.poscodx.utils.MapUtils;
import com.poscodx.utils.SocketTopicUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private void sendMessage(String id, String username, String content, ChatType type) {
        simpMessagingTemplate.convertAndSend(SocketTopicUtils.getChatTopic(id), MapUtils.toMap(
                ChatResponse.of(username, content, type)
        ));
    }

    public void sendMessage(String id, ChatRequest request) {
        if (request.getDayNight().equals("afternoon")){
            sendMessage(id, request.getNickname(), request.getContent(), ChatType.USER);
        } else if (Objects.nonNull(request.getJob()) && JobType.MAFIA.name().equals(request.getJob())){
            sendMessage(id, request.getNickname(), request.getContent(), ChatType.MAFIA_CHAT);
        }
    }
}

package com.poscodx.service;

import static com.poscodx.utils.MapUtils.*;
import static com.poscodx.utils.SocketTopicUtils.*;

import com.poscodx.domain.ChatType;
import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.ChatJobResponse;
import com.poscodx.dto.ChatResponse;
import com.poscodx.dto.NightEventResponse;
import com.poscodx.utils.MapUtils;
import com.poscodx.utils.SocketTopicUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NightService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    public void sendNightEndMessage(String roomKey, String message){
        var response = NightEventResponse.of(message,null ,GameMessageType.NIGHT_END);
        simpMessagingTemplate.convertAndSend(getRoomTopic(roomKey), toMap(response));
    }

    public void sendJobEventMessage(String roomKey, ChatResponse chatResponse){
        simpMessagingTemplate.convertAndSend(getChatTopic(roomKey), toMap(chatResponse));
    }
    public void sendJobEventMessage(String roomKey, ChatJobResponse chatResponse){
        simpMessagingTemplate.convertAndSend(getChatTopic(roomKey), toMap(chatResponse));
    }

    public void sendNightEndGameOverMessage(String roomKey, String message){
        var response = NightEventResponse.of(message,null ,GameMessageType.END);
        simpMessagingTemplate.convertAndSend(getRoomTopic(roomKey), toMap(response));
        simpMessagingTemplate.convertAndSend(getChatTopic(roomKey), toMap(ChatResponse.of(
                SYSTEM_NAME,
                message,
                ChatType.SYSTEM
        )));
    }

    public void sendChoiceMessage(String roomKey, String message, JobType jobType){
        var response = NightEventResponse.of(message, jobType ,GameMessageType.NIGHT_EVENT);
        simpMessagingTemplate.convertAndSend(getRoomTopic(roomKey), toMap(response));
    }
}

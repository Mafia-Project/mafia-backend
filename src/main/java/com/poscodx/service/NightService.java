package com.poscodx.service;

import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.JobType;
import com.poscodx.dto.NightEventResponse;
import com.poscodx.utils.MapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NightService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    public void sendNightEndMessage(String roomKey, String message){
        var response = NightEventResponse.of(message,null ,GameMessageType.NIGHT_END);
        simpMessagingTemplate.convertAndSend(getTopic(roomKey), MapUtils.toMap(response));
    }

    public void sendNightEndGameOverMessage(String roomKey, String message){
        var response = NightEventResponse.of(message,null ,GameMessageType.END);
        simpMessagingTemplate.convertAndSend(getTopic(roomKey), MapUtils.toMap(response));
    }

    public void sendChoiceMessage(String roomKey, String message, JobType jobType){
        var response = NightEventResponse.of(message, jobType ,GameMessageType.NIGHT_EVENT);
        simpMessagingTemplate.convertAndSend(getTopic(roomKey), MapUtils.toMap(response));
    }

    private String getTopic(String id){
        return String.format("/sub/rooms/%s", id);
    }
}

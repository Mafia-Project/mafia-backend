package com.poscodx.service;

import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.dto.TimeReductionResponse;
import com.poscodx.utils.MapUtils;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void testMessage(String id, String message){
        simpMessagingTemplate.convertAndSend(getTopic(id), message);
    }

    public void timeReduction(String id, TimeReductionRequest request) {
        var response = TimeReductionResponse.of(request.getNickname(), request.getTime() / 2);
        simpMessagingTemplate.convertAndSend(getTopic(id), MapUtils.toMap(response));
    }

    private String getTopic(String id){
        return String.format("/sub/rooms/%s", id);
    }
}

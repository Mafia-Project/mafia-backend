package com.poscodx.service;

import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.UserInfoResponse;
import com.poscodx.utils.MapUtils;
import com.poscodx.utils.SocketTopicUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.poscodx.utils.SocketTopicUtils.getRoomTopic;

@Service
@RequiredArgsConstructor
public class GameEventService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameInfoService gameInfoService;


    public void playerDeadEvent(String roomKey, String targetName, GameMessageType type){
        if (targetName == null) return;
        Game game = gameInfoService.getGame(roomKey);
        GamePlayer target = game.findGamePlayerByNickname(targetName);
        target.die();
        gameInfoService.sendUsers(roomKey, type);
    }
    //response : front-end 참조
    public void messageSent(String roomKey, Map<String, Object> message){
        simpMessagingTemplate.convertAndSend(SocketTopicUtils.getChatTopic(roomKey), message);
    }
}

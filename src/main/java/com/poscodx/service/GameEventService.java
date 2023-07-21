package com.poscodx.service;

import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.ChatResponse;
import com.poscodx.dto.GameMassage;
import com.poscodx.dto.UserInfoResponse;
import com.poscodx.utils.MapUtils;
import com.poscodx.utils.SocketTopicUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.poscodx.domain.GameMessageType.*;
import static com.poscodx.utils.MapUtils.*;
import static com.poscodx.utils.SocketTopicUtils.*;
import static com.poscodx.utils.SocketTopicUtils.MAFIA_WIN;
import static com.poscodx.utils.SocketTopicUtils.SYSTEM_NAME;
import static com.poscodx.utils.SocketTopicUtils.TIME_REDUCTION_MASSAGE;
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
        confirmGameEndAfterDeathEvent(game);
    }
    public void messageSent(String roomKey, Map<String, Object> message){
        simpMessagingTemplate.convertAndSend(getChatTopic(roomKey), message);
    }

    public void confirmGameEndAfterDeathEvent(Game game){
        long citizenNumber = game.getAliveCitizenNumber();
        long mafiaNumber = game.getAliveMafiaNumber();

        if (mafiaNumber == 0){
            simpMessagingTemplate.convertAndSend(getRoomTopic(game.getKey()), toMap(new GameMassage(END)));
            messageSent(game.getKey(), toMap(ChatResponse.of(SYSTEM_NAME, CITIZEN_WIN)));
        }else if(mafiaNumber == citizenNumber){
            simpMessagingTemplate.convertAndSend(getRoomTopic(game.getKey()), toMap(new GameMassage(END)));
            messageSent(game.getKey(), toMap(ChatResponse.of(SYSTEM_NAME, MAFIA_WIN)));
        }
    }
}

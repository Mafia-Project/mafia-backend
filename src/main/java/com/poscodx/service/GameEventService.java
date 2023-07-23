package com.poscodx.service;

import com.poscodx.domain.ChatType;
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
        if (targetName == null){
            gameInfoService.sendUsers(roomKey, type);
            return;
        }
        Game game = gameInfoService.getGame(roomKey);
        GamePlayer target = game.findGamePlayerByNickname(targetName);
        target.die();
        gameInfoService.sendUsers(roomKey, type);
        confirmGameEndAfterDeathEvent(game, type);
    }
    public void messageSent(String roomKey, Map<String, Object> message){
        simpMessagingTemplate.convertAndSend(getChatTopic(roomKey), message);
    }

    public synchronized void confirmGameEndAfterDeathEvent(Game game, GameMessageType type){
        long citizenNumber = game.getAliveCitizenNumber();
        long mafiaNumber = game.getAliveMafiaNumber();
        gameInfoService.sendUsers(game.getKey(), USER_INFO);

        if (mafiaNumber == 0){
            game.end();
            simpMessagingTemplate.convertAndSend(getRoomTopic(game.getKey()), toMap(new GameMassage(END)));
            messageSent(game.getKey(), toMap(ChatResponse.of(SYSTEM_NAME, CITIZEN_WIN, ChatType.SYSTEM)));
        }else if(mafiaNumber >= citizenNumber){
            game.end();
            simpMessagingTemplate.convertAndSend(getRoomTopic(game.getKey()), toMap(new GameMassage(END)));
            messageSent(game.getKey(), toMap(ChatResponse.of(SYSTEM_NAME, MAFIA_WIN, ChatType.SYSTEM)));
        }else if(type == NIGHT_END){
            messageSent(game.getKey(), toMap(ChatResponse.of(SYSTEM_NAME, "날이 밝았습니다.", ChatType.SYSTEM)));
        }else if(type == VOTE_RESULT){
            messageSent(game.getKey(), toMap(ChatResponse.of(SYSTEM_NAME, "밤이 시작되어 마피아가 활동합니다.", ChatType.SYSTEM)));
        }
    }
}

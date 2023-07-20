package com.poscodx.service;

import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.UserInfoResponse;
import com.poscodx.utils.MapUtils;
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

    //살인 이벤트 메시지
    // response : 유저의 최신정보
    public void playerDeadEvent(String roomKey, String targetName){
        Game game = gameInfoService.getGame(roomKey);
        GamePlayer target = game.findGamePlayerByNickname(targetName);
        target.die();
        gameInfoService.sendUsers(roomKey);
    }


    //채팅방 메시지
    //response : front-end 참조
    public void messageSent(String roomKey, String message, JobType target){

    }



}

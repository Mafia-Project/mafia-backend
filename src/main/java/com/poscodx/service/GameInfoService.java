package com.poscodx.service;


import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.dto.UserInfoResponse;
import com.poscodx.repository.GameInfo;
import com.poscodx.utils.MapUtils;
import com.poscodx.utils.SocketTopicUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@AllArgsConstructor
@Service
public class GameInfoService {

    private final GameInfo gameInfo;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public String addGame(GamePlayer host, boolean isPsychopathAllowed, boolean isReporterAllowed, int maximumPlayer){
//        String roomKey = UUID.randomUUID().toString();
        String roomKey = "posco";
        List<GamePlayer> playerList = new ArrayList<GamePlayer>();
        playerList.add(host);
        return gameInfo.addGame(new Game(roomKey, isPsychopathAllowed, isReporterAllowed, maximumPlayer, playerList));
    }

    public void removeGame(Game game){
        gameInfo.removeGame(game);
    }

    public Game getGame(String roomKey){
        return gameInfo.getGame(roomKey);
    }

    public void sendUsers(String roomKey){
        System.out.println("in Send User");
        Game game = getGame(roomKey);
        List<GamePlayer> gamePlayerList = game.getPlayerList();
        System.out.println("getPlayer");

        for(GamePlayer gamePlayer : gamePlayerList){
            System.out.println(gamePlayer);
        }
        var response = UserInfoResponse.of(gamePlayerList);
        System.out.println("getResponse");


        simpMessagingTemplate.convertAndSend(
                SocketTopicUtils.getRoomTopic(roomKey),
                MapUtils.toMap(response));

        System.out.println("Send Complete");

    }


}

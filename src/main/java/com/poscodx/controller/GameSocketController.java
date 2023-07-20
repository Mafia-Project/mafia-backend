package com.poscodx.controller;

import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.dto.JoinRequest;
import com.poscodx.service.GameInfoService;
import com.poscodx.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GameSocketController {

    private final GameInfoService gameInfoService;

    @MessageMapping("/rooms/{roomKey}/join-game")
    public void send(@DestinationVariable String roomKey, JoinRequest joinRequest) {
        System.out.println("Join game method");
        Game game = gameInfoService.getGame(roomKey);
        game.addGamePlayer(new GamePlayer(joinRequest.getNickname(), false));
        List<GamePlayer> gamePlayerList = game.getPlayerList();

        gameInfoService.sendUsers(roomKey);
        for(GamePlayer gamePlayer : game.getGamePlayers()){
            System.out.println(gamePlayer);
        }
    }
}

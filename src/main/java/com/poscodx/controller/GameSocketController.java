package com.poscodx.controller;

import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.dto.JoinRequest;
import com.poscodx.service.GameInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GameSocketController {

    private final GameInfoService gameInfoService;

    @PostMapping("/rooms/{roomKey}/join-game")
    public void send(@PathVariable String roomKey, JoinRequest joinRequest) {
        System.out.println("Join game method");
        Game game = gameInfoService.getGame(roomKey);
        game.addGamePlayer(new GamePlayer(joinRequest.getNickname(), false));
        gameInfoService.sendUsers(roomKey, GameMessageType.USER_INFO);
        for(GamePlayer gamePlayer : game.getGamePlayers()){
            System.out.println(gamePlayer);
        }
    }

    @PostMapping("/rooms/{roomKey}/start-game")
    public void startGame(@PathVariable String roomKey) {
        Game game = gameInfoService.getGame(roomKey);
        game.allocateJob();
        List<GamePlayer> playerList = game.getGamePlayers();
        for(GamePlayer gamePlayer:playerList){
            System.out.println(gamePlayer);
        }
        gameInfoService.sendUsers(roomKey, GameMessageType.START);
    }


}

package com.poscodx.controller;

import static com.poscodx.utils.MapUtils.*;

import com.poscodx.domain.ChatType;
import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.dto.ChatJobResponse;
import com.poscodx.dto.ChatResponse;
import com.poscodx.dto.JoinRequest;
import com.poscodx.service.GameEventService;
import com.poscodx.service.GameInfoService;
import com.poscodx.utils.MapUtils;
import com.poscodx.utils.SocketTopicUtils;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameSocketController {

    private final GameInfoService gameInfoService;
    private final GameEventService gameEventService;


    @MessageMapping("/rooms/{roomKey}/join-game")
    public void joinGame(@DestinationVariable String roomKey, JoinRequest joinRequest) {
        if (joinRequest.isHost()) {
            gameInfoService.sendUsers(roomKey, GameMessageType.USER_INFO);
        } else {
            Game game = gameInfoService.getGame(roomKey);
            game.addGamePlayer(new GamePlayer(joinRequest.getNickname(), false));
            gameInfoService.sendUsers(roomKey, GameMessageType.USER_INFO);
        }
    }

    @MessageMapping("/rooms/{roomKey}/quit-game")
    public void quitGame(@DestinationVariable String roomKey, JoinRequest joinRequest) {
        Game game = gameInfoService.getGame(roomKey);
        if (Objects.isNull(game)) {
            return;
        }

        if (game.getGamePlayers().size() == 1) {
            gameInfoService.removeGame(game);//방에 한명만 남은 상황일 경우
        } else {
            GamePlayer player = game.findGamePlayerByNickname(joinRequest.getNickname());
            if (Objects.isNull(player)) {
                return;
            } else {
                game.removeGamePlayer(player);
                if(joinRequest.isHost()){//방장인 경우
                   game.setHost();
                }

                gameInfoService.sendUsers(roomKey, GameMessageType.USER_INFO);
            }
        }
    }

}

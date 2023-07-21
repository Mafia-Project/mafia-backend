package com.poscodx.controller;

import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.dto.JoinRequest;
import com.poscodx.service.GameInfoService;
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

    /*
    * TODO
    *   나가기 기능
    *     Nickname, RoomKey validation. 통과여부 확인
    *     남은 사람이 1명인 상황에서 해당 기능이 호출된다면 remove Room.
    *     방장이 나갔을 경우 index 0번에 방장 위임
    *     GamePlayer remove이후, PlayerList 데이터 전송. (USER_INFO)
    *
    * */


    @MessageMapping("/rooms/{roomKey}/join-game")
    public void joinGame(@DestinationVariable String roomKey, JoinRequest joinRequest) {
        if(joinRequest.isHost() == true) {
            gameInfoService.sendUsers(roomKey, GameMessageType.USER_INFO);
        }
        else {
            Game game = gameInfoService.getGame(roomKey);
            game.addGamePlayer(new GamePlayer(joinRequest.getNickname(), false));
            gameInfoService.sendUsers(roomKey, GameMessageType.USER_INFO);
            for (GamePlayer gamePlayer : game.getGamePlayers()) {
                System.out.println(gamePlayer);
            }
        }
    }

    @MessageMapping("/rooms/{roomKey}/quit-game")
    public void quitGame(@DestinationVariable String roomKey, JoinRequest joinRequest){
        Game game = gameInfoService.getGame(roomKey);
        if(Objects.isNull(game)) return; //방이 존재하지 않는 경우

        if(game.getGamePlayers().size() == 1) gameInfoService.removeGame(game);//방에 한명만 남은 상황일 경우
        else{
            GamePlayer player = game.findGamePlayerByNickname(joinRequest.getNickname());
            if(Objects.isNull(player)){
                return;
            }else{
                game.removeGamePlayer(player);
                if(joinRequest.isHost()){//방장인 경우
                    List<GamePlayer> gamePlayerList = game.getPlayerList();
                    gamePlayerList.get(0).setIsHost(true);
                }

                gameInfoService.sendUsers(roomKey,GameMessageType.USER_INFO);
            }
        }
    }

    @MessageMapping("/rooms/{roomKey}/start-game")
    public void startGame(@DestinationVariable String roomKey) {
        Game game = gameInfoService.getGame(roomKey);
        game.allocateJob();
        List<GamePlayer> playerList = game.getGamePlayers();
        for(GamePlayer gamePlayer:playerList){
            System.out.println(gamePlayer);
        }
        gameInfoService.sendUsers(roomKey, GameMessageType.START);
    }


}

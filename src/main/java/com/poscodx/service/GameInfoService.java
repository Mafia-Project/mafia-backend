package com.poscodx.service;


import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.repository.GameInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Service
public class GameInfoService {

    private final GameInfo gameInfo;

    public void addGame(GamePlayer host, boolean isPsychopathAllowed, boolean isReporterAllowed, int maximumPlayer){
        String roomKey = UUID.randomUUID().toString();
        List<GamePlayer> playerList = new ArrayList<GamePlayer>();
        playerList.add(host);
        gameInfo.addGame(new Game(roomKey, isPsychopathAllowed, isReporterAllowed, maximumPlayer, playerList));
    }

    public void removeGame(Game game){
        gameInfo.removeGame(game);
    }

    public Game getGame(String roomKey){
        return gameInfo.getGame(roomKey);
    }

}

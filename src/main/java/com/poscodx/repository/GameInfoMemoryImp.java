package com.poscodx.repository;

import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class GameInfoMemoryImp implements GameInfo{
    Map<String, Game> store = new HashMap<>();
    @Override
    public String addGame(Game game) {
        store.put(game.getKey(), game);
        return game.getKey();
    }

    @Override
    public void removeGame(Game game) {
        store.remove(game.getKey());
    }

    @Override
    public Game getGame(String roomKey) {
        return store.get(roomKey);
    }


}

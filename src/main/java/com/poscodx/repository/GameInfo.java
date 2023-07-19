package com.poscodx.repository;

import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;

import java.util.List;

public interface GameInfo {
    //게임 새로 생성
    String addGame(Game game);

    //게임 삭제
    void removeGame(Game game);
    //유저 추가하기
    Game getGame(String roomKey);

}

package com.poscodx.dto;

import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static com.poscodx.domain.GameMessageType.USER_INFO;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponse {
    List<GamePlayer> playerInfo;
    GameMessageType messageType;

    public static UserInfoResponse of(List<GamePlayer> playerInfo, GameMessageType type){
        return new UserInfoResponse(playerInfo, type);
    }

}

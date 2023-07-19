package com.poscodx.dto;

import com.poscodx.domain.GameMessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import static com.poscodx.domain.GameMessageType.NIGHT_EVENT;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NightEventResponse {
    private String message;
    private GameMessageType messageType;

    public static NightEventResponse of(String message){
        return new NightEventResponse(message, NIGHT_EVENT);
    }
}

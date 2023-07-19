package com.poscodx.dto;

import static com.poscodx.domain.GameMessageType.*;

import com.poscodx.domain.GameMessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeReductionResponse {

    private String nickname;
    private String message;
    private Integer time;
    private GameMessageType type;

    public static TimeReductionResponse of(String nickname, Integer time) {
        return new TimeReductionResponse(nickname, String.format("%s님이 시간을 단축시켰습니다.", nickname), time, TIME_REDUCTION);
    }
}
